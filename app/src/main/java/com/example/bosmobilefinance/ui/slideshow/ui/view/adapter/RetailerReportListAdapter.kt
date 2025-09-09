package com.example.bosmobilefinance.ui.slideshow.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bosmobilefinance.databinding.RetailersReportsLayoutBinding
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.formatDateToFullMonth
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.reports.ReportsDataItem

class RetailerReportListAdapter(private val  ReportsDataList : MutableList<ReportsDataItem> = mutableListOf(), var context:Context): RecyclerView.Adapter<RetailerReportListAdapter.ViewHolder>() {


    class ViewHolder (private val binding: RetailersReportsLayoutBinding): RecyclerView.ViewHolder(binding.root) {
      var productDetails = binding.productDetails
      var emiAmount = binding.emiAmount
      var loancode = binding.loanCode
      var customername = binding.customername
      var customercode = binding.customercode
      var loanamount = binding.loanamount
      var downpayment = binding.downpayment
      var tenure = binding.tenure
      var paidemi = binding.paidEmi
      var dueemi = binding.duesEmi
      var duedate = binding.duedate
      var dueMob = binding.custMob
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RetailersReportsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun getItemCount(): Int = ReportsDataList.size



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

      holder.productDetails.text = ReportsDataList[position].productDetails
      holder.emiAmount.text = "₹".plus(ReportsDataList[position].emiAmount)
      holder.loancode.text = ReportsDataList[position].loanCode
      holder.customername.text =ReportsDataList[position].customerName
      holder.customercode.text = ReportsDataList[position].customerCode
      holder.loanamount.text ="Loan amount : ₹ " .plus(ReportsDataList[position].loanAmount)
      holder.downpayment.text = "Down payment : ₹ " .plus(ReportsDataList[position].downPayment)
      holder.tenure.text ="Tenure : " .plus(ReportsDataList[position].tenure)
      holder.paidemi.text ="Paid emi : " .plus(ReportsDataList[position].paidEmi)
      holder.dueemi.text = "Due emi : " .plus(ReportsDataList[position].duesEmi)
      holder.duedate.text =formatDateToFullMonth(ReportsDataList[position].dueDate)
      holder.dueMob.text = ReportsDataList[position].custerMob

    }

}