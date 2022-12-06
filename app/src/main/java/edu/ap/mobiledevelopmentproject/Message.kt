package edu.ap.mobiledevelopmentproject

import android.content.Context
import android.widget.Toast

class Message {
    companion object {
        fun message(context: Context, msg:String) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }
    }

}