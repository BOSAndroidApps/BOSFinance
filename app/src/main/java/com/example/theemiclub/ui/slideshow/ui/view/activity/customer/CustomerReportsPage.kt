package com.example.theemiclub.ui.slideshow.ui.view.activity.customer

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.network.RetrofitClient
import com.example.theemiclub.R
import com.example.theemiclub.databinding.ActivityCustomerReportsPageBinding
import com.example.theemiclub.ui.slideshow.adapter.RetailerReportListAdapter
import com.example.theemiclub.ui.slideshow.constant.ConstantClass
import com.example.theemiclub.ui.slideshow.data.model.loginsignup.reports.GetReportsReq
import com.example.theemiclub.ui.slideshow.data.model.loginsignup.reports.ReportsDataItem
import com.example.theemiclub.ui.slideshow.data.repository.AuthRepository
import com.example.theemiclub.ui.slideshow.data.viewModelFactory.CommonViewModelFactory
import com.example.theemiclub.ui.slideshow.localdb.SharedPreference
import com.example.theemiclub.ui.slideshow.ui.viewmodel.AuthenticationViewModel
import com.example.theemiclub.ui.slideshow.utils.ApiStatus
import com.google.gson.Gson

class CustomerReportsPage : AppCompatActivity() {
    lateinit var binding : ActivityCustomerReportsPageBinding
    lateinit var preference : SharedPreference
    lateinit var viewModel: AuthenticationViewModel
    var ReportDataList : MutableList<ReportsDataItem> = mutableListOf()
    lateinit var reportAdapter : RetailerReportListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerReportsPageBinding.inflate(layoutInflater)
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

    setview()
    hitApiForGetReports(binding.reporttype.selectedItem.toString())
    setclicklistner()

}

fun setview(){
    val adapter = ArrayAdapter.createFromResource(this,  R.array.reporttype, R.layout.mobilenamelayout)
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    binding.reporttype.adapter = adapter

    var isSpinnerFirstCall = true // declare outside the listener

    binding.reporttype.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
            if (isSpinnerFirstCall) {
                isSpinnerFirstCall = false
                return // skip the first auto-call
            }

            val selectedItem = parent.getItemAtPosition(position).toString()
            hitApiForGetReports(selectedItem)
        }

        override fun onNothingSelected(parent: AdapterView<*>) {
            // nothing
        }
    }


}

fun setclicklistner(){

    binding.back.setOnClickListener {
        finish()
    }

}


fun hitApiForGetReports( reporttype:String) {
    var customerCode = preference.getStringValue(ConstantClass.CustomerCode,"")
    var recordStatus = reporttype
    var reportreq = GetReportsReq(
        retailercode = "",
        recordStatus = recordStatus,
        customercode = customerCode
    )
    Log.d("ReportReq", Gson().toJson(reportreq))
    viewModel.getReportsReq(reportreq).observe(this){
            resources->resources.let {
        when(it.apiStatus){
            ApiStatus.SUCCESS -> {
                it.data?.let { users ->
                    users.body()?.let { response ->
                        ConstantClass.dialog.dismiss()
                        Log.d("MobileRes", Gson().toJson(ReportDataList) )
                        ReportDataList = response.data!!.toMutableList()
                        if(ReportDataList.size>0){
                            binding.showreports.visibility= View.VISIBLE
                            binding.notfoundimage.visibility= View.GONE
                            reportAdapter = RetailerReportListAdapter(ReportDataList,this)
                            binding.showreports.adapter = reportAdapter
                            reportAdapter.notifyDataSetChanged()

                        }else{
                            binding.showreports.visibility= View.GONE
                            binding.notfoundimage.visibility= View.VISIBLE
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


}