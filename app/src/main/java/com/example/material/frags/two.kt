package com.example.material.frags

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.material.R
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream


class two : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_two, container, false)


        val capture=view.findViewById<Button>(R.id.button8)
        val image2=view.findViewById<ImageView>(R.id.image2)


        capture.setOnClickListener {
            if (ContextCompat.checkSelfPermission(it.context,android.Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED)
            {

                val intent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                image.launch(intent)

            }
            else{
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf( android.Manifest.permission.CAMERA),101)
            }


        }


        return view
    }

    private val image=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ it ->
        if (it.resultCode== Activity.RESULT_OK)
        {
            if (it.data!=null)
            {

                Log.e("hariom", "jinklas bhava ")

                val image2= view?.findViewById<ImageView>(R.id.image2)

                val bitmap= it.data!!.extras?.get("data") as Bitmap


                val bitmap2=Bitmap.createScaledBitmap(bitmap,1200,1600,false)

                if (image2 != null) {
                    image2.setImageBitmap(bitmap2)
                }

                val baos= ByteArrayOutputStream()
                bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, baos)

                //accessing uid from shared preferences
                val sharedPreferences=
                    this.activity?.getSharedPreferences("counter", AppCompatActivity.MODE_PRIVATE)
                val uid= sharedPreferences?.getString("uid",null).toString()

//                saving photo in storage
                val storageRef = Firebase.storage.reference.child(uid).child("image"+System.currentTimeMillis())
                storageRef.putBytes(baos.toByteArray()).addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener {

                        Toast.makeText(activity, "IMAGE UPLOADED SUCCESSFULLY", Toast.LENGTH_SHORT).show()
                        Log.e("hariom", "taklas bhava")
                        //saving photo location and owner in database
                        val dd=storageRef.name
                        val database = Firebase.database("https://material-ba9f6-default-rtdb.asia-southeast1.firebasedatabase.app")
                        val myRef = database.getReference("PHOTO").child(uid).child(dd)
                        myRef.setValue(it.toString())

                    }
                    storageRef.putBytes(baos.toByteArray()).addOnFailureListener {
                        Log.e("hariom", "nhi taklas bhava")
                    }
                }



            }
            else{
                Log.e("hariom", "haglas bhava ")
            }

        }
    }


}

