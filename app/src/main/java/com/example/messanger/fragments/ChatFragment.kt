package com.example.messanger.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentContainer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.messanger.R
import com.example.messanger.adapters.AdapterUser
import com.example.messanger.databinding.FragmentChatBinding
import com.example.messanger.models.User
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatFragment : Fragment() {
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
    lateinit var fragmentChatBinding:FragmentChatBinding
    lateinit var root:View
    lateinit var listUser:ArrayList<User>
    lateinit var adapterUser:AdapterUser
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var reference: DatabaseReference
    lateinit var firebaseAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       fragmentChatBinding = FragmentChatBinding.inflate(inflater,container,false)
        root  =fragmentChatBinding.root
        listUser = ArrayList()
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("users")

        adapterUser = AdapterUser(object:AdapterUser.OnItemClickListener{
            override fun itemClick(user: User, position: Int) {
                var bundle = Bundle()
                bundle.putSerializable("user",user)
                var navOptions = NavOptions.Builder()
                navOptions.setEnterAnim(R.anim.enter)
                navOptions.setExitAnim(R.anim.exit)
                navOptions.setPopEnterAnim(R.anim.pop_enter)
                navOptions.setPopExitAnim(R.anim.pop_exit)
                findNavController().navigate(R.id.correspondenceFragment,bundle,navOptions.build())
            }
        })
        reference.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot.children
                listUser.clear()
                for (i in children){
                    val value = i.getValue(User::class.java)
                    var listFilter = ArrayList<User>()
                    if (value!=null && value?.uid?.lowercase(Locale.getDefault()) !=firebaseAuth.uid?.lowercase(Locale.getDefault())){
                        listUser.add(value)
                    }
                    if (value!=null && value?.uid?.lowercase(Locale.getDefault()) ==firebaseAuth.uid?.lowercase(Locale.getDefault())){
                        listFilter.add(value)
                    }
                }
                adapterUser.submitList(listUser)
                fragmentChatBinding.rvUsers.adapter = adapterUser
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        fragmentChatBinding.addContact.setOnClickListener {
            var bundle= Bundle()
            var navOptions = NavOptions.Builder()
            navOptions.setEnterAnim(R.anim.enter)
            navOptions.setExitAnim(R.anim.exit)
            navOptions.setPopEnterAnim(R.anim.pop_enter)
            navOptions.setPopExitAnim(R.anim.pop_exit)
            findNavController().navigate(R.id.contaktsFragment,bundle,navOptions.build())
        }
        return root
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChatFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChatFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}