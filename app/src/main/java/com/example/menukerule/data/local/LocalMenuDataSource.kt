package com.example.menukerule.data.local

import com.example.menukerule.core.TimeUtils
import com.example.menukerule.data.model.MenuEntity
import com.example.menukerule.data.model.Totals


class LocalMenuDataSource(private val menuDao: MenuDao) {

    suspend fun getMenus(): MutableList<MenuEntity> {
        return menuDao.getAllMenus()
    }

    suspend fun getRandomMenus(totals: Totals): MutableList<MenuEntity>{

        val totalsCategory = totals.totalOther+totals.totalBeef+totals.totalChicken+totals.totalFish
        var menusList: MutableList<MenuEntity>? = null

        if(totalsCategory == 0){
            menusList = menuDao.getRandomMenus(totals.total)
        }else{
            if(totals.total > 0){
                menusList = menuDao.getRandomMenusCategory(totals.totalOther,totals.totalBeef,totals.totalChicken,totals.totalFish)
                var idsMenus : MutableList<Long> = arrayListOf()
                for(item in menusList){
                    idsMenus.add(item.id)
                }
                var randomMenusNotId = menuDao.getRandomMenusNotIds(totals.total,idsMenus)
                for (item in randomMenusNotId){
                    menusList.add(item)
                }
            }else{
                menusList = menuDao.getRandomMenusCategory(totals.totalOther,totals.totalBeef,totals.totalChicken,totals.totalFish)
            }
        }
        return menusList
    }

    suspend fun checkedMenus(menuList : MutableList<MenuEntity>) : MutableList<MenuEntity>{
        var idsMenus : MutableList<Long> = arrayListOf()
        for(item in menuList){
            idsMenus.add(item.id)
        }
        menuDao.clearCheckedMenus()
        val currentDate = TimeUtils.getCurrentDate("dd/MM/yyyy HH:mm:ss")
        menuDao.checkedMenus(idsMenus, currentDate)
        return menuDao.getCheckedMenus()
    }

    suspend fun getCheckedMenus(): MutableList<MenuEntity>{
        return menuDao.getCheckedMenus()
    }

    suspend fun saveMenu(menu : MenuEntity){
        menuDao.saveMenu(menu)
    }

    suspend fun deleteMenu(id : Long){
        menuDao.deleteMenu(id)
    }
}