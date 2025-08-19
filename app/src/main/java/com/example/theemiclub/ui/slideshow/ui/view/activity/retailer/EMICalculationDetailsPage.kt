package com.example.theemiclub.ui.slideshow.ui.view.activity.retailer

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.network.RetrofitClient
import com.bumptech.glide.Glide
import com.example.theemiclub.R
import com.example.theemiclub.databinding.ActivityEmicalculationDetailsPageBinding
import com.example.theemiclub.ui.slideshow.constant.ConstantClass
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.BrandName
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.ClickOnCardDashboard
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.DownPayment
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.EmiAmount
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.InterestRate
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.MRPPrice
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.ModelColor
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.ModelName
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.ModelVarient
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.SellingPrice
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.Tenure
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.ToBePaidAmount
import com.example.theemiclub.ui.slideshow.data.model.loginsignup.DataItem
import com.example.theemiclub.ui.slideshow.data.model.loginsignup.DataItems
import com.example.theemiclub.ui.slideshow.data.model.loginsignup.GetEMISplitDetlailsReq
import com.example.theemiclub.ui.slideshow.data.repository.AuthRepository
import com.example.theemiclub.ui.slideshow.data.viewModelFactory.CommonViewModelFactory
import com.example.theemiclub.ui.slideshow.ui.viewmodel.AuthenticationViewModel
import com.example.theemiclub.ui.slideshow.utils.ApiStatus
import com.google.gson.Gson

class EMICalculationDetailsPage : AppCompatActivity() {
    lateinit var binding: ActivityEmicalculationDetailsPageBinding
    lateinit var viewModel: AuthenticationViewModel
    private var sellingPriceHandler = Handler(Looper.getMainLooper())
    private var sellingPriceRunnable: Runnable? = null

    companion object {
        var EmiSplitDataModel: MutableList<DataItems> = mutableListOf()
        lateinit var MobileData: DataItem
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmicalculationDetailsPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface))
        )[AuthenticationViewModel::class.java]

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

        setonClickListner()
        hitApiForGetEmiPercent(
            MobileData.brandName,
            MobileData.modelName,
            MobileData.variantName,
            MobileData.mrpPrice,
            MobileData.avlbColors
        )


    }


    fun hitApiForGetEmiPercent(
        brandName: String,
        modelName: String,
        variantName: String,
        mrpPrice: String,
        avlColors: String
    ) {
        var emisplitReq = GetEMISplitDetlailsReq(
            brandName = brandName,
            modelName = modelName,
            variantName = variantName,
            mrpPrice = mrpPrice,
            avlColors = avlColors
        )
        Log.d("EmiPercentReq", Gson().toJson(emisplitReq))

        BrandName = brandName
        ModelName = modelName
        ModelVarient = variantName
        MRPPrice = mrpPrice
        ModelColor = avlColors

        viewModel.getSplitEmiDetails(emisplitReq).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            users.body()?.let { response ->
                                ConstantClass.dialog.dismiss()
                                Log.d("EmiPercentRes", Gson().toJson(response.data))
                                if (response.status.equals("True")) {
                                    var EmiDataList = response.data
                                    if (EmiDataList!!.size > 0) {
                                        EmiSplitDataModel = EmiDataList
                                        binding.processingfees.text =
                                            "₹ ".plus(EmiDataList[0].processingFees)
                                        SellingPrice = EmiDataList[0].sellingPrice
                                        UpdateEmiCalculationOnSellingPrice(EmiDataList, SellingPrice)

                                        /*// binding.totalAmt.text = "₹ ".plus(EmiSplitDataModel[0].sellingPrice)
                                         SellingPrice =  EmiSplitDataModel[0].sellingPrice

                                         binding.tenuretxt.text = EmiDataList[0].tenure.plus(" Months")

                                         val sellingPrice = EmiSplitDataModel[0].sellingPrice.toDouble()
                                         val downPaymentPerc = EmiDataList[0].downPaymentPerc.toDouble() // or .toFloat()
                                         val annualInterestRate = EmiDataList[0].interestPerc
                                         val tenureMonths = EmiDataList[0].tenure.toInt()
                                         val processingFee = EmiDataList[0].processingFees.toDouble()

                                         // Step 1: Calculate down payment
                                         val downPayment = (sellingPrice * downPaymentPerc) / 100
                                         binding.downPayment.text = "₹ ".plus(downPayment)

                                         // Step 2: Calculate principal
                                         val principalAmount = sellingPrice.toDouble() - downPayment

                                         // Step 3: Calculate monthly interest rate
                                         val monthlyInterestRate = annualInterestRate.toDouble() / 12 / 100

                                         println("Monthly Interest Rate = $monthlyInterestRate")

                                         //MonthlyInterestAmount = Principal×MonthlyInterestRate principal amount is loan amount
                                         var MonthlyInterestAmount = principalAmount * monthlyInterestRate

                                         binding.monthlyinterest.text = "₹ ".plus(MonthlyInterestAmount)

                                         // Step 4: EMI Calculation
                                         val tenureYears = tenureMonths / 12.0
                                         val totalInterest = principalAmount.toDouble() * annualInterestRate.toDouble() * tenureYears / 100
                                         val emi = (principalAmount.toDouble() + totalInterest) / tenureMonths

                                         binding.emitxt.text="₹ ".plus("%.2f".format(emi))

                                         val toBePaidNow = downPayment + processingFee
                                         binding.tobepaynowamt.text = "₹ ".plus("%.2f".format(toBePaidNow))


                                         binding.totalAmt.text ="₹ ".plus("%.2f".format(principalAmount))

                                         DownPayment= downPayment.toString()
                                         Tenure = tenureMonths.toString()
                                         EmiAmount = emi.toString()
                                         ConstantClass.LoanAmount = principalAmount
                                         InterestRate = annualInterestRate
                                         ToBePaidAmount = "%.2f".format(toBePaidNow)

                                         setDataOnUI()*/

                                    }
                                }
                            }
                        }
                    }

                    ApiStatus.ERROR -> {
                        ConstantClass.dialog.dismiss()
                    }

                    ApiStatus.LOADING -> {
                        ConstantClass.OpenPopUpForVeryfyOTP(this)
                    }

                }

            }

        }

    }

    fun setDataOnUI(SellingPrice: String) {
        Glide.with(this).load(MobileData.imagePath).placeholder(R.drawable.samsung).error(R.drawable.samsung).into(binding.deviceImage)
        binding.brandname.text = MobileData.brandName
        binding.designtype.text = MobileData.modelName
        binding.editiontxt.text = MobileData.remark
        binding.colorvarient.text = MobileData.avlbColors
        binding.stroage.text = MobileData.variantName
        binding.mrpamount.text = "₹ ".plus(MobileData.mrpPrice)
        // binding.sellingamount.text = "₹ ".plus(EmiSplitDataModel[0].sellingPrice)
        binding.sellingamount.setText(SellingPrice)

        if (ClickOnCardDashboard.equals("Customer")) {
            binding.nextbuttonlayout.visibility = View.VISIBLE
        }
        if (ClickOnCardDashboard.equals("Product")) {
            binding.nextbuttonlayout.visibility = View.GONE
        }
    }


    fun setonClickListner() {

        binding.back.setOnClickListener {
            finish()
        }



        var isclick = true
        var lastSellingPrice: String? = null

        binding.sellingamount.isCursorVisible = true
        binding.sellingamount.requestFocus()

        binding.sellingamount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isclick) {
                    isclick = false
                    return
                }

                val enteredPriceStr = s.toString().trim()

                if (enteredPriceStr.isEmpty()) {
                    val minSellingPriceStr = EmiSplitDataModel[0].sellingPrice
                    isclick = true // prevent re-trigger
                    binding.sellingamount.setText(minSellingPriceStr)
                    binding.sellingamount.setSelection(minSellingPriceStr.length) // keep cursor blinking at end
                    return
                }

                val enteredPrice = enteredPriceStr.toDoubleOrNull() ?: return
                val minSellingPrice = EmiSplitDataModel[0].sellingPrice.toDoubleOrNull() ?: 0.0
                val mrpPrice = binding.mrpamount.text.toString().replace("₹", "").trim().toDoubleOrNull() ?: 0.0

                when {
                    enteredPrice < minSellingPrice -> {
                        binding.sellingamount.error = "Selling Price cannot be less than ₹$minSellingPrice"
                        return
                    }

                    enteredPrice > mrpPrice -> {
                        binding.sellingamount.error = "Selling Price cannot be more than MRP ₹$mrpPrice"
                        return
                    }

                    else -> {
                        // ✅ Call API only if value changed from last call
                        if (enteredPriceStr != lastSellingPrice) {
                            lastSellingPrice = enteredPriceStr
                            if (EmiSplitDataModel.isNotEmpty()) {
                                UpdateEmiCalculationOnSellingPrice(EmiSplitDataModel, enteredPriceStr)
                            }
                        }
                        // ✅ Keep cursor at last index
                        binding.sellingamount.setSelection(s?.length ?: 0)
                    }
                }
            }
        })



        binding.nextbuttonlayout.setOnClickListener {
            /*if(ClickOnCardDashboard.equals("Customer"))
            {*/
            if (!ToBePaidAmount.isNullOrBlank()) {
                startActivity(
                    Intent(
                        this@EMICalculationDetailsPage,
                        PaymentInformation::class.java
                    )
                )
            }

            /* }
             else
             {
                 startActivity(Intent(this@EMICalculationDetailsPage, IDVerificationPage::class.java))
             }*/

        }

    }


    fun UpdateEmiCalculationOnSellingPrice(EmiDataList: MutableList<DataItems>, SellingPrice: String) {

        binding.tenuretxt.text = EmiDataList[0].tenure.plus(" Months")

        val sellingPrice = SellingPrice.toDouble()
        val downPaymentPerc = EmiDataList[0].downPaymentPerc.toDouble() // or .toFloat()
        val annualInterestRate = EmiDataList[0].interestPerc
        val tenureMonths = EmiDataList[0].tenure.toInt()
        val processingFee = EmiDataList[0].processingFees.toDouble()

        // Step 1: Calculate down payment
        val downPayment = (sellingPrice * downPaymentPerc) / 100
        binding.downPayment.text = "₹ ".plus("%.2f".format(downPayment))

        // Step 2: Calculate principal
        val principalAmount = sellingPrice.toDouble() - downPayment

        // Step 3: Calculate monthly interest rate
        val monthlyInterestRate = annualInterestRate.toDouble() / 12 / 100

        println("Monthly Interest Rate = $monthlyInterestRate")

        //MonthlyInterestAmount = Principal×MonthlyInterestRate principal amount is loan amount
        var MonthlyInterestAmount = principalAmount * monthlyInterestRate

        binding.monthlyinterest.text = "₹ ".plus("%.2f".format(MonthlyInterestAmount))

        // Step 4: EMI Calculation
        val tenureYears = tenureMonths / 12.0
        val totalInterest =
            principalAmount.toDouble() * annualInterestRate.toDouble() * tenureYears / 100
        val emi = (principalAmount.toDouble() + totalInterest) / tenureMonths

        binding.emitxt.text = "₹ ".plus("%.2f".format(emi))

        val toBePaidNow = downPayment + processingFee
        binding.tobepaynowamt.text = "₹ ".plus("%.2f".format(toBePaidNow))


        binding.totalAmt.text = "₹ ".plus("%.2f".format(principalAmount))

        DownPayment = downPayment.toString()
        Tenure = tenureMonths.toString()
        EmiAmount = emi.toString()
        ConstantClass.LoanAmount = principalAmount
        InterestRate = annualInterestRate
        ToBePaidAmount = "%.2f".format(toBePaidNow)

        setDataOnUI(SellingPrice)
    }


}