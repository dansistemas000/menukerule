package com.menu.menukerule.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.menu.menukerule.data.model.MenuEntity


@Dao
interface MenuDao {

    @Query("SELECT * FROM MenuEntity")
    suspend fun getAllMenus() : MutableList<MenuEntity>

    @Query("SELECT * FROM MenuEntity \n" +
            "WHERE id in(SELECT id FROM MenuEntity WHERE checked = 0 AND category = 'Pescado' ORDER BY RANDOM()  limit :totalFish)\n" +
            "or id in(SELECT id FROM MenuEntity WHERE checked = 0 AND category = 'Carne' ORDER BY RANDOM() LIMIT :totalBeef)\n" +
            "or id in(SELECT id FROM MenuEntity WHERE checked = 0 AND category = 'Otro' ORDER BY RANDOM() LIMIT :totalOther)\n" +
            "or id in(SELECT  id FROM MenuEntity WHERE checked = 0 AND category = 'Pollo' ORDER BY RANDOM() LIMIT :totalChicken)")
    suspend fun getRandomMenusCategory(totalOther : Int,
                               totalBeef : Int,
                               totalChicken : Int,
                               totalFish : Int) : MutableList<MenuEntity>

    @Query("SELECT  * FROM MenuEntity WHERE checked = 0 AND id NOT IN(:idsMenus) ORDER BY RANDOM() LIMIT :total")
    suspend fun getRandomMenusNotIds(total: Int, idsMenus : MutableList<Long>?) : MutableList<MenuEntity>

    @Query("SELECT  * FROM MenuEntity WHERE checked = 0 ORDER BY RANDOM() LIMIT :total")
    suspend fun getRandomMenus(total: Int) : MutableList<MenuEntity>

    @Query("UPDATE MenuEntity SET checked = 1, date_at = :currentDate WHERE id in(:ids)")
    suspend fun checkedMenus(ids : MutableList<Long>, currentDate : String)

    @Query("UPDATE MenuEntity SET checked = 0 WHERE checked = 1")
    suspend fun clearCheckedMenus()

    @Query("SELECT * FROM MenuEntity WHERE checked=1")
    suspend fun getCheckedMenus() : MutableList<MenuEntity>

    @Query("DELETE FROM MenuEntity WHERE id = :id")
    suspend fun deleteMenu(id: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMenu(menu : MenuEntity)
}