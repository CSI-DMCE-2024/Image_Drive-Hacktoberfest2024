package com.example.material.main

import android.content.ContentValues
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.viewpager2.widget.ViewPager2
import com.example.material.R
import com.example.material.adapters.tabsadapter
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
lateinit var Myadapter: tabsadapter

class MainActivity3 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        val sharedPreferences= getSharedPreferences("counter", MODE_PRIVATE)
        val uid=sharedPreferences.getString("uid",null).toString()

        val a=ColorDrawable(getColor(R.color.grey))
        supportActionBar?.setBackgroundDrawable(a)
        var database=Firebase.database
        val myRef = database.getReference("EMAIL ID").child(uid)
        // Read from the database
        myRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val name = snapshot.child("NAME").value.toString()
                val email = snapshot.child("EMAIL ID").value.toString()
                //view.text = name + ":" + email
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })

        //tabs
        var tabl=findViewById<TabLayout>(R.id.tabs)
        var viewp=findViewById<ViewPager2>(R.id.viewp)
        Myadapter = tabsadapter(supportFragmentManager,lifecycle)
        viewp.adapter= Myadapter

        tabl.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    viewp.currentItem=tab.position
                }

            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })

        viewp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabl.selectTab(tabl.getTabAt(position))
            }

        })


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val a=menuInflater
        a.inflate(R.menu.menu,menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val sharedPreferences= getSharedPreferences("counter", MODE_PRIVATE)
        if (item.toString()=="Logout"){

            sharedPreferences.edit().apply{
                putBoolean("flag",false)
            }.apply()
            startActivity(Intent(this, MainActivity2::class.java))
        }
        else{
            Log.w(ContentValues.TAG, "potty")
        }

    return true
    }
}
