package com.menu.menukerule.core.extensions

import android.content.Context
import android.os.Environment


fun Context.getDirFolder(folder : String) = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES + folder)

fun Context.getDirTemp() = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/.temp/")

