package com.example.theemiclub.ui.slideshow.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.network.RetrofitClient
import com.example.theemiclub.R
import com.example.theemiclub.databinding.ActivityDashBoardBinding
import com.example.theemiclub.databinding.NavHeaderDashBoardBinding
import com.example.theemiclub.ui.slideshow.constant.ConstantClass
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.Retailer
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.isInternetAvailable
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.loginType
import com.example.theemiclub.ui.slideshow.data.model.loginsignup.GetCustomerLoanDetailsReq
import com.example.theemiclub.ui.slideshow.data.repository.AuthRepository
import com.example.theemiclub.ui.slideshow.data.viewModelFactory.CommonViewModelFactory
import com.example.theemiclub.ui.slideshow.localdb.SharedPreference
import com.example.theemiclub.ui.slideshow.ui.view.activity.ChooseYourRolePage
import com.example.theemiclub.ui.slideshow.ui.view.activity.customer.CustomerEMIPage
import com.example.theemiclub.ui.slideshow.ui.view.activity.customer.CustomerReportsPage
import com.example.theemiclub.ui.slideshow.ui.view.activity.customer.MakePaymentPage
import com.example.theemiclub.ui.slideshow.ui.view.activity.retailer.IDVerificationPage
import com.example.theemiclub.ui.slideshow.ui.view.activity.retailer.MobileSelectionActivity
import com.example.theemiclub.ui.slideshow.ui.view.activity.retailer.NewCustomerRegistrationPage
import com.example.theemiclub.ui.slideshow.ui.view.activity.retailer.RetailerCustomerReportsPage
import com.example.theemiclub.ui.slideshow.ui.viewmodel.AuthenticationViewModel
import com.example.theemiclub.ui.slideshow.utils.ApiStatus
import com.google.gson.Gson

class DashBoard : AppCompatActivity() {
    private lateinit var binding: ActivityDashBoardBinding
    private lateinit var headerBinding : NavHeaderDashBoardBinding
    lateinit var preference : SharedPreference
    var count: Int = 0
    lateinit var dialog : Dialog

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.appBarDashBoard.root) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            view.setPadding(
                systemBarsInsets.left,
                systemBarsInsets.top,
                systemBarsInsets.right,
                systemBarsInsets.bottom
            )

            WindowInsetsCompat.CONSUMED
        }

        preference = SharedPreference(this)

        val navView = binding.navView  // If using main layout binding
        val headerView = navView.getHeaderView(0)
        headerBinding = NavHeaderDashBoardBinding.bind(headerView)


        val versionName = packageManager.getPackageInfo(packageName, 0).versionName
        binding.appVersionText.text = "Version $versionName"

        setonclickListner()
        setDataHeader()

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun setDataHeader() {
        val firstName = preference.getStringValue(ConstantClass.FirstName, "").orEmpty()
        val lastName = preference.getStringValue(ConstantClass.LastName, "").orEmpty()
        val emailId = preference.getStringValue(ConstantClass.CustomerEmailID, "").orEmpty()
        val logintype = preference.getStringValue(ConstantClass.LoginType, "").orEmpty()

        val safeLastName = if (!lastName.isNullOrBlank() && lastName != "null") lastName else ""
        val safeEmail = if (!emailId.isNullOrBlank() && emailId != "null") emailId else ""

        headerBinding.username.text = firstName.plus(" ").plus(safeLastName)
        headerBinding.emailid.text = safeEmail

        if(logintype.equals(Retailer)){
            binding.appBarDashBoard.deskdesign.retailersDashboard.visibility= View.VISIBLE
            binding.appBarDashBoard.deskdesign.customerDashboard.visibility= View.GONE
            binding.appBarDashBoard.deskdesign.subtitle.text = "One Tap to Your Next Loan"

        }
        else{
            binding.appBarDashBoard.deskdesign.retailersDashboard.visibility= View.GONE
            binding.appBarDashBoard.deskdesign.customerDashboard.visibility= View.VISIBLE
            binding.appBarDashBoard.deskdesign.subtitle.text = "Track Your Loan. Pay with Ease"

        }


    }


    fun setonclickListner(){

        binding.navView.setNavigationItemSelectedListener { menuItem->
            when(menuItem.itemId){
                R.id.logout -> {
                    OpenPopUpForVeryfyOTP()
                    true
                }
                else -> false
            }
        }


        binding.appBarDashBoard.deskdesign.menuicon.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }


        headerBinding.drawableclose.setOnClickListener {
            binding.drawerLayout.closeDrawers()
        }


        binding.appBarDashBoard.deskdesign.emicard.setOnClickListener {
            ConstantClass.ClickOnCardDashboard = "Product"
            startActivity(Intent(this@DashBoard, MobileSelectionActivity::class.java))
        }


        binding.appBarDashBoard.deskdesign.customer.setOnClickListener {
            ConstantClass.ClickOnCardDashboard = "Customer"
            startActivity(Intent(this@DashBoard, IDVerificationPage::class.java))
        }


        binding.appBarDashBoard.deskdesign.customeremicard.setOnClickListener {
            startActivity(Intent(this@DashBoard, CustomerEMIPage::class.java))
        }


        binding.appBarDashBoard.deskdesign.customerMakePaymentCard.setOnClickListener {
            startActivity(Intent(this@DashBoard, MakePaymentPage::class.java))
        }


        binding.appBarDashBoard.deskdesign.credit.setOnClickListener {
            startActivity(Intent(this@DashBoard, RetailerCustomerReportsPage::class.java))
        }


        binding.appBarDashBoard.deskdesign.customerreports.setOnClickListener {
            startActivity(Intent(this@DashBoard, CustomerReportsPage::class.java))
        }


    }


    override fun onBackPressed() {
        if(count==0){
            count++
            Toast.makeText(this@DashBoard,"Press again to exit",Toast.LENGTH_SHORT).show()
        }else{
            super.onBackPressed()
        }

    }


    fun OpenPopUpForVeryfyOTP(){
        dialog = Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.signoutalert)


        dialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }


        dialog.setCanceledOnTouchOutside(false)

        val cancel = dialog.findViewById<Button>(R.id.btnCancel)
        val done = dialog.findViewById<Button>(R.id.btnLogout)

        done.setOnClickListener{
            preference.setBooleanValue(ConstantClass.LoggedIn, false )
            preference.setStringValue(ConstantClass.LoginType,"")
            ConstantClass.ClickOnCardDashboard=""
            val intent = Intent(this@DashBoard, ChooseYourRolePage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }


        cancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

    }

}