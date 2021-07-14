package com.example.menukerule.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.menukerule.R
import com.example.menukerule.core.*
import com.example.menukerule.core.extensions.*
import com.example.menukerule.data.local.AppDataBase
import com.example.menukerule.data.local.LocalMenuDataSource
import com.example.menukerule.data.model.MenuEntity
import com.example.menukerule.databinding.FragmentHomeMenuBinding
import com.example.menukerule.presentation.MenuViewModel
import com.example.menukerule.presentation.MenuViewModelFactory
import com.example.menukerule.repository.MenuRepositoryImpl
import com.example.menukerule.ui.adapter.MainMenuAdapter

class HomeMenuFragment : Fragment(R.layout.fragment_home_menu), MainMenuAdapter.OnMenuClickListener {

    private lateinit var binding: FragmentHomeMenuBinding
    private val actionImage = ImageController()
    private val viewModel by viewModels<MenuViewModel>{
        MenuViewModelFactory(MenuRepositoryImpl(LocalMenuDataSource(AppDataBase.getDatabase(requireContext()).menuDao())))
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeMenuBinding.bind(view)
        getMenus()
    }

    private fun getMenus(){
        viewModel.fetchMainScreenMenus().observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is Result.Loading ->{
                    binding.progressBar.show()
                }
                is Result.Success ->{
                    binding.progressBar.hide()
                    if(result.data.isEmpty()){
                        binding.emptyContainer.show()
                        return@Observer
                    }else{
                        binding.emptyContainer.hide()
                    }
                    binding.rvHome.adapter = MainMenuAdapter(result.data, 1,this@HomeMenuFragment)
                }
                is Result.Failure ->{
                    binding.progressBar.hide()
                    Log.d("errorHomeMenu","error: ${result.exception}")
                    context?.toastLong("! OCURRIO UN ERROR !")
                }
            }

        })
    }

    override fun onDeleteMenuClick(menus : MutableList<MenuEntity>, position: Int){
        val menu = menus[position]
        val id= menu.id
        val title = menu.title
        val imageName = menu.imageName
        val dialog = AlertDialog.Builder(requireContext())
                .setTitle("Eliminar menu")
                .setMessage("¿Esta seguro que quiere eliminar el menu: $title")
                .setNegativeButton("Cancelar") { view, _ ->
                     view.dismiss()
                }
                .setPositiveButton("Aceptar") { view, _ ->
                    deleteMenuEntity(id, menus, position)
                    if(imageName != ""){
                        actionImage.setContext(requireContext())
                        actionImage.deleteImageMenu(imageName)
                    }
                    view.dismiss()
                }
                .setCancelable(false)
                .create()
        dialog.show()
    }

    private fun deleteMenuEntity(id: Long, menus: MutableList<MenuEntity>, position: Int){
        viewModel.deleteMenu(id).observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is Result.Loading ->{
                    context?.toastShort("PROCESANDO...")
                }
                is Result.Success ->{
                    menus.removeAt(position)
                    binding.rvHome.adapter?.notifyItemRemoved(position)
                    context?.toastLong("MENU ELIMINADO")
                    if(menus.isEmpty()){
                        binding.emptyContainer.show()
                    }
                }
                is Result.Failure ->{
                    Log.d("errorHomeMenu","error: ${result.exception}")
                    context?.toastLong("! OCURRIO UN ERROR !")
                }
            }
        })
    }

}