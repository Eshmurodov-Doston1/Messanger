package com.example.messanger.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentContainer
import com.example.messanger.R
import com.example.messanger.databinding.FragmentCorrespondenceBinding
import com.example.messanger.models.User
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions
import android.provider.MediaStore

import android.provider.MediaStore.MediaColumns

import android.app.Activity
import android.app.AlertDialog
import android.content.SharedPreferences
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.messanger.App
import com.example.messanger.adapters.ChatAdapter
import com.example.messanger.adapters.GalleryAdapter
import com.example.messanger.databinding.BottomsheetDialogBinding
import com.example.messanger.databinding.DialogImageBinding
import com.example.messanger.models.Image
import com.example.messanger.models.Message
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.github.florent37.runtimepermission.kotlin.coroutines.experimental.askPermission
import com.google.firebase.database.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest
import kotlin.collections.ArrayList
import android.graphics.LightingColorFilter





class CorrespondenceFragment : Fragment(R.layout.fragment_correspondence) {
    private val binding by viewBinding(FragmentCorrespondenceBinding::bind)
    lateinit var emojiActions:EmojIconActions
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var referenceMessage: DatabaseReference
    lateinit var chatAdapter: ChatAdapter
    lateinit var listMessage:ArrayList<Message>
    private  val TAG = "CorrespondenceFragment"
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var sharedPreferences: SharedPreferences
    lateinit var galleryAdapter:GalleryAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            sharedPreferences = requireActivity().getSharedPreferences("send",0)
            sharedPreferences.getInt("position",-1)
            emojiActions = EmojIconActions(requireContext(), binding.root, emojiconEditText, emojiBtn)
            val user = arguments?.getSerializable("user") as User
            Picasso.get().load(user.userImage).into(imageUser)
            listMessage = ArrayList()
            firebaseAuth = FirebaseAuth.getInstance()
            firebaseDatabase = FirebaseDatabase.getInstance()
            referenceMessage = firebaseDatabase.getReference("Message")
            galleryAdapter =  GalleryAdapter(object:GalleryAdapter.OnItemClickListener{
                override fun onItemClick(image: Image, position: Int,isChecked:Boolean) {
                 var alertDialog = AlertDialog.Builder(requireContext(),R.style.BottomSheetDialogThem)
                    val create = alertDialog.create()
                    var dialogImageBinding = DialogImageBinding.inflate(LayoutInflater.from(root.context),null,false)
                    create.setView(dialogImageBinding.root)


                    Glide.with(root.context).load(image.image).apply(RequestOptions().centerCrop()).into(dialogImageBinding.imagePhone)
                    val position1 = sharedPreferences.getInt("position", -1)
                    if (position1==position && !isChecked){
                        dialogImageBinding.animatedcheckbox.setChecked(false)
                        dialogImageBinding.sendImage.visibility = View.INVISIBLE
                    }else if(position1==position && isChecked){
                        dialogImageBinding.animatedcheckbox.setChecked(true)
                        dialogImageBinding.sendImage.visibility = View.VISIBLE
                    }else{
                        dialogImageBinding.sendImage.visibility = View.INVISIBLE
                    }


                    dialogImageBinding.animatedcheckbox.setOnChangeListener {
                        if (it){
                            val edit = sharedPreferences.edit()
                            edit.putInt("position",position)
                            edit.apply()
                            galleryAdapter.notifyItemChanged(position)
                            dialogImageBinding.sendImage.visibility = View.VISIBLE
                        }else{
                            dialogImageBinding.sendImage.visibility = View.INVISIBLE
                        }
                    }
                    create.show()
                }
                override fun onCheckClick(image: Image, position: Int,isChecked:Boolean) {

                }
            },requireContext(),requireActivity())

            name.text = "${user.name} ${user.surname}"
            emojiBtn.setOnClickListener {
                emojiActions.ShowEmojIcon()
            }
            file.setOnClickListener {
                askPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE){
                    var bottomSheetDialog = BottomSheetDialog(requireContext(),R.style.BottomSheetDialogThem)
                    var bottomSheetDialogBinding = BottomsheetDialogBinding.inflate(LayoutInflater.from(root.context),null,false)
                    bottomSheetDialog.setContentView(bottomSheetDialogBinding.root)

                    galleryAdapter.submitList(getAllImages())
                    bottomSheetDialogBinding.rvGalery.adapter = galleryAdapter
                    bottomSheetDialog.show()
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
                    var message = Message(
                        message,
                        simpleDateFormat.toString(), firebaseAuth.currentUser?.uid, user.uid, key
                    )
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

//    private fun getAllShownImagesPath(activity: Activity): ArrayList<String?>? {
//        val cursor: Cursor?
//        val listOfAllImages = ArrayList<String?>()
//        var absolutePathOfImage: String? = null
//        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//        val projection = arrayOf(
//            MediaColumns.DATA,
//            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
//        )
//        cursor = activity.contentResolver.query(
//            uri, projection, null,
//            null, null
//        )
//        val column_index_data: Int = cursor?.getColumnIndexOrThrow(MediaColumns.DATA) ?: 0
//        cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)!!
//        while (cursor.moveToNext()) {
//            absolutePathOfImage = cursor.getString(column_index_data)
//            listOfAllImages.add(absolutePathOfImage)
//        }
//        return listOfAllImages
//    }


    fun getAllImages():ArrayList<Image>{
        var images = ArrayList<Image>()
        var allImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        var projection = arrayOf(MediaStore.Images.ImageColumns.DATA,MediaStore.Images.Media.DISPLAY_NAME)

        var cursor = requireActivity().contentResolver.query(allImageUri,projection,null,null,null)
        try {
            cursor?.moveToFirst()
            do {
                var image = Image()
                image.image =cursor?.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                image.name =cursor?.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                images.add(image)
            }while (cursor!!.moveToNext())
            cursor.close()
        }catch (e:Exception){
            e.printStackTrace()
        }


        return images
    }



}