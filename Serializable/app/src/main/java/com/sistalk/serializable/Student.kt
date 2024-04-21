package com.sistalk.serializable

import java.io.Serializable

class Student(val name:String, val age:Int, val score: Score):Serializable {

}

class Score(val math:Int,val english:Int, val chinese:Int) : Serializable{
    var grade:String = ""
    init {
        if (math > 90 && english > 90 && chinese > 90) {
            grade = "A"
        } else if (math > 80 && english > 80 && chinese > 80) {
            grade = "B"
        } else {
            grade = "C"
        }
    }
}