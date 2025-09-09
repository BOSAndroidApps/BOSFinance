package com.example.theemiclub.ui.slideshow.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.theemiclub.R
import com.example.theemiclub.databinding.MobilelistitemlayoutBinding
import com.example.theemiclub.ui.slideshow.ui.view.activity.retailer.EMICalculationDetailsPage
import com.example.theemiclub.ui.slideshow.ui.view.activity.retailer.EMICalculationDetailsPage.Companion.MobileData
import com.example.theemiclub.ui.slideshow.data.model.loginsignup.DataItem

class MobileListAdapter(private val  MobileDataList : MutableList<DataItem> = mutableListOf(), var context:Context): RecyclerView.Adapter<MobileListAdapter.ViewHolder>() {
    var  selectPosition = -1

    class ViewHolder (private val binding: MobilelistitemlayoutBinding): RecyclerView.ViewHolder(binding.root) {
      var mobileicon = binding.mobileicon
      var mobilename = binding.mobilename
      var mobileprice = binding.mobileprice
      var selectborder = binding.selectcard
      var colorlayout = binding.colorlayout
      var colorcard = binding.colorcard
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MobilelistitemlayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun getItemCount(): Int = MobileDataList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(MobileDataList[position].imagePath).placeholder(R.drawable.samsung).error(R.drawable.samsung).into(holder.mobileicon)
        holder.mobilename.setText(MobileDataList[position].brandName+" "+MobileDataList[position].modelName)
        holder.mobileprice.setText("₹ "+MobileDataList[position].mrpPrice+" ("+ MobileDataList[position].variantName +")")
        val safeColor = try {
            Color.parseColor(MobileDataList[position].avlbColors.lowercase())
        } catch (e: IllegalArgumentException) {
            Color.TRANSPARENT
        }
        holder.colorcard.setCardBackgroundColor(safeColor)

         /* holder.colorlayout.removeAllViews()

        val subLayout = LinearLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(8, 4, 8, 4)
            }
            orientation = LinearLayout.HORIZONTAL
        }

        val cardView = CardView(context).apply { layoutParams = LinearLayout.LayoutParams(context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._10sdp),
            context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._10sdp)).apply { setMargins(8, 0, 0, 2) }
            radius = context.resources.getDimension(com.intuit.sdp.R.dimen._20sdp)
            val safeColor = try {
                Color.parseColor(MobileDataList[position].avlbColors.lowercase())
            } catch (e: IllegalArgumentException) {
                Color.TRANSPARENT
            }

            setCardBackgroundColor(safeColor)
        }

        subLayout.addView(cardView)
        holder.colorlayout.addView(subLayout)*/


        /* MobileDataList[position].colorgradientList.forEach { cardItem ->
             val context = holder.itemView.context

             val subLayout = LinearLayout(context).apply {
                 layoutParams = LinearLayout.LayoutParams(
                     LinearLayout.LayoutParams.WRAP_CONTENT,
                     LinearLayout.LayoutParams.WRAP_CONTENT
                 ).apply {
                     setMargins(8, 4, 8, 4)
                 }
                 orientation = LinearLayout.HORIZONTAL
             }

             val cardView = CardView(context).apply {
                 layoutParams = LinearLayout.LayoutParams(
                     context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._10sdp),
                     context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._10sdp)
                 ).apply {
                     setMargins(8, 0, 0, 0)
                 }
                 radius = context.resources.getDimension(com.intuit.sdp.R.dimen._20sdp)
                 setCardBackgroundColor(cardItem)
             }

             subLayout.addView(cardView)
             holder.colorlayout.addView(subLayout)
         }*/

        if(selectPosition==position){
            holder.selectborder.setBackgroundResource(R.drawable.bg_black_border)
        }
        else{
            holder.selectborder.background = null
        }

        holder.itemView.setOnClickListener{
            selectPosition = position
            notifyDataSetChanged()
           // context.startActivity(Intent(context,EMICalculationPage::class.java))
            MobileData=MobileDataList[position]
            context.startActivity(Intent(context, EMICalculationDetailsPage::class.java))
        }

    }

}