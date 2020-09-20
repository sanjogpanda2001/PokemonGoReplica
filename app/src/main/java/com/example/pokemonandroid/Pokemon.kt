package com.example.pokemonandroid

import android.location.Location

class Pokemon {
    var name:String?=null
    var des:String?=null
    var image:Int?=null
    var power:Double?=null
   // var lat:Double?=null
    var location:Location?=null
  //  var log:Double?=null
    var IsCatched:Boolean?=false
    constructor(image:Int,name:String,des:String,power:Double,lat:Double,log:Double){
        this.name=name
        this.des=des
        this.power=power
        this.image=image
       // this.lat=lat
       // this.log=log
        this.location=Location(name)
        this.location!!.latitude=lat
        this.location!!.longitude=log
        this.IsCatched=false
    }
}