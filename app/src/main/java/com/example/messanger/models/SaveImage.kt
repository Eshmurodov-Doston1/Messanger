package com.example.messanger.models

class SaveImage {
  var imageUri:String?=null
    var isChecked:Boolean?=null

    constructor(imageUri: String?, isChecked: Boolean?) {
        this.imageUri = imageUri
        this.isChecked = isChecked
    }
}