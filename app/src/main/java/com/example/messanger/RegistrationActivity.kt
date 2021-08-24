package com.example.messanger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.example.messanger.databinding.ActivityRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import android.R
import android.content.Intent
import android.widget.Toast
import com.example.messanger.models.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient

import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*
import java.lang.Exception
import java.util.*


class RegistrationActivity : AppCompatActivity() {
    lateinit var binding:ActivityRegistrationBinding
    lateinit var firebaseAuth:FirebaseAuth
    var REQUEST_CODE=1
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var reference:DatabaseReference
    lateinit var googleSignInClient:GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("users")

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("66004078767-elrooi8hlvsbmii6o5ahupkmu1rujlvj.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.signIn.setOnClickListener {
            var intent = googleSignInClient.signInIntent
            startActivityForResult(intent,REQUEST_CODE)
        }


//        reference.addValueEventListener(object:ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val children = snapshot.children
//                var isHave=false
//                for (i in children){
//                    val value = i.getValue(User::class.java)
//                    if (value?.uid?.lowercase(Locale.getDefault())==firebaseAuth.currentUser?.uid &&value?.email?.lowercase(Locale.getDefault())==firebaseAuth.currentUser?.email ){
//                        isHave=true
//                        break
//                    }
//                }
//                if (isHave){
//
//                }else{
//                    startActivity(Intent(this@RegistrationActivity,UserActivity::class.java)
//                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK ))
//                    finish()
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//
//        })



        if (firebaseAuth.currentUser!=null){
            startActivity(Intent(this@RegistrationActivity,MainActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK ))
            finish()
        }



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==REQUEST_CODE){
            var signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            if (signInAccountTask.isSuccessful){
                try {
                    var googleSignInAccount= signInAccountTask.getResult(ApiException::class.java)
                    Toast.makeText(this, "Sucess", Toast.LENGTH_SHORT).show()
                    if (googleSignInAccount!=null){
                        var authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.idToken,null)
                        firebaseAuth.signInWithCredential(authCredential)
                            .addOnCompleteListener {
                                if (it.isSuccessful){
                                    var isHave=false
                                    reference.addValueEventListener(object:ValueEventListener{
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            val children = snapshot.children
                                            for (i in children){
                                                val value = i.getValue(User::class.java)
                                                if (value?.email?.lowercase(Locale.getDefault())==firebaseAuth.currentUser?.email?.lowercase(Locale.getDefault())){
                                                    isHave=true
                                                    break
                                                }
                                            }
                                            if (isHave){
                                                startActivity(Intent(this@RegistrationActivity,MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                                            }else{
                                                startActivity(Intent(this@RegistrationActivity,UserActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                                            }
                                            finish()
                                        }
                                        override fun onCancelled(error: DatabaseError) {

                                        }

                                    })
                                }
                            }
                    }
                }catch (e:Exception){

                }
            }
        }
    }
}