package com.example.snapchat

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.net.HttpURLConnection
import java.net.URL

class ViewSnapActivity : AppCompatActivity() {

    var messageTextView : TextView? = null
    var snapImageView:ImageView? = null
    val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_snap)

        messageTextView = findViewById(R.id.messageTextView)
        snapImageView = findViewById(R.id.snapImageView)

        messageTextView?.text = intent.getStringExtra("message")

        val task = ImageDownloader()
        val myImage: Bitmap
        try {
            myImage = task.execute(intent.getStringExtra("imageURL")).get()
            snapImageView?.setImageBitmap(myImage)
            Log.i("Image","Done")
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }


    }
    inner class ImageDownloader : AsyncTask<String, Void, Bitmap>() {
         override fun doInBackground(vararg urls: String):Bitmap? {
             try {
                 val url = URL(urls[0])
                 val connection = url.openConnection() as HttpURLConnection
                 connection.connect()
                 val `inputStream` = connection.inputStream
                 return BitmapFactory.decodeStream(`inputStream`)
             } catch (e: Exception) {
                 e.printStackTrace()
                 return null
             }
         }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.currentUser?.uid).child("snaps").child(intent.getStringExtra("snapKey")).removeValue()

        FirebaseStorage.getInstance().getReference().child("images").child(intent.getStringExtra("imageName")).delete()



    }
}
