package com.example.menukerule.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.menukerule.core.TimeUtils


@Entity
data class MenuEntity(
        @ColumnInfo(name="title")
        val title: String = "",
        @ColumnInfo(name="description")
        val description: String = "",
        @ColumnInfo(name="category")
        val category: String = "",
        @ColumnInfo(name="imageName")
        val imageName: String = "",
        @ColumnInfo(name="checked", defaultValue = "false")
        val checked: Boolean = false,
        @ColumnInfo(name="date_at")
        val date_at: String = TimeUtils.getCurrentDate("dd/MM/yyyy HH:mm:ss"),
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0)


