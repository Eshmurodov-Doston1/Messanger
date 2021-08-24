package com.example.messanger.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentContainer
import androidx.viewpager2.widget.ViewPager2
import com.example.messanger.R
import com.example.messanger.adapters.ViewPagerAdapter
import com.example.messanger.databinding.FragmentMainBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    lateinit var fragmentMainBinding:FragmentMainBinding
    lateinit var root:View
    lateinit var viewPagerAdapter: ViewPagerAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentMainBinding = FragmentMainBinding.inflate(inflater,container,false)
        root = fragmentMainBinding.root
        loadView()
        fragmentMainBinding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.chat->{
                    fragmentMainBinding.viewpager2.currentItem=0
                }
                R.id.group->{
                    fragmentMainBinding.viewpager2.currentItem=1
                }
                R.id.kanal->{
                    fragmentMainBinding.viewpager2.currentItem=2
                }
                R.id.menu->{
                    fragmentMainBinding.viewpager2.currentItem=3
                }
            }
            true
        }
        return root
    }

    private fun loadView() {
        viewPagerAdapter = ViewPagerAdapter(requireActivity())
        fragmentMainBinding.viewpager2.adapter = viewPagerAdapter
        fragmentMainBinding.viewpager2.registerOnPageChangeCallback(object:ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when(position){
                    0->{
                        fragmentMainBinding.bottomNavigation.menu.findItem(R.id.chat).isChecked=true
                        fragmentMainBinding.bottomNavigation.menu.findItem(R.id.chat).setIcon(R.drawable.message1)
                        fragmentMainBinding.bottomNavigation.menu.findItem(R.id.group).setIcon(R.drawable.group)
                        fragmentMainBinding.bottomNavigation.menu.findItem(R.id.kanal).setIcon(R.drawable.save1)
                        fragmentMainBinding.bottomNavigation.menu.findItem(R.id.menu).setIcon(R.drawable.ic_menu)
                    }
                    1->{
                        fragmentMainBinding.bottomNavigation.menu.findItem(R.id.group).isChecked=true
                        fragmentMainBinding.bottomNavigation.menu.findItem(R.id.chat).setIcon(R.drawable.message2)
                        fragmentMainBinding.bottomNavigation.menu.findItem(R.id.group).setIcon(R.drawable.group2)
                        fragmentMainBinding.bottomNavigation.menu.findItem(R.id.kanal).setIcon(R.drawable.save1)
                        fragmentMainBinding.bottomNavigation.menu.findItem(R.id.menu).setIcon(R.drawable.ic_menu)
                    }
                    2->{
                        fragmentMainBinding.bottomNavigation.menu.findItem(R.id.kanal).isChecked=true
                        fragmentMainBinding.bottomNavigation.menu.findItem(R.id.chat).setIcon(R.drawable.message2)
                        fragmentMainBinding.bottomNavigation.menu.findItem(R.id.group).setIcon(R.drawable.group)
                        fragmentMainBinding.bottomNavigation.menu.findItem(R.id.kanal).setIcon(R.drawable.save2)
                        fragmentMainBinding.bottomNavigation.menu.findItem(R.id.menu).setIcon(R.drawable.ic_menu)
                    }
                    3->{
                        fragmentMainBinding.bottomNavigation.menu.findItem(R.id.menu).isChecked=true
                        fragmentMainBinding.bottomNavigation.menu.findItem(R.id.chat).setIcon(R.drawable.message2)
                        fragmentMainBinding.bottomNavigation.menu.findItem(R.id.group).setIcon(R.drawable.group)
                        fragmentMainBinding.bottomNavigation.menu.findItem(R.id.kanal).setIcon(R.drawable.save1)
                        fragmentMainBinding.bottomNavigation.menu.findItem(R.id.menu).setIcon(R.drawable.ic_menu_2)
                    }
                }
            }
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}