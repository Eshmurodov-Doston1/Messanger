package com.example.messanger.models

class Message {
    var message:String?=null
    var timeStamp:String?=null
    var idMessage:String?=null
    var recipentId:String?=null
    var key:String?=null
    var readeTimes:String?=null


    constructor()

    constructor(
        message: String?,
        timeStamp: String?,
        idMessage: String?,
        recipentId: String?,
        key: String?
    ) {
        this.message = message
        this.timeStamp = timeStamp
        this.idMessage = idMessage
        this.recipentId = recipentId
        this.key = key
    }


    constructor(
        message: String?,
        timeStamp: String?,
        idMessage: String?,
        recipentId: String?,
        key: String?,
        readeTimes: String?
    ) {
        this.message = message
        this.timeStamp = timeStamp
        this.idMessage = idMessage
        this.recipentId = recipentId
        this.key = key
        this.readeTimes = readeTimes
    }

}