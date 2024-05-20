package com.example.quizapplication.model

class user {

    var name =""
    var age =0
    var email =""
    var password = ""

    constructor() // Default constructor required for Firebase deserialization


    constructor(name: String, age: Int, email: String, password: String) {
        this.name = name
        this.age = age
        this.email = email
        this.password = password
    }
}