package com.example.messanger.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.messanger.fragments.ChannelFragment
import com.example.messanger.fragments.ChatFragment
import com.example.messanger.fragments.GroupFragment
import com.example.messanger.fragments.MenuFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity):FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
     return when(position){
         0->{
             ChatFragment()
         }
         1->{
             GroupFragment()
         }
         2->{
             ChannelFragment()
         }
         3->{
             MenuFragment()
         }
         else->{
             ChatFragment()
         }
     }
    }

}