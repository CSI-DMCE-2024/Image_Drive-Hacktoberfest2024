package com.example.material.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.material.frags.one
import com.example.material.frags.three
import com.example.material.frags.two

class tabsadapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
       return 3
    }

    override fun createFragment(position: Int): Fragment {
        if (position==0)
            return one()
        if (position==1)
            return two()
        if (position==2)
            return three()
        else
            return one()

    }

}