package edu.ap.mobiledevelopmentproject

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class FirebaseHelper(_context: Context) {
    private val db = Firebase.firestore
    private var sqlHelper: SqlHelper? = null
    private var context = _context

    // add toilet to firebase collection
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

    // returns list of toilets from firebase collection
    fun read(){
        val toiletList = ArrayList<Toilet>()
        db.collection("toilets")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val toiletInfo = Toilet()
                    toiletInfo.street = document.getString("street")
                    toiletInfo.number = document.getString("number")
                    toiletInfo.postal_code = document.getLong("postal_code")?.toInt()
                    toiletInfo.district = document.getString("district")
                    toiletInfo.target_audience = document.getString("target_audience")
                    toiletInfo.wheelchair_accessible = document.getString("wheelchair_accessible")
                    toiletInfo.changing_table = document.getString("changing_table")
                    toiletInfo.x_coord = document.getDouble("x_coord")
                    toiletInfo.y_coord = document.getDouble("y_coord")
                    toiletInfo.email = document.getString("email")
                    toiletList.add(toiletInfo)
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
                sqlHelper = SqlHelper(context)
                sqlHelper!!.createSQL(toiletList)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents", exception)
            }
    }

    // deletes document from firebase collection
    fun deleteDocument(doc: String){
        db.collection("toilets").document(doc)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }
}