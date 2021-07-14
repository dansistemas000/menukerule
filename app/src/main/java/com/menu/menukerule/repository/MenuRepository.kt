package com.menu.menukerule.repository

import com.menu.menukerule.data.model.MenuEntity
import com.menu.menukerule.data.model.Totals


interface MenuRepository {
    suspend fun getMenus() : MutableList<MenuEntity>

    suspend fun getRandomMenus(totals: Totals) : MutableList<MenuEntity>

    suspend fun getCheckedMenus() : MutableList<MenuEntity>

    suspend fun saveMenu(menu : MenuEntity)

    suspend fun deleteMenu(id: Long)

    suspend fun checkedMenus(menuList : MutableList<MenuEntity>): MutableList<MenuEntity>
}