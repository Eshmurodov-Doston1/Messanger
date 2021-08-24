package com.example.messanger.models

import android.graphics.Bitmap

class Contact {
    var name:String?=null
    var phoneNumber:String?=null
    var image:Bitmap?=null

    constructor(name: String?, phoneNumber: String?, image: Bitmap?) {
        this.name = name
        this.phoneNumber = phoneNumber
        this.image = image
    }
}