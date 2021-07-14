package com.menu.menukerule.core

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

private const val SECOND_MILLIS = 1
private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
private const val DAY_MILLIS = 24 * HOUR_MILLIS

object TimeUtils {

    fun getTimeAgo(time: Int):String{
        val now = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
        if(time > now || time <=0){
            return "en el futuro"
        }

        val diff = now - time
        return when{
            diff < MINUTE_MILLIS -> "Justo ahora"
            diff < 2 * MINUTE_MILLIS -> "hace un minuto"
            diff < 60 * MINUTE_MILLIS -> "hace una hora"
            diff < 24 * HOUR_MILLIS -> "hace ${diff / HOUR_MILLIS} horas"
            diff < 48 * HOUR_MILLIS -> "ayer"
            else -> "hace ${diff / DAY_MILLIS} días"
        }
    }

    fun parseStringToDate(date: String, format: String): Date{
        val formatter = SimpleDateFormat(format)
        val formatDate = formatter.parse(date)
        return formatDate
    }

    fun getCurrentDate(format : String) : String {
        val sdf = SimpleDateFormat(format)
        val currentDate = sdf.format(Date())
        return currentDate
    }
}