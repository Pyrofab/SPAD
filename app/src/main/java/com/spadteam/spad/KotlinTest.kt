package com.spadteam.spad

import android.widget.Toast

/**
 * Created by Fabien on 18/05/2017.
 */
class KotlinTest {
    private data class SPADContact(val name : String, val number: Int, val mail : String)

    fun test(abc : String): Int? {
        println(abc)
        return SPADContact("abc", 12, "abc@gmail.com").number
    }
}