package com.menu.menukerule.ui.randommenu

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.menu.menukerule.R
import com.menu.menukerule.core.Result
import com.menu.menukerule.core.extensions.hide
import com.menu.menukerule.core.extensions.show
import com.menu.menukerule.core.extensions.toastLong
import com.menu.menukerule.data.local.AppDataBase
import com.menu.menukerule.data.local.LocalMenuDataSource
import com.menu.menukerule.data.model.MenuEntity
import com.menu.menukerule.data.model.Totals
import com.menu.menukerule.databinding.FragmentRandomMenuBinding
import com.menu.menukerule.presentation.MenuViewModel
import com.menu.menukerule.presentation.MenuViewModelFactory
import com.menu.menukerule.repository.MenuRepositoryImpl
import com.menu.menukerule.ui.adapter.MainMenuAdapter
import java.lang.Integer.parseInt


class RandomMenuFragment : Fragment(R.layout.fragment_random_menu) {

    private var total : Int = 0
    private var mainTotal : Int = 0
    private lateinit var menuList : MutableList<MenuEntity>
    private lateinit var binding: FragmentRandomMenuBinding
    private val viewModel by viewModels<MenuViewModel>{
        MenuViewModelFactory(
            MenuRepositoryImpl(
                LocalMenuDataSource(
                    AppDataBase.getDatabase(
                        requireContext()
                    ).menuDao()
                )
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRandomMenuBinding.bind(view)

        viewModel.getCheckedMenus().observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.show()
                }
                is Result.Success -> {
                    binding.progressBar.hide()
                    binding.rvHome.adapter = MainMenuAdapter(result.data,2)
                }
                is Result.Failure -> {
                    binding.progressBar.hide()
                    Log.d("errorRandomMenu", "error: ${result.exception}")
                    context?.toastLong("! OCURRIO UN ERROR !")
                }
            }

        })

        val llOther = binding.llOther
        val llBeef = binding.llBeef
        val llChicken = binding.llChicken
        val llFish = binding.llFish
        val llMain = binding.llMain

        val categoryList: MutableList<View> = mutableListOf(llOther,llBeef,llChicken,llFish)

        countController(llOther)
        countController(llBeef)
        countController(llChicken)
        countController(llFish)
        mainCountController(binding.llMain,categoryList)


        binding.btnGenerateMenu.setOnClickListener {

            val totalOther = parseInt(llOther.findViewById<TextView>(R.id.txt_count).text.toString())
            val totalBeef = parseInt(llBeef.findViewById<TextView>(R.id.txt_count).text.toString())
            val totalChicken = parseInt(llChicken.findViewById<TextView>(R.id.txt_count).text.toString())
            val totalFish = parseInt(llFish.findViewById<TextView>(R.id.txt_count).text.toString())
            val total = mainTotal - total

            viewModel.getRandomMenus(Totals(totalOther,totalBeef,totalChicken,totalFish,total)).observe(viewLifecycleOwner, Observer { result ->
                when(result){
                    is Result.Loading ->{
                        binding.progressBar.show()
                        binding.rvHome.hide()
                    }
                    is Result.Success ->{
                        binding.progressBar.hide()
                        binding.rvHome.show()
                        if(result.data.isNotEmpty()){
                            menuList = result.data
                            binding.btnSaveMenu.show()
                            binding.rvHome.adapter = MainMenuAdapter(menuList,2)
                        }else{
                            binding.btnSaveMenu.hide()
                        }
                    }
                    is Result.Failure ->{
                        binding.progressBar.hide()
                        Log.d("errorRandomMenu","error: ${result.exception}")
                        context?.toastLong("! OCURRIO UN ERROR !")
                    }
                }
            })
        }

        binding.btnSaveMenu.setOnClickListener {
            viewModel.checkedMenus(menuList).observe(viewLifecycleOwner, Observer { result ->
                when(result){
                    is Result.Loading ->{
                        binding.progressBar.show()
                        binding.rvHome.hide()
                        binding.btnSaveMenu.hide()
                    }
                    is Result.Success ->{
                        resetCounts(llMain)
                        resetCounts(categoryList)
                        cleanItems()
                        binding.rvHome.adapter = MainMenuAdapter(result.data,2)
                    }
                    is Result.Failure ->{
                        binding.progressBar.hide()
                        binding.btnSaveMenu.show()
                        Log.d("errorRandomMenu","error: ${result.exception}")
                        context?.toastLong("! OCURRIO UN ERROR !")
                    }
                }
            })
        }
    }

    private fun cleanItems(){
        binding.btnSaveMenu.hide()
        binding.progressBar.hide()
        binding.rvHome.show()
        total= 0
        mainTotal = 0
    }


    private fun countController(view : View){
        val less = view.findViewById<ImageView>(R.id.iv_less)
        val more = view.findViewById<ImageView>(R.id.iv_more)
        val txt = view.findViewById<TextView>(R.id.txt_count)
        less.setOnClickListener {
            var count = parseInt(txt.text.toString())
            if(count > 0){
                total--
                count--
                txt.text = count.toString()
            }
        }
        more.setOnClickListener {
            var count = parseInt(txt.text.toString())
            if(count < 99 && mainTotal > total){
                count++
                total++
                txt.text = count.toString()
            }
        }
    }

    private fun mainCountController(view : View,categories : MutableList<View>){
        val less = view.findViewById<ImageView>(R.id.iv_less)
        val more = view.findViewById<ImageView>(R.id.iv_more)
        val txt = view.findViewById<TextView>(R.id.txt_count)

        less.setOnClickListener {
            var count = parseInt(txt.text.toString())
            if(count > 0){
                count--
                mainTotal--
                txt.text = count.toString()
                resetCounts(categories)
            }
        }
        more.setOnClickListener {
            var count = parseInt(txt.text.toString())
            if(count < 99){
                count++
                mainTotal++
                txt.text = count.toString()
                resetCounts(categories)
            }
        }
    }

    private fun resetCounts(view : MutableList<View>){
        for (item in view){
            val text = item.findViewById<TextView>(R.id.txt_count)
            text.text = "0"
        }
        total = 0
    }

    private fun resetCounts(view: View){
        val text = view.findViewById<TextView>(R.id.txt_count)
        text.text = "0"
    }
}
