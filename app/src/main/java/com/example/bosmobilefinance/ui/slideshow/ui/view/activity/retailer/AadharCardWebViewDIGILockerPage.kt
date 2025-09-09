package com.example.bosmobilefinance.ui.slideshow.ui.view.activity.retailer

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bosmobilefinance.databinding.ActivityAadharCardWebViewDigilockerPageBinding
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.AadharBackImageUri
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.AadharFrontImageUri
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.AadharNumber
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.AadharTransactionIdNo

class AadharCardWebViewDIGILockerPage : AppCompatActivity() {

  lateinit  var binding : ActivityAadharCardWebViewDigilockerPageBinding


   companion object{
       var digilockerLink : String = ""
       var VerifiedID: String = ""
   }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

         binding = ActivityAadharCardWebViewDigilockerPageBinding.inflate(layoutInflater)
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

        setDataInWebView()

    }

    fun setDataInWebView(){
        binding.webview.settings.javaScriptEnabled = true
        binding.webview.addJavascriptInterface(WebAppInterface(this), "AndroidInterface")
        binding.webview.webViewClient = WebViewClient()
        binding.webview.loadUrl(digilockerLink)

        binding.webview.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                val jsCode = """
            (function() {
                try {
                    var text = document.getElementById("transctionID").innerText;
                    var id = text.split(":")[1].trim(); // Extract only the ID
                    AndroidInterface.receiveTransactionId(id);
                } catch (e) {
                    AndroidInterface.receiveTransactionId("Error: " + e.message);
                }
            })();
        """.trimIndent()

                binding.webview.evaluateJavascript(jsCode, null)
            }
        }


    }


    override fun onBackPressed() {
        if (binding.webview.canGoBack()) {
            binding.webview.goBack()
        } else {
            if(VerifiedID.startsWith("Error")){
                ConstantClass.AadharVerified = "no"
                super.onBackPressed()
            }
        }
    }


    class WebAppInterface(private val context: Context) {

        @JavascriptInterface
        fun receiveTransactionId(transactionId: String) {
            Log.d("WebViewData", "Transaction ID: $transactionId")
            VerifiedID =  transactionId
            if (!transactionId.isBlank() && !transactionId.startsWith("Error")) {
                if(context is Activity){
                    if(transactionId.equals(AadharTransactionIdNo)){
                        ConstantClass.AadharVerified = "yes"
                        AadharFrontImageUri = null
                        AadharBackImageUri = null
                        AadharNumber = ""
                        val intent = Intent(context, NewCustomerRegistrationPage::class.java)
                        context.startActivity(intent)
                        context.finish()
                    }else{
                        Toast.makeText(context, "Aadhar not verified", Toast.LENGTH_SHORT).show()
                        ConstantClass.AadharVerified = ""
                        context.finish()
                    }

                }
            } else {

            }

        }

    }




}