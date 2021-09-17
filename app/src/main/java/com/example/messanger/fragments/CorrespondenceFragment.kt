package com.example.messanger.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.example.messanger.R
import com.example.messanger.databinding.FragmentCorrespondenceBinding
import com.example.messanger.models.User
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions
import android.provider.MediaStore

import android.app.AlertDialog
import android.content.SharedPreferences
import android.net.Uri
import android.text.method.ScrollingMovementMethod
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.messanger.adapters.ChatAdapter
import com.example.messanger.adapters.GalleryAdapter
import com.example.messanger.databinding.BottomsheetDialogBinding
import com.example.messanger.models.Image
import com.example.messanger.models.Message
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.google.firebase.database.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import com.example.messanger.models.SaveImage
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import lv.chi.photopicker.ChiliPhotoPicker
import lv.chi.photopicker.PhotoPickerFragment


class CorrespondenceFragment : Fragment(R.layout.fragment_correspondence),PhotoPickerFragment.Callback {
    private val binding by viewBinding(FragmentCorrespondenceBinding::bind)
    lateinit var emojiActions:EmojIconActions
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var referenceMessage: DatabaseReference
    lateinit var firebaseStorage: FirebaseStorage
    lateinit var reference:StorageReference
    lateinit var chatAdapter: ChatAdapter
    lateinit var listMessage:ArrayList<Message>
    private  val TAG = "CorrespondenceFragment"
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var sharedPreferences: SharedPreferences
    lateinit var galleryAdapter:GalleryAdapter
    lateinit var listChecked:ArrayList<SaveImage>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseStorage = FirebaseStorage.getInstance()
        reference = firebaseStorage.getReference("SendImages")
        binding.apply {
            sharedPreferences = requireActivity().getSharedPreferences("send",0)

            emojiActions = EmojIconActions(requireContext(), binding.root, emojiconEditText, emojiBtn)
            val user = arguments?.getSerializable("user") as User
            Picasso.get().load(user.accountImageUrl).into(imageUser)
            listMessage = ArrayList()
            firebaseAuth = FirebaseAuth.getInstance()
            firebaseDatabase = FirebaseDatabase.getInstance()
            referenceMessage = firebaseDatabase.getReference("Message")
            name.text = "${user.firstName} ${user.lastName}"
            emojiBtn.setOnClickListener {
                emojiActions.ShowEmojIcon()
            }

            file.setOnClickListener {
                listChecked = ArrayList()
                askPermission(android.Manifest.permission.CAMERA){
                    ScrollingMovementMethod()
                    openPicker()
                }.onDeclined { e ->
                    if (e.hasDenied()) {
                        AlertDialog.Builder(requireContext())
                            .setMessage("Please accept our permissions")
                            .setPositiveButton("yes") { dialog, which ->
                                e.askAgain();
                            } //ask again
                            .setNegativeButton("no") { dialog, which ->
                                dialog.dismiss();
                            }.show();
                    }

                    if(e.hasForeverDenied()) {
                        // you need to open setting manually if you really need it
                        e.goToSettings();
                    }
                }
            }
            audio.setOnClickListener {

            }


            send.setOnClickListener {
                val message = emojiconEditText.text.toString().trim()
                if (message.isNotEmpty()) {
                    val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(
                        Date()
                    )
                    var key = referenceMessage.push().key
                    var message = Message(message, simpleDateFormat.toString(), firebaseAuth.currentUser?.uid, user.uid, key)
                    referenceMessage.child("${firebaseAuth.currentUser?.uid}/${user.uid}/$key")
                        .setValue(message)
                    referenceMessage.child("${user.uid}/${firebaseAuth.currentUser?.uid}/$key")
                        .setValue(message)
                    emojiconEditText.text.clear()
                }
            }

            referenceMessage.child("${firebaseAuth.currentUser?.uid}/${user.uid}").addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val children = snapshot.children
                    listMessage.clear()
                    for (i in children){
                        listMessage.add(i.getValue(Message::class.java)!!)

                    }
                    chatAdapter = ChatAdapter(listMessage, firebaseAuth.currentUser?.uid.toString())
                    chatRv.adapter = chatAdapter
                    chatAdapter.notifyDataSetChanged()
                    chatRv.smoothScrollToPosition(chatAdapter.itemCount)
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

            emojiconEditText.addTextChangedListener {
                if (it.toString().trim().isNotEmpty()) {
                    send.visibility = View.VISIBLE
                    file.visibility = View.GONE
                    audio.visibility = View.GONE
                } else {
                    send.visibility = View.INVISIBLE
                    file.visibility = View.VISIBLE
                    audio.visibility = View.VISIBLE
                }
            }
            emojiActions.setUseSystemEmoji(true)
        }
    }
    private fun openPicker() {
        PhotoPickerFragment.newInstance(
            multiple = true,
            allowCamera = false,
            maxSelection = 5,
            theme = R.style.ChiliPhotoPicker_Light
        ).show(childFragmentManager, "picker")


    }


    override fun onImagesPicked(photos: ArrayList<Uri>) {
        Toast.makeText(requireContext(), photos.joinToString(separator = "\n") { it.toString() }, Toast.LENGTH_SHORT).show()
    }


}