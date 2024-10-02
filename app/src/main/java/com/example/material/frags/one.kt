package com.example.material.frags

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.material.R
import com.example.material.main.MainActivity3
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class one : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_one, container, false)

        var tv=view.findViewById<Button>(R.id.button3)
        var txt=view.findViewById<TextView>(R.id.textView6)
        // Inflate the layout for this fragment

        tv.setOnClickListener {

            if (ContextCompat.checkSelfPermission(it.context,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)
            {
                val intent=Intent(Intent.ACTION_PICK)
                intent.type="image/*"
                image.launch(intent)
            }
            else{
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ,android.Manifest.permission.READ_EXTERNAL_STORAGE
                        , android.Manifest.permission.READ_MEDIA_IMAGES),100)
            }

        }

        return view


    }
    val image=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ it ->
        if (it.resultCode==Activity.RESULT_OK)
        {
            if (it.data!=null)
            {
                //accessing uid from shared preferences
                val sharedPreferences=
                    this.activity?.getSharedPreferences("counter", AppCompatActivity.MODE_PRIVATE)
                val uid= sharedPreferences?.getString("uid",null).toString()

                //saving photo in storage
                var storageRef = Firebase.storage.reference.child(uid).child("IMAGES").child("image"+System.currentTimeMillis())
                storageRef.putFile(it.data!!.data!!).addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener {

                        Toast.makeText(activity, "IMAGE UPLOADED SUCCESSFULLY", Toast.LENGTH_SHORT).show()

                        //saving photo location and owner in database
                        val dd=storageRef.name
                        Log.d(TAG, dd)
                        val database = Firebase.database("https://material-ba9f6-default-rtdb.asia-southeast1.firebasedatabase.app")
                        val myRef = database.getReference("PHOTO").child(uid).child(dd)
                        myRef.setValue(it.toString())

                    }
                }



            }
        }
    }


}