package com.example.property_management

import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

data class PropertyData(
    var imgUrl: String="" ,
    var propertyName:String = "",
    var units:String =""
)

fun main(){

}
val propertyList: ArrayList<PropertyData> = arrayListOf()
/*fun readFromFirestore(): ArrayList<PropertyData> {
    val db = Firebase.firestore
    val auth = Firebase.auth

    val userid = auth.currentUser?.uid

    /*db.collection("Owners").document(userid!!).collection("Properties").get()
        .addOnCompleteListener{
                result->
            for(document in result.getResult()){
                //Log.d("Test","${document.id} => ${document.data}")
                propertyList.add(
                    document.toObject<PropertyData>()
                )
            }
            Log.d("Test","${propertyList.size}")
        }
        .addOnFailureListener{exception->
            Log.d("Test","Error getting documents",exception)
        }*/
    db.collection("Owners").document(userid!!).collection("properties")
        .addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.d("Test", "Listen Failed", e)
            }
            if (snapshot != null) {
                for (document in snapshot) {
                    propertyList.add(
                        document.toObject<PropertyData>()
                    )
                }
            } else {
                Log.d("Test", "document null")
            }
        }
    return propertyList
}*/


    /* propertyList.add(
        PropertyData(
            imgUrl = "image.png",
            propertyName = "Property Name 1",
            units = "units"
        )
    )

    propertyList.add(
        PropertyData(
            imgUrl = "image2.png",
            propertyName = "Property Name 2",
            units = "units of 2"
        )
    )

    propertyList.add(
        PropertyData(
            imgUrl = "image3.png",
            propertyName = "Property Name 3",
            units = "units of 3"
        )
    )

    propertyList.add(
        PropertyData(
            imgUrl = "image4.png",
            propertyName = "Property Name 4",
            units = "units of 4"
        )
    )

    propertyList.add(
        PropertyData(
            imgUrl = "image5.png",
            propertyName = "Property Name 5",
            units = "units of 5"
        )
    )

    propertyList.add(
        PropertyData(
            imgUrl = "image6.png",
            propertyName = "Property Name 6",
            units = "units of 6"
        )
    )*/







