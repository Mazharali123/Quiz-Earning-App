package com.example.quizapplication.model

class historyModel {


    var timeAndDate : String = ""
    var coin :String = ""
    var isWithDrawal :Boolean = false

   constructor()

    constructor(timeAndDate: String, coin: String, isWithDrawal: Boolean) {
        this.timeAndDate = timeAndDate
        this.coin = coin
        this.isWithDrawal = isWithDrawal
    }
}
