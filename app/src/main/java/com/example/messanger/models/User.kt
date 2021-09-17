package com.example.messanger.models

import android.text.TextUtils
import java.io.Serializable

class User:Serializable {
    var firstName:String?=null
    var lastName:String?=null
    var phoneNumber:String?=null
    var bio:String?=null
    var username:String?=null
    var accountImageUrl:String?=null
    var email:String?=null
    var uid:String?=null
    var lastSeenTimestamp:String?=null

    fun getFulName():String{
        return  if(TextUtils.isEmpty(lastName)){
           "$firstName"
        }else{
            "$firstName $lastName"
        }

    }
    constructor()


    constructor(
        firstName: String?,
        lastName: String?,
        phoneNumber: String?,
        bio: String?,
        username: String?,
        accountImageUrl: String?,
        email: String?,
        uid: String?,
        lastSeenTimestamp: String?
    ) {
        this.firstName = firstName
        this.lastName = lastName
        this.phoneNumber = phoneNumber
        this.bio = bio
        this.username = username
        this.accountImageUrl = accountImageUrl
        this.email = email
        this.uid = uid
        this.lastSeenTimestamp = lastSeenTimestamp
    }

}