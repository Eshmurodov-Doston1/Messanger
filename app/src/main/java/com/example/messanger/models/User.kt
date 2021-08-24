package com.example.messanger.models

import java.io.Serializable

class User:Serializable {
    var name:String?=null
    var surname:String?=null
    var userInfo:String?=null
    var phoneNumber:String?=null
    var userImage:String?=null
    var email:String?=null
    var uid:String?=null


    constructor(
        name: String?,
        surname: String?,
        userInfo: String?,
        phoneNumber: String?,
        userImage: String?,
        email: String?,
        uid: String?
    ) {
        this.name = name
        this.surname = surname
        this.userInfo = userInfo
        this.phoneNumber = phoneNumber
        this.userImage = userImage
        this.email = email
        this.uid = uid
    }

    constructor()
}