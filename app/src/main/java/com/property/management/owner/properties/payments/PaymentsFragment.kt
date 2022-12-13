package com.property.management.owner.properties.payments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.property.management.databinding.FragmentPaymentsBinding
import com.property.management.owner.properties.UnitAdapter
import java.text.SimpleDateFormat
import java.util.Date


class PaymentsFragment : Fragment() {
    private var _binding: FragmentPaymentsBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore
    private var tenantId = ""
    private lateinit var paymentList:ArrayList<PaymentData>
    private lateinit var pRecyclerView: RecyclerView
    private lateinit var paymentAdapter : PaymentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPaymentsBinding.inflate(inflater,container,false)
        val root:View = binding.root

        paymentList = arrayListOf()
        val bundle = Bundle(this.arguments)
        if(bundle!=null)
        {
            tenantId = bundle.get("tenantId").toString()
        }
        readPaymentsFromFirebase()



        return root
    }

    private fun readPaymentsFromFirebase() {
        Log.d("Test","tenantId $tenantId")
        db.collection("Payments").whereEqualTo("tenantId",tenantId).get()
            .addOnCompleteListener {documents ->
                for(document in documents.result)
                {
                    val m = document.getData()
                    val time = m!!.get("transactionDate") as Timestamp
                    val s = Date(time.toDate().toString())

                    val sdf = SimpleDateFormat("MM/dd/yyyy")

                    paymentList.add(PaymentData(
                        amount = m!!.get("amountPaid").toString().toInt(),
                        datePaid = sdf.format(s),
                        paymentForMonth = m.get("paymentForMonth").toString())
                    )
                }
                pRecyclerView = binding.recyclerViewPayments
                pRecyclerView.layoutManager = LinearLayoutManager(context)
                paymentAdapter = PaymentAdapter(paymentList,this)
                pRecyclerView.adapter = paymentAdapter
            }
    }
}


