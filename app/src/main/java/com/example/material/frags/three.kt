package com.example.material.frags

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.material.R
import com.example.material.adapters.Video
import com.example.material.adapters.photos
import com.example.material.adapters.RecycleAdapter
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database

class Three : Fragment() {
    lateinit var myAdapter: RecycleAdapter
    val items = ArrayList<Any>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_three, container, false)

        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        Log.w(TAG, "UID:$uid")
        val database = Firebase.database("https://material-ba9f6-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val myRef = database.getReference("MEDIA").child(uid)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycle)
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        myAdapter = RecycleAdapter(items)
        recyclerView.adapter = myAdapter

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                items.clear()
                for (mediaSnapshot in snapshot.children) {
                    val mediaType = mediaSnapshot.child("type").getValue(String::class.java)
                    val mediaPath = mediaSnapshot.child("path").getValue(String::class.java)
                    val mediaUrl = mediaSnapshot.child("url").getValue(String::class.java)

                    if (mediaType == "video") {
                        items.add(Video(mediaPath!!, mediaUrl!!))
                    } else if (mediaType == "image") {
                        items.add(photos(mediaPath!!, mediaUrl!!))
                    }
                }
                myAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

        return view
    }
}