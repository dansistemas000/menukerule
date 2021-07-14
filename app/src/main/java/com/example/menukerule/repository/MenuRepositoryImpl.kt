package com.example.menukerule.repository

import com.example.menukerule.data.local.LocalMenuDataSource
import com.example.menukerule.data.model.MenuEntity
import com.example.menukerule.data.model.Totals


class MenuRepositoryImpl(private val dataSource: LocalMenuDataSource): MenuRepository {

   override suspend fun getMenus(): MutableList<MenuEntity> = dataSource.getMenus()

    override suspend fun getRandomMenus(totals: Totals): MutableList<MenuEntity> = dataSource.getRandomMenus(totals)

    override suspend fun getCheckedMenus(): MutableList<MenuEntity> = dataSource.getCheckedMenus()

    override suspend fun checkedMenus(menuList: MutableList<MenuEntity>): MutableList<MenuEntity> = dataSource.checkedMenus(menuList)

    override suspend fun saveMenu(menu: MenuEntity){
        dataSource.saveMenu(menu)
    }

    override suspend fun deleteMenu(id: Long) {
        dataSource.deleteMenu(id)
    }
}