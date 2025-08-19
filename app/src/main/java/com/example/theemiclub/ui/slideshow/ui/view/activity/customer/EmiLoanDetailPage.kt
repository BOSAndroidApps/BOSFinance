package com.example.theemiclub.ui.slideshow.ui.view.activity.customer

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bos.payment.appName.network.RetrofitClient
import com.example.theemiclub.R
import com.example.theemiclub.databinding.ActivityEmiLoanDetailPageBinding
import com.example.theemiclub.ui.slideshow.activity.DashBoard
import com.example.theemiclub.ui.slideshow.constant.ConstantClass
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.formatDateToDDMMYYYY
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.getCurrentUtcTimestamp
import com.example.theemiclub.ui.slideshow.data.model.loginsignup.CustomerLoanEmiReceiveReq
import com.example.theemiclub.ui.slideshow.data.model.loginsignup.GetCustomerLoanDetailsReq
import com.example.theemiclub.ui.slideshow.data.repository.AuthRepository
import com.example.theemiclub.ui.slideshow.data.viewModelFactory.CommonViewModelFactory
import com.example.theemiclub.ui.slideshow.localdb.SharedPreference
import com.example.theemiclub.ui.slideshow.ui.view.activity.ChooseYourRolePage
import com.example.theemiclub.ui.slideshow.ui.view.activity.retailer.NewCustomerRegistrationPage
import com.example.theemiclub.ui.slideshow.ui.viewmodel.AuthenticationViewModel
import com.example.theemiclub.ui.slideshow.utils.ApiStatus
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class EmiLoanDetailPage : AppCompatActivity() {
    lateinit var binding : ActivityEmiLoanDetailPageBinding
    lateinit var dialog : Dialog
    lateinit var viewModel: AuthenticationViewModel
    var loanCode: String = ""
    var emiAmount: String = ""
    var paymentDate: String = ""
    var paidAmount: String = ""
    var paymentMode: String = ""
    var createdBy: String = ""
    lateinit var preference : SharedPreference

    companion object{
      lateinit var LoanId : String
      lateinit var customerCode : String
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEmiLoanDetailPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            view.setPadding(
                systemBarsInsets.left,
                systemBarsInsets.top,
                systemBarsInsets.right,
                systemBarsInsets.bottom
            )

            WindowInsetsCompat.CONSUMED
        }

        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface)))[AuthenticationViewModel::class.java]
        preference = SharedPreference(this)
        HitApiForEmiList()
        setOnClickListner()

    }


    fun setDataForProgress(paidAmount:Double, remainingAmount:Double){
        val totalAmount = paidAmount + remainingAmount
        val progressPercent = (paidAmount * 100) / totalAmount

        binding.progressBar.max = 100
        binding.progressBar.progress = progressPercent.toInt()

    }


    fun setDataInspinner(tenure:Int,emiAmount:Double){
        val companyAdapter = ArrayAdapter.createFromResource(this,  R.array.paymentmode, R.layout.mobilenamelayout)
        companyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.paymentmode.adapter = companyAdapter

        val emiOptions = ConstantClass.generateEMIOptions(tenure)
        val noOfEmiAdapter = ArrayAdapter(this, R.layout.mobilenamelayout, emiOptions )
        noOfEmiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.noOfEmi.adapter = noOfEmiAdapter

        binding.noOfEmi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString().toInt()
                val totalEmiAmount = emiAmount * selectedItem
                binding.amount.text = "₹ ". plus(totalEmiAmount)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Optional
            }

        }


    }


    fun setOnClickListner(){

       binding.back.setOnClickListener {
           finish()
       }

       binding.submitpayment.setOnClickListener {
           var selectedNoofEmi = binding.noOfEmi.selectedItem.toString().toInt()
           ConstantClass.OpenPopUpForVeryfyOTP(this)
           lifecycleScope.launch {
               for (i in 1..selectedNoofEmi) {
                   HitApiForPayEmiAmount(selectedNoofEmi,i)
                   delay(1000) // wait 1 second between calls
               }
           }

       }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun HitApiForEmiList(){
        var loanemireq = GetCustomerLoanDetailsReq(
            loancode = LoanId,
            customercode = "")
        Log.d("customerloanEmireq", Gson().toJson(loanemireq))
        viewModel.getCustomerLoanEmiDetailsReq(loanemireq).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            users.body()?.let {
                                response ->
                                Log.d("customerLoanemiresp", response.toString())
                                if(ConstantClass.dialog!=null && ConstantClass.dialog.isShowing){
                                    ConstantClass.dialog.dismiss()
                                    var LoanEmiList = response.data
                                    binding.loanamount.text = "₹ " .plus(LoanEmiList!![0]!!.loanAmount.toString())

                                    var amount = calculateEMIPaymentStatus(LoanEmiList!![0]!!.paidEmi.toInt(),LoanEmiList!![0]!!.emiAmount.toDouble(), LoanEmiList!![0]!!.tenure)

                                    binding.paidamount.text = "₹ ". plus(amount.first)
                                    binding.remainamount.text = "₹ ".plus(amount.second)

                                    setDataForProgress(amount.first,amount.second)

                                    binding.monthlyemi.text = LoanEmiList!![0]!!.emiAmount.toString()
                                    binding.modelname.text = LoanEmiList!![0]!!.modelName.toString()
                                    val monthlyInterestRate = LoanEmiList!![0]!!.interestRate.toDouble() / 12 / 100
                                    var MonthlyInterestAmount = LoanEmiList!![0]!!.loanAmount.toDouble()  * monthlyInterestRate
                                    binding.interest.text = "₹ ". plus(MonthlyInterestAmount)
                                    binding.startdate.text = formatDateToDDMMYYYY(LoanEmiList!![0]!!.startDate.toString())
                                    binding.enddate.text = formatDateToDDMMYYYY(LoanEmiList!![0]!!.endDate.toString())
                                    binding.noofpaidemi.text = LoanEmiList!![0]!!.paidEmi.toString()
                                    binding.pendingnoofemi.text = LoanEmiList!![0]!!.duesEmi.toString()
                                    binding.color.text = LoanEmiList!![0]!!.avlbColors.toString()
                                    setDataInspinner(LoanEmiList!![0]!!.duesEmi.toInt(),LoanEmiList!![0]!!.emiAmount)
                                    loanCode = LoanEmiList!![0]!!.loanCode.toString()
                                    emiAmount = LoanEmiList!![0]!!.emiAmount.toString()
                                    paidAmount = LoanEmiList!![0]!!.emiAmount.toString()

                                    if(LoanEmiList!![0]!!.paidEmi.toString()==LoanEmiList!![0]!!.tenure.toString()){
                                        binding.emidetailspaynowlayout.visibility=View.GONE
                                        binding.doneemitext.visibility=View.VISIBLE
                                    }else{
                                        binding.emidetailspaynowlayout.visibility=View.VISIBLE
                                        binding.doneemitext.visibility=View.GONE
                                    }

                                }
                            }

                        }

                    }

                    ApiStatus.ERROR -> {
                        if(ConstantClass.dialog!=null && ConstantClass.dialog.isShowing){
                            ConstantClass.dialog.dismiss()
                        }

                    }

                    ApiStatus.LOADING -> {
                        ConstantClass.OpenPopUpForVeryfyOTP(this)
                    }

                }
            }
        }
    }



    fun HitApiForPayEmiAmount(emicount:Int,loopcount :Int){
        val firstName = preference.getStringValue(ConstantClass.FirstName, "").orEmpty()
        val lastName = preference.getStringValue(ConstantClass.LastName, "").orEmpty()
        val safeLastName = if (!lastName.isNullOrBlank() && lastName != "null") lastName else ""
        val createdBy = firstName.plus(" ").plus(safeLastName)
        var customercode =  preference.getStringValue(ConstantClass.CustomerCode, "")
        var retailercode =  preference.getStringValue(ConstantClass.RetailerCode, "")

        var loanEmiReceiveReq = CustomerLoanEmiReceiveReq(
            mode = "INSERT",
            rid = 0,
            loanCode = loanCode,
            emiAmount = emiAmount,
            paymentDate = getCurrentUtcTimestamp(),
            paidAmount = paidAmount,
            paymentMode = binding.paymentmode.selectedItem.toString(),
            utrNumber = "",
            remarks = binding.remarkEdt.text.toString(),
            creatdeBy = createdBy,
            receiptNo = "",
            retailercode = retailercode,
            customerCode = customercode
        )
        Log.d("loanEmiReceiveReq", Gson().toJson(loanEmiReceiveReq))
        viewModel.getCustomerLoanDetailsReq(loanEmiReceiveReq).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            users.body()?.let {
                                    response ->
                                     Log.d("loanEmiReceiveResp", response.toString())
                                     if(loopcount==emicount){
                                         if(ConstantClass.dialog!=null && ConstantClass.dialog.isShowing){
                                             ConstantClass.dialog.dismiss()
                                         }
                                          OpenPopUpForVeryfyOTP()
                                     }
                                 }

                        }

                    }

                    ApiStatus.ERROR -> {
                        if(ConstantClass.dialog!=null && ConstantClass.dialog.isShowing){
                            ConstantClass.dialog.dismiss()
                        }

                    }

                    ApiStatus.LOADING -> {

                    }

                }
            }
        }
    }


    fun OpenPopUpForVeryfyOTP(){
        dialog = Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.layout_for_payment)

        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

            statusBarColor = Color.TRANSPARENT
            navigationBarColor = Color.TRANSPARENT
        }

        var exitbutton = dialog.findViewById<LinearLayout>(R.id.exitlayout)
        var shareImage = dialog.findViewById<ImageView>(R.id.shareimage)

        shareImage.setOnClickListener {
            shareImageFromDrawable(this, R.drawable.bosqrimage)
        }


        exitbutton.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this@EmiLoanDetailPage, DashBoard::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }


        dialog.setOnDismissListener{
            dialog.dismiss()
            val intent = Intent(this@EmiLoanDetailPage, DashBoard::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }


        dialog.setCanceledOnTouchOutside(false)

        dialog.show()

    }



    fun calculateEMIPaymentStatus(paidEMI: Int, emiAmount: Double, tenure: Int): Pair<Double, Double> {
        val paidAmount = paidEMI * emiAmount
        val remainingAmount = (tenure - paidEMI) * emiAmount
        return Pair(paidAmount, remainingAmount)
    }







    fun shareImageFromDrawable(context: Context, drawableResId: Int) {
        // Convert drawable to bitmap
        val bitmap = BitmapFactory.decodeResource(context.resources, drawableResId)

        // Save bitmap to cache directory
        val cachePath = File(context.cacheDir, "images")
        cachePath.mkdirs() // Create directory if not exists
        val file = File(cachePath, "shared_image.png")
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.close()

        // Get content URI
        val imageUri: Uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)

        // Share Intent
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, imageUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(Intent.createChooser(shareIntent, "Share Image"))
    }


}