package com.example.bosmobilefinance.ui.slideshow.ui.view.activity.customer

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.network.RetrofitClient
import com.example.bosmobilefinance.databinding.ActivityCustomerEmipageBinding
import com.example.bosmobilefinance.ui.slideshow.adapter.CustomerEMIDetailsAdapter
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.isInternetAvailable
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.CustomerDataItem
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.GetCustomerLoanDetailsReq
import com.example.bosmobilefinance.ui.slideshow.data.repository.AuthRepository
import com.example.bosmobilefinance.ui.slideshow.data.viewModelFactory.CommonViewModelFactory
import com.example.bosmobilefinance.ui.slideshow.localdb.SharedPreference
import com.example.bosmobilefinance.ui.slideshow.ui.viewmodel.AuthenticationViewModel
import com.example.bosmobilefinance.ui.slideshow.utils.ApiStatus
import com.google.gson.Gson

class CustomerEMIPage : AppCompatActivity() {
    lateinit var binding : ActivityCustomerEmipageBinding
    lateinit var adapter : CustomerEMIDetailsAdapter
    lateinit var viewModel: AuthenticationViewModel
    lateinit var preference : SharedPreference
    var customerLoanEmiDetailsList : MutableList<CustomerDataItem?>? = mutableListOf()
    var FilterDataList : MutableList<CustomerDataItem?>? = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCustomerEmipageBinding.inflate(layoutInflater)
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

        preference = SharedPreference(this)
        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface)))[AuthenticationViewModel::class.java]

        if(isInternetAvailable(this@CustomerEMIPage)) {
            HitApiForEmiList()
        }

        setOnClickListner()

    }


    fun setDataOnView(customerLoanEmiDetailsList : MutableList<CustomerDataItem?>?){
        adapter = CustomerEMIDetailsAdapter(this,customerLoanEmiDetailsList)
        binding.showingLoanList.adapter = adapter
        adapter.notifyDataSetChanged()
    }


    fun setOnClickListner(){

        binding.back.setOnClickListener {
            finish()
        }

        binding.searcMobile.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    val search = s.toString().lowercase().trim()
                    val result = customerLoanEmiDetailsList!!.filter {
                        it!!.brandName.lowercase().contains(search) || it.loanCode.lowercase().contains(search)
                    }
                    FilterDataList!!.clear()
                    FilterDataList!!.addAll(result)
                    setDataOnView(FilterDataList)
                    adapter.notifyDataSetChanged()
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            }
        )


    }

    fun HitApiForEmiList(){
        var loanemireq = GetCustomerLoanDetailsReq(
            loancode = "",
            customercode = preference.getStringValue(ConstantClass.CustomerCode,"")
        )
        Log.d("customerloanEmireq",Gson().toJson(loanemireq))

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
                                    customerLoanEmiDetailsList = LoanEmiList
                                    setDataOnView(customerLoanEmiDetailsList)
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


}