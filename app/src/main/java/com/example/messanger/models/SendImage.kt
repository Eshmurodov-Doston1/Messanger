package com.example.messanger.models

class SendImage {
    var image:String?=null
    var time:String?=null
    var name:String?=null

    constructor()

    constructor(image: String?, time: String?, name: String?) {
        this.image = image
        this.time = time
        this.name = name
    }
}