package com.example.material.adapters


import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.Environment.DIRECTORY_DCIM
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.material.R
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import java.io.File


class recycleadapter(val array: ArrayList<photos>): RecyclerView.Adapter<recycleadapter.viewholder>() {


    class viewholder(view:View): RecyclerView.ViewHolder(view){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.gallery,parent,false)
         return viewholder(view)
    }

    override fun getItemCount(): Int {
        return array.size
    }

      override fun onBindViewHolder(holder: viewholder, position: Int) {
        var photo=array.get(position)
        Picasso.get()
            .load(photo.photourl)
            .resize(500, 900)
           .centerCrop()
            .into( holder.itemView.findViewById<ImageView>(R.id.image))

        val download=holder.itemView.findViewById<Button>(R.id.button)
        download.setOnClickListener {
            var uid= FirebaseAuth.getInstance().currentUser?.uid.toString()

//            val database = Firebase.database("https://material-ba9f6-default-rtdb.asia-southeast1.firebasedatabase.app/")
//            val myRef = database.getReference("PHOTO").child(uid).child(photo.path)

            val myRef = com.google.firebase.ktx.Firebase.storage.reference.child(uid).child("IMAGES")
            val filename=photo.path+"puch.jpg"


            if (Build.VERSION.SDK_INT==Build.VERSION_CODES.Q)
            {
                Toast.makeText(it.context, "SAVED IN APP DATA", Toast.LENGTH_SHORT).show()
                val dir= it.context.getExternalFilesDir(null)
                val file = File(dir, filename)
                Toast.makeText(it.context, photo.path, Toast.LENGTH_SHORT).show()

                myRef.child(photo.path).getFile(file)
                    .addOnSuccessListener(OnSuccessListener<FileDownloadTask.TaskSnapshot?> {
                        Log.e("STORAGE_Q", "local tem file created  created ")
                    })
                    .addOnFailureListener(
                        OnFailureListener { exception ->
                            Log.e(
                                "firebase ",
                                ";local tem file not created  created $exception"
                            )
                        })
            }
            else{

                val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                val file = File(directory, filename)
                Toast.makeText(it.context, photo.path, Toast.LENGTH_SHORT).show()

                myRef.child(photo.path).getFile(file)
                    .addOnSuccessListener(OnSuccessListener<FileDownloadTask.TaskSnapshot?> {
                        Log.e("STORAGE", " local tem file created  created ")
                    })
                    .addOnFailureListener(
                        OnFailureListener { exception ->
                            Log.e(
                                "firebase ",
                                ";local tem file not created  created $exception"
                            )
                        })
            }
        }


          val delete=holder.itemView.findViewById<ImageView>(R.id.delete)
          delete.setOnClickListener{


              var uid= FirebaseAuth.getInstance().currentUser?.uid.toString()
              val name=photo.path

              //deleting from database
              var realtime =Firebase.database("https://material-ba9f6-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("PHOTO").child(uid).child(name)
              realtime.removeValue().addOnSuccessListener {

                  //deleting from storage
                  var myRef = com.google.firebase.ktx.Firebase.storage.reference.child(uid).child(name)
                  myRef.delete().addOnSuccessListener {

                      Log.e(
                          "Delete ",
                          "vachla FIREBASE"
                      )
                  }
              }.addOnFailureListener {
                 // Toast.makeText(it.context, "DELETED", Toast.LENGTH_SHORT).show()
                  Log.e(
                      "Delete ",
                      "HAGLA FIREBASE"
                  )


              }









          }
    }
}

