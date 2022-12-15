package com.property.management.tenant.update_payment_details_fragment

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.property.management.databinding.FragmentUpdatePaymentDetailsBinding
import java.io.ByteArrayOutputStream
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*

class PaymentDetailsFragment : Fragment() {

    private var _binding: FragmentUpdatePaymentDetailsBinding? = null
    private val TAG = "Property_Management"
    private val db = Firebase.firestore
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var storageRef = Firebase.storage
    private val auth = Firebase.auth
    private var imgURL = ""
    private var formatDate = SimpleDateFormat("MMMM d, yyyy", Locale.US)
    private var formatDateWithMonthYear = SimpleDateFormat("MMMM YYYY", Locale.US)
    private var tenantId = ""
    companion object {
        // Define the pic id
        private const val picID = 123
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val paymentDetailsViewModel =
            ViewModelProvider(this).get(PaymentDetailsViewModel::class.java)

        _binding = FragmentUpdatePaymentDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val md = MessageDigest.getInstance("MD5")
        tenantId = md.digest(auth.currentUser!!.email!!.trim().toByteArray()).toHex()

        binding.editTransactionDate.setOnClickListener(View.OnClickListener {
            val getDate : Calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(requireContext(),android.R.style.Theme_Holo_Light_Dialog_MinWidth,DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->

                val selectDate : Calendar = Calendar.getInstance()
                selectDate.set(Calendar.YEAR,i)
                selectDate.set(Calendar.MONTH,i2)
                selectDate.set(Calendar.DAY_OF_MONTH,i3)
                val date : String = formatDate.format(selectDate.time)
                //Toast.makeText(requireContext(),"Date: "+ date, Toast.LENGTH_SHORT).show()
                val displayDate: TextView = binding.editTransactionDate
                displayDate.text = date

            },getDate.get(Calendar.YEAR),getDate.get(Calendar.MONTH),getDate.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
            datePicker.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        })
        binding.editPaymentForDate.setOnClickListener(View.OnClickListener {
            val getDate : Calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(requireContext(),android.R.style.Theme_Holo_Light_Dialog_MinWidth,DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->

                val selectDate : Calendar = Calendar.getInstance()
                selectDate.set(Calendar.YEAR,i)
                selectDate.set(Calendar.MONTH,i2)
                //selectDate.set(Calendar.DAY_OF_MONTH,i3)
                val date : String = formatDateWithMonthYear.format(selectDate.time)

                val displayDate: TextView = binding.editPaymentForDate
                displayDate.text = date
            },getDate.get(Calendar.YEAR),getDate.get(Calendar.MONTH),getDate.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
            datePicker.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        })
        binding.saveButton.setOnClickListener {
            callPaymentDatabase()
        }

        binding.uploadImageButton.setOnClickListener{
            val camera_intent =
                Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(camera_intent,picID)
        }
        return root
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == picID) {
            val photo = data!!.extras!!["data"] as Bitmap?
            binding.imageViewReceipt.setImageBitmap(photo)
            val bytes = ByteArrayOutputStream()
            photo!!.compress(Bitmap.CompressFormat.JPEG,90, bytes)
            val byteArray = bytes.toByteArray()
            uploadToFirebase(byteArray)
        }
    }

    private fun uploadToFirebase(byteArray: ByteArray) {
        storageRef.getReference("PaymentReceiptImages").child(System.currentTimeMillis().toString())
            .putBytes(byteArray)
            .addOnSuccessListener { task->
                task.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener {
                        imgURL = it.toString()
                    }
            }
    }
    private fun callPaymentDatabase(){
        val transactionDateTimestamp = Timestamp(Date(binding.editTransactionDate.text.toString()))
        val req = hashMapOf(
            "amountPaid" to binding.editAmountPaid.text.toString().toInt(),
            "paid" to false,
            "transactionDate" to transactionDateTimestamp,
            "paymentForMonth" to binding.editPaymentForDate.text.toString(),
            "transactionReceipt" to imgURL,
            "tenantId" to tenantId
        )
        db.collection("Payments").add(req)
            .addOnSuccessListener { document->
                Log.d(TAG,"Record added ")
                val action = PaymentDetailsFragmentDirections.actionPaymentDetailsFragmentToNavigationHome()
                findNavController().navigate(action)
            }
            .addOnFailureListener { exception ->
                Log.d(TAG,"Error in writing document in Firebase",exception)
            }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun ByteArray.toHex() :String = joinToString(separator = "") { byte -> "%02x".format(byte) }
}