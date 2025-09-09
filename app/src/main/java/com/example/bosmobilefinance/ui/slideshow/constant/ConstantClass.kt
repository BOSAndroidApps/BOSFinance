package com.example.bosmobilefinance.ui.slideshow.constant

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaScannerConnection
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Base64
import android.util.Patterns
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.bosmobilefinance.R
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.Response
import org.json.JSONArray
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object ConstantClass {
     //const val BASE_URL = "https://oqapi.bos.center/"

     const val BASE_URL = "https://api.oqpay.in/"
     const val SMS_BASE_URL = "http://web.adcruxmedia.in/"
     const val PAN_BASE_URL = "https://api.aopay.in/"



     const val SMS_API_KEY = "KBSxc26XqjoiR7SA"
     const val SMS_SENDER_ID = "BOSCNT"
     const val SMS_TEMPLATE_ID = "1207175396979758678"
     const val PAN_VERIFICATION_REGISTRATION_ID = "AOP-554"

     const val CustomerCode = "customerCode"
     const val RetailerCode = "retailerCode"
     const val CustomerMobileNumber = "mobileNumber"
     const val CustomerEmailID = "emailID"
     const val FirstName = "firstName"
     const val LastName = "lastName"
     const val LoggedIn = "logged"
     const val Retailer = "Retailer"
     const val Customer = "Customer"
     const val LoginType = "Logintype"
     const val online = "Online"
     const val offline = "Offline"

     lateinit var  dialog : Dialog
     var PanNumber : String = ""
     var CheckOnlineOrOffline : String = ""
     var PanNumberVerified : String = "yes"
     var BrandName : String = ""
     var ModelName : String = ""
     var ModelVarient : String = ""
     var ModelColor : String = ""
     var SellingPrice : String = ""
     var MRPPrice : String = ""
     var DownPayment : String = ""
     var Tenure : String = ""
     var LoanAmount : Double = 0.0
     var EmiAmount : String = ""
     var InterestRate : String = ""
     var ToBePaidAmount : String = ""
     var CountryName : String ? = ""
     var ClickOnCardDashboard : String ? = ""
     var AadharNumber : String = ""
     var AadharFrontImageUri: Uri? = null
     var AadharBackImageUri: Uri? = null
     var PanFrontImageUri: Uri? = null
     var AadharVerified : String = ""
     var CustPhotoPath : Uri? = null
     var CustFirstName : String = ""
     var CustMiddleName : String = ""
     var CustLastName : String = ""
     var CustPrimaryMobileNumber : String = ""
     var CustPrimaryOTP : String = "1234"
     var CustPrimaryMobileVerified : String = "yes"
     var CustAlternateMobileNumber : String = ""
     var CustAlternateMobileOTP : String = "1234"
     var CustAlternateMobileVerified : String = "yes"
     var isAggrementVerified : String = ""
     var CusteMailID : String = ""
     var CustFlatNo : String = ""
     var CustAreaSector : String = ""
     var CustCurrentAddress : String = ""
     var CustPinCode : String = ""
     var CustCountry : String ? = "India"
     var CustStateName : String = ""
     var CustCityName : String = ""
     var ImeiNumber1 : String = ""
     var ImeiNumber1SealPhotoPath : Uri? = null
     var ImeiNumber2 : String = ""
     var ImeiNumber2SealPhotoPath : Uri? = null
     var Invoive_Path : Uri? = null
     var ImeiNumberPhotoPath : Uri? = null
     var AccountNumber : String = ""
     var BankIFSCCode : String = ""
     var BankName : String = ""
     var AccountType : String = ""
     var BranchName : String = ""
     var RefName : String = ""
     var RefRelationShip : String = ""
     var RefmobileNo : String = ""
     var RefAddress : String = ""
     var DebitOrCreditCard : String = ""
     var UpiMandate : String = "yes"
     var CreatedBy : String = ""
     var loginType : String = ""
     var  iisAggrementVerified : Boolean = false
     var AadharTransactionIdNo : String = ""

     fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }
        else {
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }

    }


    fun OpenPopUpForVeryfyOTP(context: Context){
        dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.loader)

        dialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            statusBarColor = Color.TRANSPARENT
            navigationBarColor = Color.TRANSPARENT
            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
        }

        dialog.setCanceledOnTouchOutside(false)


        dialog.show()

    }


    fun validateLoginInput(mobileOrEmailID: String, context: Context): Boolean {
        val isMobile = mobileOrEmailID.all { it.isDigit() } && mobileOrEmailID.length == 10

        val isEmail = Patterns.EMAIL_ADDRESS.matcher(mobileOrEmailID).matches()

        if (!isMobile && !isEmail) {
            Toast.makeText(context, "Enter a valid 10-digit mobile number or valid email address", Toast.LENGTH_SHORT).show()
            return false
        }


        return true // Input is valid
    }


    fun getCityStateFromPincode(context: Context, pincode: String, callback: (String?, String?, String?) -> Unit) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.postalpincode.in/pincode/$pincode")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                Handler(Looper.getMainLooper()).post {
                    callback(null, null, null)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let { jsonString ->
                    try {
                        val jsonArray = JSONArray(jsonString)
                        val postOfficeArray = jsonArray.getJSONObject(0).getJSONArray("PostOffice")
                        if (postOfficeArray.length() > 0) {
                            val postOffice = postOfficeArray.getJSONObject(0)
                            val city = postOffice.getString("District")
                            val state = postOffice.getString("State")
                            val country = postOffice.getString("Country")

                            Handler(Looper.getMainLooper()).post {
                                callback(city, state, country)
                            }
                        } else {
                            Handler(Looper.getMainLooper()).post {
                                callback(null, null, null)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Handler(Looper.getMainLooper()).post {
                            callback(null, null, null)
                        }
                    }
                }
            }
        })
    }


    fun isValidPinCode(pinCode: String): Boolean {
        return pinCode.matches(Regex("^[1-9][0-9]{5}$"))
    }


    fun uriToBase64(context: Context, uri: Uri): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bytes = inputStream?.readBytes()
            inputStream?.close()
            if (bytes != null) {
                Base64.encodeToString(bytes, Base64.NO_WRAP)
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    fun getBytesFromBitmap(bitmap: Bitmap?): ByteArray? {
        if (bitmap != null) {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream)
            return stream.toByteArray()
        }
        return null
    }



    fun uriToFile(uri: Uri, context: Context): File? {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val tempFile = File.createTempFile("upload_", ".jpg", context.cacheDir)
        tempFile.outputStream().use { fileOut ->
            inputStream.copyTo(fileOut)
        }
        return tempFile
    }


/*    fun saveImageToCache(context: Context, imageUri: Uri): File? {
        return try {
            // Open input stream from the Uri
            val inputStream = context.contentResolver.openInputStream(imageUri) ?: return null

            // Create a temp file in the cache directory
            val fileName = "cache_img_${System.currentTimeMillis()}.jpg"
            val tempFile = File(context.cacheDir, fileName)

            // Write the contents of the URI to the file
            val outputStream = FileOutputStream(tempFile)
            inputStream.copyTo(outputStream)

            // Close streams
            outputStream.flush()
            outputStream.close()
            inputStream.close()

            tempFile
        }
        catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }*/


   /* fun saveImageToCache(context: Context, imageUri: Uri,filename:String): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(imageUri) ?: return null

            val fileName = "$filename${System.currentTimeMillis()}.jpg"
            val tempFile = File(context.cacheDir, fileName)

            // Ensure file is created
            if (!tempFile.exists()) {
                tempFile.createNewFile()
            }

            // Use buffered streams for safe and complete copy
            inputStream.use { input ->
                FileOutputStream(tempFile).use { output ->
                    input.copyTo(output)
                }
            }

            tempFile
            //saveImageToPictures(context,tempFile)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }*/

    fun saveImageToCache(
        context: Context,
        imageUri: Uri,
        filename: String
    ): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(imageUri) ?: return null
            val bitmap = BitmapFactory.decodeStream(inputStream) ?: return null
            inputStream.close()

            val fileName = "$filename${System.currentTimeMillis()}.jpg"
            val tempFile = File(context.cacheDir, fileName)

            FileOutputStream(tempFile).use { output ->
                // Compress to ~70% quality
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, output)
            }

            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }




    fun saveImageToPictures(context: Context, imageFile: File): File? {
        return try {
            val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val newFile = File(picturesDir, imageFile.name)

            imageFile.copyTo(newFile, overwrite = true)

            // Notify media scanner so it shows in Gallery
            MediaScannerConnection.scanFile(context, arrayOf(newFile.absolutePath), null, null)

            newFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }



    fun createMultipartFromUri(
        context: Context,
        uri: Uri?,
        partName: String,
        filename:String
    ): MultipartBody.Part? {
        return uri?.let {
            val cacheFile = saveImageToCache(context, it, filename )
            cacheFile?.let { file ->
                val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                MultipartBody.Part.createFormData(partName, file.name, requestFile)
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateEmiEndDateFromNow(tenureMonths: Int): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val startDate = ZonedDateTime.now(ZoneOffset.UTC)
        val endDate = startDate.plusDays(1).plusMonths(tenureMonths.toLong())
        return endDate.format(formatter)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentStartDate(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val now = ZonedDateTime.now(ZoneOffset.UTC)
        return now.format(formatter)
    }


    fun getAndroidId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDateToDDMMYYYY(inputDate: String): String {
        return try {
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val dateTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LocalDateTime.parse(inputDate, inputFormatter)
            } else {
                TODO("VERSION.SDK_INT < O")
            }
            dateTime.format(outputFormatter)
        } catch (e: Exception) {
            "" // return empty string or handle error as needed
        }
    }

    fun generateEMIOptions(maxEMI: Int): Array<String> {
        return (1..maxEMI).map { it.toString() }.toTypedArray()
    }

    fun getCurrentUtcTimestamp(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(Date())
    }

    fun EditText.disableCopyPaste() {
        this.isLongClickable = false
        this.setTextIsSelectable(false)
        this.customInsertionActionModeCallback = object : ActionMode.Callback {
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?) = false
            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?) = false
            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?) = false
            override fun onDestroyActionMode(mode: ActionMode?) {}
        }
        this.customSelectionActionModeCallback = this.customInsertionActionModeCallback
    }


    fun formatDateToFullMonth(dateString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            outputFormat.format(date ?: return "")
        } catch (e: Exception) {
            ""
        }
    }



}