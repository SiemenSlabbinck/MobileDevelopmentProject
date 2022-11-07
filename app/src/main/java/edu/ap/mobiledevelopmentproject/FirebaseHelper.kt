package edu.ap.mobiledevelopmentproject

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.ArrayList

class FirebaseHelper {
    private val db = Firebase.firestore

    fun add(toilet: Toilet){
        db.collection("toilets")
            .add(toilet)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    fun read(): ArrayList<Toilet> {
        val toiletList = ArrayList<Toilet>()
        db.collection("toilets")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    var toiletInfo = Toilet()
                    toiletInfo.street = document.getString("street")
                    toiletInfo.number = document.getString("number")
                    toiletInfo.postal_code = document.getString("postal_code")?.toInt()
                    toiletInfo.district = document.getString("district")
                    toiletInfo.target_audience = document.getString("target_audience")
                    toiletInfo.wheelchair_accessible = document.getString("wheelchair_accessible")
                    toiletInfo.changing_table = document.getString("changing_table")
                    toiletInfo.x_coord = document.getString("x_coord")?.toDouble()
                    toiletInfo.y_coord = document.getString("y_coord")?.toDouble()
                    toiletList.add(toiletInfo)
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents", exception)
            }
        return toiletList
    }

    fun deleteDocument(doc: String){
        db.collection("toilets").document(doc)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }
}