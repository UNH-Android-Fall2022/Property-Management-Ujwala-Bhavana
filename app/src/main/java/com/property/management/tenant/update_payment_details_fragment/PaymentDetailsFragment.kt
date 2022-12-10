package com.property.management.tenant.update_payment_details_fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.property.management.databinding.FragmentUpdatePaymentDetailsBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class PaymentDetailsFragment : Fragment() {

    private var _binding: FragmentUpdatePaymentDetailsBinding? = null
    private val TAG = "Property_Management"
    private val db = Firebase.firestore
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var formatDate = SimpleDateFormat("dd MMMM YYYY", Locale.US)
    private var formatDateWithMonthYear = SimpleDateFormat("MMMM YYYY", Locale.US)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val paymentDetailsViewModel =
            ViewModelProvider(this).get(PaymentDetailsViewModel::class.java)

        _binding = FragmentUpdatePaymentDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.editTransactionDate.setOnClickListener(View.OnClickListener {
            val getDate : Calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(requireContext(),android.R.style.Theme_Holo_Light_Dialog_MinWidth,DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->

                val selectDate : Calendar = Calendar.getInstance()
                selectDate.set(Calendar.YEAR,i)
                selectDate.set(Calendar.MONTH,i2)
                selectDate.set(Calendar.DAY_OF_MONTH,i3)
                val date : String = formatDate.format(selectDate.time)
                Toast.makeText(requireContext(),"Date: "+ date, Toast.LENGTH_SHORT).show()
                val displayDate: TextView = binding.editTransactionDate
                displayDate.text = date

            },getDate.get(Calendar.YEAR),getDate.get(Calendar.MONTH),getDate.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
        })
        binding.editPaymentForDate.setOnClickListener(View.OnClickListener {
            val getDate : Calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(requireContext(),android.R.style.Theme_Holo_Light_Dialog_MinWidth,DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->

                val selectDate : Calendar = Calendar.getInstance()
                selectDate.set(Calendar.YEAR,i)
                selectDate.set(Calendar.MONTH,i2)
                //selectDate.set(Calendar.DAY_OF_MONTH,i3)
                val date : String = formatDateWithMonthYear.format(selectDate.time)
                Toast.makeText(requireContext(),"Date: "+ date, Toast.LENGTH_SHORT).show()
                val displayDate: TextView = binding.editPaymentForDate
                displayDate.text = date
            },getDate.get(Calendar.YEAR),getDate.get(Calendar.MONTH),getDate.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
        })
        binding.saveButton.setOnClickListener {
            callPaymentDatabase()
        }
        return root
    }
    private fun callPaymentDatabase(){

        val req = hashMapOf(
            "amountPaid" to binding.editAmountPaid.text.toString(),
            "paid" to false,
            "transactionDate" to binding.editTransactionDate.text.toString(),
            "paymentForMonth" to binding.editPaymentForDate.text.toString(),
            "transactionReceipt" to ""
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
}