package com.menu.menukerule.core.extensions

import android.content.Context
import android.widget.Toast


fun Context.toastLong(text: String) = Toast.makeText(this,text,Toast.LENGTH_LONG).show()


fun Context.toastShort(text: String) = Toast.makeText(this,text,Toast.LENGTH_SHORT).show()

