package com.example.theemiclub.ui.slideshow.ui.view.activity.retailer

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bos.payment.appName.network.RetrofitClient
import com.example.theemiclub.R
import com.example.theemiclub.databinding.ActivityMobileselectionBinding
import com.example.theemiclub.ui.slideshow.adapter.MobileListAdapter
import com.example.theemiclub.ui.slideshow.constant.ConstantClass
import com.example.theemiclub.ui.slideshow.data.model.loginsignup.DataItem
import com.example.theemiclub.ui.slideshow.data.repository.AuthRepository
import com.example.theemiclub.ui.slideshow.data.viewModelFactory.CommonViewModelFactory
import com.example.theemiclub.ui.slideshow.localdb.SharedPreference
import com.example.theemiclub.ui.slideshow.ui.view.model.MobileListModel
import com.example.theemiclub.ui.slideshow.ui.viewmodel.AuthenticationViewModel
import com.example.theemiclub.ui.slideshow.utils.ApiStatus
import com.example.theemiclub.ui.slideshow.utils.GridSpacingItemDecoration

class MobileSelectionActivity : AppCompatActivity() {

    lateinit var binding : ActivityMobileselectionBinding
    lateinit var adapter : MobileListAdapter

    lateinit var viewModel: AuthenticationViewModel
    lateinit var preference : SharedPreference

    companion object{
        var MobileList : MutableList<MobileListModel> = mutableListOf()
        var MobileDataList : MutableList<DataItem> = mutableListOf()
        var FilterDataList : MutableList<DataItem> = mutableListOf()

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMobileselectionBinding.inflate(layoutInflater)
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

        val spacingInPixels = 5   // or just use `5` in dp
        binding.showingMobileList.layoutManager = GridLayoutManager(this, 2)
        binding.showingMobileList.addItemDecoration(GridSpacingItemDecoration(2, spacingInPixels, true))
        //addMobileList()
        setClickListner()
        hitApiForGetMobileDataList()
    }


    fun setClickListner(){

        binding.back.setOnClickListener { finish() }

        binding.searchIcon.setOnClickListener {
            binding.searcMobile.requestFocus()
            binding.searcMobile.text.clear()
            // Show the keyboard
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.searcMobile, InputMethodManager.SHOW_IMPLICIT)

        }


        binding.searcMobile.addTextChangedListener(
            object :TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    val search = s.toString().lowercase().trim()
                    val result = MobileDataList.filter {
                        it.brandName.lowercase().contains(search) || it.modelName.lowercase().contains(search)
                    }
                    FilterDataList.clear()
                    FilterDataList.addAll(result)
                    setViewData(FilterDataList)
                    adapter.notifyDataSetChanged()
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            }
        )

    }


    fun setViewData(MobileDataList : MutableList<DataItem>){
        adapter = MobileListAdapter(MobileDataList,this@MobileSelectionActivity)
        binding.showingMobileList.adapter = adapter

    }


    fun hitApiForGetMobileDataList(){
        viewModel.getMobileList().observe(this){ resources->resources.let {
            when(it.apiStatus){
                ApiStatus.SUCCESS -> {
                    it.data?.let { users ->
                        users.body()?.let { response ->
                            ConstantClass.dialog.dismiss()
                            Log.d("MobileRes", response.message)
                            if(response.status.equals("True")){
                                MobileDataList = response.data!!
                                setViewData(MobileDataList)
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

    fun addMobileList() {
        MobileList.clear()

        val colorList1 = arrayListOf(
            ContextCompat.getColor(this, R.color.black),
            ContextCompat.getColor(this, R.color.lightblue),
            ContextCompat.getColor(this, R.color.lightpink)
        )
        MobileList.add(MobileListModel(R.drawable.iphone, "Apple iPhone 16", "₹ 79,000 (128GB)", colorList1))

        val colorList2 = arrayListOf(
            ContextCompat.getColor(this, R.color.black),
            ContextCompat.getColor(this, R.color.skyblue),
            ContextCompat.getColor(this, R.color.darkred)
        )
        MobileList.add(MobileListModel(R.drawable.samsung, "Samsung", "₹ 30,000 (64GB)", colorList2))

        val colorList3 = arrayListOf(
            ContextCompat.getColor(this, R.color.black),
            ContextCompat.getColor(this, R.color.lightgrey),
            ContextCompat.getColor(this, R.color.lightgreen)
        )
        MobileList.add(MobileListModel(R.drawable.oneplus, "OnePlus", "₹ 59,000 (64GB)", colorList3))

        val colorList4 = arrayListOf(
            ContextCompat.getColor(this, R.color.black),
            ContextCompat.getColor(this, R.color.blue),
            ContextCompat.getColor(this, R.color.teal700)
        )
        MobileList.add(MobileListModel(R.drawable.realme, "Realme", "₹ 20,000 (64GB)", colorList4))
    }

}