package com.example.messanger.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.messanger.R
import com.example.messanger.databinding.FragmentAddUserBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class AddUserFragment : Fragment(R.layout.fragment_add_user) {
    private val binding by viewBinding(FragmentAddUserBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

        }
    }
}