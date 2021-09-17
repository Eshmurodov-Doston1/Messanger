package com.example.messanger

import android.Manifest
import android.app.AlertDialog
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.example.messanger.databinding.ActivityUserBinding
import com.example.messanger.databinding.ImageDialogBinding
import com.example.messanger.models.User
import com.github.florent37.runtimepermission.kotlin.askPermission

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.kaopiz.kprogresshud.KProgressHUD
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class UserActivity : AppCompatActivity() {
    lateinit var activityUserBinding:ActivityUserBinding
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseFireStore:FirebaseFirestore
    lateinit var firebaseDatabase:FirebaseDatabase
    lateinit var firebaseStorage: FirebaseStorage
    lateinit var reference:StorageReference
    lateinit var photoUri:Uri
    var imageUri:String?=null
    lateinit var listUser:ArrayList<User>
    lateinit var referenceDatabase:DatabaseReference
    lateinit var currentPhotoPAth:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityUserBinding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(activityUserBinding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        var imageFile = createImageFile()
        firebaseFireStore = FirebaseFirestore.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()
        photoUri = FileProvider.getUriForFile(this,BuildConfig.APPLICATION_ID,imageFile)
        firebaseDatabase = FirebaseDatabase.getInstance()
        activityUserBinding.clouse.setOnClickListener {
            finish()
        }
        listUser = ArrayList()
        referenceDatabase = firebaseDatabase.getReference("users")
        reference  = firebaseStorage.getReference("images")
        activityUserBinding.imageUser.setOnClickListener {
            askPermission(Manifest.permission.CAMERA){
                var alertDialog = AlertDialog.Builder(this,R.style.BottomSheetDialogThem)
                val create = alertDialog.create()
                var imageDialogBinding = ImageDialogBinding.inflate(LayoutInflater.from(this),null,false)
                create.setView(imageDialogBinding.root)
                imageDialogBinding.camera.setOnClickListener {
                   getCameraImage.launch(photoUri)
                    create.dismiss()
                }
                imageDialogBinding.galary.setOnClickListener {
                    pickImageFromGalleryNew()
                    create.dismiss()
                }
                create.show()
            }.onDeclined { e ->
                if (e.hasDenied()) {
                    AlertDialog.Builder(this)
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
        activityUserBinding.save.setOnClickListener {
            val name = activityUserBinding.name.text.toString().trim()
            val surname = activityUserBinding.surname.text.toString().trim()
            val userInfo = activityUserBinding.userInfo.text.toString().trim()
            val phone = activityUserBinding.phoneInput.rawText.toString().trim()
            var time = SimpleDateFormat("dd:MM.yyyy hh.MM",Locale.getDefault()).format(Date())
            if (name.isNotEmpty() && surname.isNotEmpty() && userInfo.isNotEmpty() && phone.isNotEmpty()){
                if (imageUri!=null){
                    var dialog =  KProgressHUD.create(this)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setLabel("Please wait")
                        .setCancellable(true)
                        .setDimAmount(2.0F)
                        .show();
                    var m = System.currentTimeMillis()
                    var uploadTask = reference.child(m.toString()).putFile(this.photoUri)
                    uploadTask.addOnSuccessListener {
                        val downloadUrl = it.metadata?.reference?.downloadUrl
                        var uid =  firebaseAuth.currentUser?.uid.toString()
                        if (it.task.isSuccessful){
                            downloadUrl?.addOnSuccessListener {
                                dialog.dismiss()
                                var imageUri = it.toString()
                                    var user = User(name,surname,phone,userInfo,name,imageUri,firebaseAuth.currentUser?.email.toString(),uid,time)
                                    referenceDatabase.addValueEventListener(object:ValueEventListener{
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            var listFilter = ArrayList<User>()
                                            val children = snapshot.children
                                            listUser.clear()
                                            for (i in children){
                                                val user = i.getValue(User::class.java)
                                                if (user!=null && user.uid==uid){
                                                    listFilter.add(user)
                                                }
                                                if (user!=null && user.uid!=uid){
                                                    listUser.add(user)
                                                }
                                            }
                                            if(listFilter.isEmpty()){
                                                referenceDatabase.child(uid).setValue(user)
                                                Toast.makeText(this@UserActivity, "Save", Toast.LENGTH_SHORT).show()
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {

                                        }

                                    })
                            }
                        }
                    }
                }else{
                    Toast.makeText(this, "No Image", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    //gallery
    private fun pickImageFromGalleryNew() {
        getImageContent.launch("image/*")
    }

    private val getImageContent = registerForActivityResult(ActivityResultContracts.GetContent()){uri->
        if (uri!=null){
            activityUserBinding.imageUser.setImageURI(uri)
            photoUri = uri
            imageUri= uri.toString()
        }
    }

    //camera
    private val getCameraImage = registerForActivityResult(ActivityResultContracts.TakePicture()){
        if (it){
            if (photoUri!=null){
                activityUserBinding.imageUser.setImageURI(photoUri)
                imageUri= photoUri.toString()
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile():File {
        val simpleDate = SimpleDateFormat("dd.MM.yyyy HH:mm:ss",Locale.getDefault()).format(Date())
        val externalFilesDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_$simpleDate",".jpg",externalFilesDir).apply {
            currentPhotoPAth = absolutePath
        }
    }
}