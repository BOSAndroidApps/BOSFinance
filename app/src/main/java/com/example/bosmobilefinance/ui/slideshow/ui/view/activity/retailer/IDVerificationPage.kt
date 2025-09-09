package com.example.bosmobilefinance.ui.slideshow.ui.view.activity.retailer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.network.RetrofitClient
import com.example.bosmobilefinance.R
import com.example.bosmobilefinance.databinding.ActivityIdverificationPageBinding
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.AadharTransactionIdNo
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.CheckOnlineOrOffline
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.PanFrontImageUri
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.PanNumber
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.verification.AadharVerificationReq
import com.example.bosmobilefinance.ui.slideshow.data.repository.AuthRepository
import com.example.bosmobilefinance.ui.slideshow.data.viewModelFactory.CommonViewModelFactory
import com.example.bosmobilefinance.ui.slideshow.localdb.SharedPreference
import com.example.bosmobilefinance.ui.slideshow.ui.view.activity.retailer.AadharCardWebViewDIGILockerPage.Companion.digilockerLink
import com.example.bosmobilefinance.ui.slideshow.ui.viewmodel.AuthenticationViewModel
import com.example.bosmobilefinance.ui.slideshow.utils.ApiStatus
import com.google.gson.Gson

class IDVerificationPage : AppCompatActivity() {
    lateinit var binding : ActivityIdverificationPageBinding
    lateinit var viewModel: AuthenticationViewModel
    lateinit var preference : SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIdverificationPageBinding.inflate(layoutInflater)

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

        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterfacePAN)))[AuthenticationViewModel::class.java]
        preference = SharedPreference(this)

        setOnClickListner()

    }

    fun setOnClickListner() {

        binding.back.setOnClickListener {
            onBackPressed()
        }

        binding.aadharcardlayout.setOnClickListener {
            if (PanNumber.isBlank()) {
                Toast.makeText(
                    this@IDVerificationPage,
                    "Please Verify Pan Card first!!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (ConstantClass.CheckOnlineOrOffline.equals(ConstantClass.online)) {
                    hitApiForAadharVerification()
                } else {
                    startActivity(
                        Intent(
                            this@IDVerificationPage,
                            AadharCardVerificationPage::class.java
                        )
                    )
                }
            }
        }

        binding.pancardlayout.setOnClickListener {
            if (CheckOnlineOrOffline.isBlank()) {
                Toast.makeText(
                    this@IDVerificationPage,
                    "Please select mode first!!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                startActivity(Intent(this@IDVerificationPage, PanCardVerificationPage::class.java))
            }

        }

        binding.cibilcardlayout.setOnClickListener {
            startActivity(Intent(this@IDVerificationPage, CivilReportForm::class.java))
        }

        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->

            val radioButton = group.findViewById<RadioButton>(checkedId)

            if (radioButton != null && radioButton.isPressed) {
                when (checkedId) {
                    R.id.radioButton1 -> {
                        CheckOnlineOrOffline = ConstantClass.online
                        PanNumber=""
                        PanFrontImageUri= null
                        ConstantClass.AadharVerified=""
                        binding.pancardlayout.isEnabled= true
                        AadharTransactionIdNo=""
                        onResume()
                    }

                    R.id.radioButton2 -> {
                        CheckOnlineOrOffline = ConstantClass.offline
                        PanNumber=""
                        PanFrontImageUri= null
                        ConstantClass.AadharVerified=""
                        binding.pancardlayout.isEnabled= true
                        AadharTransactionIdNo=""
                        onResume()
                    }
                }
            }

        }

    }


    override fun onResume() {
        super.onResume()

        if(PanNumber.isBlank()){
            binding.donepancard.visibility= View.GONE
            binding.pancardlayout.isEnabled= true
        }else{
            binding.donepancard.visibility= View.VISIBLE
            binding.pancardlayout.isEnabled= false
        }

        if(ConstantClass.AadharVerified.equals("no")&& ConstantClass.CheckOnlineOrOffline.equals(ConstantClass.online)){
            binding.doneaadhaar.visibility= View.VISIBLE
        }
        else{
            binding.doneaadhaar.visibility= View.GONE
        }


    }



    fun hitApiForAadharVerification(){
        val firstName = preference.getStringValue(ConstantClass.FirstName, "").orEmpty()
        val lastName = preference.getStringValue(ConstantClass.LastName, "").orEmpty()
        val emailId = preference.getStringValue(ConstantClass.CustomerEmailID, "").orEmpty()
        val mob = preference.getStringValue(ConstantClass.CustomerMobileNumber, "").orEmpty()

        var aadharverificationreq = AadharVerificationReq(
            firstName = firstName,
            lastName = lastName,
            mobileNumber = mob,
            emailId = emailId,
            registrationId = ConstantClass.PAN_VERIFICATION_REGISTRATION_ID,
        )

        Log.d("AadharVerificationreq", Gson().toJson(aadharverificationreq))

        viewModel.getAadharVerificationReq(aadharverificationreq).observe(this){
                resources->resources.let{
            when(it.apiStatus){
                ApiStatus.SUCCESS->{
                    it.data.let {
                        users -> users!!.body().let { response ->
                        ConstantClass.dialog.dismiss()
                        Log.d("AadharVerificationResp", Gson().toJson(response))

                        if(response!!.code==null){
                            Toast.makeText(this@IDVerificationPage,response.message,Toast.LENGTH_SHORT).show()
                        }
                        if(response!!.code.equals("200")){
                            digilockerLink = response!!.model.kycUrl
                            AadharTransactionIdNo= response.model.transactionId
                            startActivity(Intent(this@IDVerificationPage,AadharCardWebViewDIGILockerPage::class.java))
                            finish()
                        }
                        else{
                            Toast.makeText(this@IDVerificationPage,response.message,Toast.LENGTH_SHORT).show()
                        }
                      }
                    }
                }
                ApiStatus.ERROR->{
                    ConstantClass.dialog.dismiss()
                }

                ApiStatus.LOADING->{
                    ConstantClass.OpenPopUpForVeryfyOTP(this)
                }
            }
        }
        }

    }



    override fun onBackPressed() {
        PanNumber=""
        PanFrontImageUri= null
        ConstantClass.AadharVerified=""
        CheckOnlineOrOffline=""
        AadharTransactionIdNo=""
        super.onBackPressed()
    }


}