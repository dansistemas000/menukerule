package com.example.menukerule.repository

import com.example.menukerule.data.model.MenuEntity
import com.example.menukerule.data.model.Totals


interface MenuRepository {
    suspend fun getMenus() : MutableList<MenuEntity>

    suspend fun getRandomMenus(totals: Totals) : MutableList<MenuEntity>

    suspend fun getCheckedMenus() : MutableList<MenuEntity>

    suspend fun saveMenu(menu : MenuEntity)

    suspend fun deleteMenu(id: Long)

    suspend fun checkedMenus(menuList : MutableList<MenuEntity>): MutableList<MenuEntity>
}