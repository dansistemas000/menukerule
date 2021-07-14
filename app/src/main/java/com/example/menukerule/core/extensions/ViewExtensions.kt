package com.example.menukerule.core.extensions

import android.view.View
import com.example.menukerule.R

fun View.hide(){
    this.visibility = View.GONE
}

fun View.show(){
    this.visibility = View.VISIBLE
}

fun View.blueBB(){
    this.setBackgroundResource(R.drawable.bc_border_bottom_blue)
}

fun View.redBB(){
    this.setBackgroundResource(R.drawable.bc_border_bottom_red)
}

fun View.yellowBB(){
    this.setBackgroundResource(R.drawable.bc_border_bottom_yellow)
}

fun View.blackBB(){
    this.setBackgroundResource(R.drawable.bc_border_bottom_green)
}


