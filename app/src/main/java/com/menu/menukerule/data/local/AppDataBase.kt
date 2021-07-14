package com.menu.menukerule.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.menu.menukerule.data.model.MenuEntity

@Database(entities = [MenuEntity::class], version = 10)
abstract class AppDataBase: RoomDatabase() {

    abstract fun menuDao(): MenuDao

    companion object{
        private var INSTANCE: AppDataBase? = null

        fun getDatabase(context: Context): AppDataBase{
            INSTANCE = INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                AppDataBase::class.java,
                "menu_table"
            ).fallbackToDestructiveMigration().build()
            return INSTANCE!!
        }
    }
}