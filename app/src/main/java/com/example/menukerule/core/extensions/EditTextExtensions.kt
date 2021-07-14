package com.example.menukerule.core.extensions

import android.widget.EditText

fun EditText.cleanText() : String{
    val text = this.text.trim()
    return text.toString()
}