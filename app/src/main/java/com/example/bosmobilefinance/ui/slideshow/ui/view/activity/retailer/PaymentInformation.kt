package com.example.bosmobilefinance.ui.slideshow.ui.view.activity.retailer

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bosmobilefinance.R
import com.example.bosmobilefinance.databinding.ActivityPaymentInformationBinding
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.AccountNumber
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.AccountType
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.BankIFSCCode
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.BankName
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.BranchName
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.RefAddress
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.RefName
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.RefRelationShip
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.RefmobileNo

class PaymentInformation : AppCompatActivity() {
    lateinit var  binding : ActivityPaymentInformationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPaymentInformationBinding.inflate(layoutInflater)
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

        setDataInSpinner()
        setOnClickListner()
    }

    fun setDataInSpinner(){
        val adapter = ArrayAdapter.createFromResource(this,  R.array.accounttype, R.layout.mobilenamelayout)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.acounttype.adapter = adapter

    }


    fun setOnClickListner(){
        binding.back.setOnClickListener {
            finish()
        }

        binding.nextlayout.setOnClickListener {

            val (isValid, errorMessage) = isValidForm(
                accountNumber = binding.accountnumber.text.toString().trim(),
                ifscCode = binding.ifsccode.text.toString().trim(),
                bankName = binding.bankname.text.toString().trim(),
                accountType = binding.acounttype.selectedItem.toString().trim(),
                branchName = binding.branchname.text.toString().trim(),
                referName = binding.refername.text.toString().trim(),
                referRelation = binding.referrelatinonship.text.toString().trim(),
                refMobile = binding.refmobno.text.toString().trim(),
                refAddress = binding.refaddress.text.toString().trim())

            if (!isValid) {
                Toast.makeText(this@PaymentInformation, errorMessage, Toast.LENGTH_SHORT).show()
            }
            else{
                AccountNumber = binding.accountnumber.text.toString().trim()
                BankIFSCCode = binding.ifsccode.text.toString().trim()
                BankName = binding.bankname.text.toString().trim()
                AccountType = binding.acounttype.selectedItem.toString().trim()
                BranchName = binding.branchname.text.toString().trim()
                RefName = binding.refername.text.toString().trim()
                RefRelationShip = binding.referrelatinonship.text.toString().trim()
                RefmobileNo = binding.refmobno.text.toString().trim()
                RefAddress =  binding.refaddress.text.toString().trim()
                startActivity(Intent(this@PaymentInformation, IMEIDetailsPage::class.java))
            }

        }

    }


    fun isValidForm(
        accountNumber: String,
        ifscCode: String,
        bankName: String,
        accountType: String,
        branchName: String,
        referName: String,
        referRelation: String,
        refMobile: String,
        refAddress: String
    ): Pair<Boolean, String?> {
        // Bank details
        if (accountNumber.isBlank()) return Pair(false, "Enter account number")

        if (!ifscCode.matches(Regex("^[A-Z]{4}0[A-Z0-9]{6}$"))) return Pair(false, "Enter valid IFSC code")

        if (bankName.isBlank()) return Pair(false, "Enter bank name")

        if (accountType.isBlank()) return Pair(false, "Select account type")

        if (branchName.isBlank()) return Pair(false, "Enter branch name")

        // Reference details
        if (referName.isBlank()) return Pair(false, "Enter reference name")

        if (referRelation.isBlank()) return Pair(false, "Enter reference relationship")

        if (!refMobile.matches(Regex("^[6-9]\\d{9}$"))) return Pair(false, "Enter valid reference mobile number")

        if (refAddress.isBlank()) return Pair(false, "Enter reference address")

        return Pair(true, null)
    }



}