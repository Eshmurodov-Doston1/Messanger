package com.example.messanger.fragments


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import androidx.navigation.fragment.findNavController
import com.example.messanger.R
import com.example.messanger.adapters.AdapterContact
import com.example.messanger.databinding.FragmentContaktsBinding
import com.example.messanger.models.Contact
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import java.util.*
import java.util.jar.Manifest
import kotlin.collections.ArrayList

class ContaktsFragment : Fragment(R.layout.fragment_contakts) {
    lateinit var listContact:ArrayList<Contact>
    lateinit var listContact1:ArrayList<Contact>
    lateinit var adapterContact:AdapterContact
    private  val TAG = "ContaktsFragment"
    private val binding by viewBinding(FragmentContaktsBinding::bind)
    @SuppressLint("Range")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.apply {
            clouse.setOnClickListener {
                findNavController().popBackStack()
            }
            listContact1 = ArrayList()

            search.setOnClickListener {
                clouse.visibility = View.INVISIBLE
                name.visibility = View.INVISIBLE
                search.visibility= View.INVISIBLE
                cons.visibility = View.VISIBLE
            }

            cancle.setOnClickListener {
                val searchText = searchEdt.text.toString().trim()
                if (searchText.isNotEmpty()){
                    searchEdt.text.clear()
                }else{
                    cons.visibility = View.INVISIBLE
                    clouse.visibility = View.VISIBLE
                    name.visibility = View.VISIBLE
                    search.visibility= View.VISIBLE
                }
            }
            searchEdt.addTextChangedListener(object:TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    filters(p0.toString())
                }

            })

            askPermission(android.Manifest.permission.READ_CONTACTS){
               listContact1.clear()
                var cursor = (activity as AppCompatActivity).contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null)
                while (cursor?.moveToNext() == true){
                    var name =  cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)).trim()
                    var  phone_number =cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).trim()
                    val photo_uri = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))
                    var bitmap: Bitmap? = null
                    if (photo_uri != null) {
                        bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, Uri.parse(photo_uri))
                    }
                    var contact = Contact(name, phone_number,bitmap)
                    listContact1.add(contact)
                }
                if (listContact1.isNotEmpty()){
                    textNoInformation.visibility = View.INVISIBLE
                    adapterContact = AdapterContact(object:AdapterContact.OnItemClicListener{
                        override fun onItemClick(contact: Contact, position: Int) {

                        }
                    })
                    adapterContact.submitList(listContact1)
                    rvContact.adapter = adapterContact
                }else if (listContact1.isEmpty()){
                    textNoInformation.visibility = View.VISIBLE
                    rvContact.visibility = View.INVISIBLE
                }
            }.onDeclined { e ->
                if (e.hasDenied()) {
                    AlertDialog.Builder(requireContext())
                        .setMessage("Please accept our permissions")
                        .setPositiveButton("yes") { dialog, which ->
                            e.askAgain()
                        }
                        .setNegativeButton("no") { dialog, which ->
                            dialog.dismiss();
                        }
                        .show();
                }

                if(e.hasForeverDenied()) {
                    e.goToSettings();
                }
            }
        }
    }
    private fun filters(s:String){
        var listFilter:ArrayList<Contact> = ArrayList()
        for (i in listContact1){
            if(i.name?.lowercase(Locale.getDefault())?.contains(s.lowercase(Locale.getDefault())) == true){
                listFilter.add(i)
            }
        }
        adapterContact.filters(listFilter)
    }
}