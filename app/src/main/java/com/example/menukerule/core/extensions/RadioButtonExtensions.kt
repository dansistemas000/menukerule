package com.example.menukerule.core.extensions

import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup

fun RadioGroup.getCheckedText(view: View): String{
    if(this.checkedRadioButtonId != -1){
        val checkId  = this.checkedRadioButtonId
        val checkRadio : RadioButton = view.findViewById(checkId)
        return checkRadio.text.toString()
    }else{
        return ""
    }
}