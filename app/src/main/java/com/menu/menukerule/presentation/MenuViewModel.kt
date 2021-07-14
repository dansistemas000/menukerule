package com.menu.menukerule.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.menu.menukerule.repository.MenuRepository
import kotlinx.coroutines.Dispatchers
import com.menu.menukerule.core.Result
import com.menu.menukerule.data.model.MenuEntity
import com.menu.menukerule.data.model.Totals

class MenuViewModel(private val repo: MenuRepository): ViewModel() {

    fun fetchMainScreenMenus() = liveData(Dispatchers.IO){
        emit(Result.Loading())
        try {
            emit(Result.Success(repo.getMenus()))
        }catch (e: Exception){
            emit(Result.Failure(e))

        }
    }

    fun getRandomMenus(totals: Totals) = liveData(Dispatchers.IO){
        emit(Result.Loading())
        try {
            emit(Result.Success(repo.getRandomMenus(totals)))
        }catch (e: Exception){
            emit(Result.Failure(e))

        }
    }

    fun getCheckedMenus() = liveData(Dispatchers.IO){
        emit(Result.Loading())
        try {
            emit(Result.Success(repo.getCheckedMenus()))
        }catch (e: Exception){
            emit(Result.Failure(e))
        }
    }

    fun saveMenu(menu: MenuEntity) = liveData(Dispatchers.IO){
        emit(Result.Loading())
        try {
            emit(Result.Success(repo.saveMenu(menu)))
        }catch (e: Exception){
            emit(Result.Failure(e))

        }
    }

    fun deleteMenu(id : Long)= liveData(Dispatchers.IO){
        emit(Result.Loading())
        try {
            emit(Result.Success(repo.deleteMenu(id)))
        }catch (e: Exception){
            emit(Result.Failure(e))
        }
    }

    fun checkedMenus(menuList : MutableList<MenuEntity>)= liveData(Dispatchers.IO){
        emit(Result.Loading())
        try {
            emit(Result.Success(repo.checkedMenus(menuList)))
        }catch (e: Exception){
            emit(Result.Failure(e))
        }
    }
}


class MenuViewModelFactory(private val repo: MenuRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MenuViewModel(repo) as T
    }
}