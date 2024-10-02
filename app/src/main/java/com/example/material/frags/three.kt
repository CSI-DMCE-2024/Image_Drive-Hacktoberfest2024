package com.example.material.frags

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.material.R
import com.example.material.adapters.photos
import com.example.material.adapters.recycleadapter
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database


class three : Fragment() {
    lateinit var  Myadapter: recycleadapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_three, container, false)
        var photus= ArrayList<photos>()

        var uid= FirebaseAuth.getInstance().currentUser?.uid.toString()
        Log.w(TAG, "UID:$uid")
        val database = Firebase.database("https://material-ba9f6-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val myRef = database.getReference("PHOTO").child(uid)

        val recylce=view.findViewById<RecyclerView>(R.id.recycle)
        val layoutManager= LinearLayoutManager(context)
        recylce.layoutManager=layoutManager

        Myadapter=recycleadapter(photus)
        recylce.adapter=Myadapter

        myRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated
                photus.clear()
                for (snapshot in snapshot.children) {
                    val key=snapshot.key.toString()
                    val value = snapshot.value.toString()
                    Log.w(TAG, "VAL:"+value)
                    photus.add(photos(key,value))
                }
                Myadapter.notifyDataSetChanged()
                System.out.println("Hello$photus")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
        return view
    }
}

