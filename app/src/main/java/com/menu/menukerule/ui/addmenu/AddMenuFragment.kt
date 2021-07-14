package com.menu.menukerule.ui.addmenu

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.menu.menukerule.R
import com.menu.menukerule.core.*
import com.menu.menukerule.core.extensions.*
import com.menu.menukerule.data.local.AppDataBase
import com.menu.menukerule.data.local.LocalMenuDataSource
import com.menu.menukerule.data.model.MenuEntity
import com.menu.menukerule.databinding.FragmentAddMenuBinding
import com.menu.menukerule.presentation.MenuViewModel
import com.menu.menukerule.presentation.MenuViewModelFactory
import com.menu.menukerule.repository.MenuRepositoryImpl
import java.io.IOException
import java.util.*


class AddMenuFragment : Fragment(R.layout.fragment_add_menu) {

    private val REQUEST_IMAGE_CAPTURE = 1;
    private lateinit var binding: FragmentAddMenuBinding
    private val imageAction = ImageController()
    private var imageUri : Uri? = null

    private val viewModel by viewModels<MenuViewModel>{
        MenuViewModelFactory(MenuRepositoryImpl(LocalMenuDataSource(AppDataBase.getDatabase(requireContext()).menuDao())))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddMenuBinding.bind(view)
        createMenu(view)
        addImage()
        clearRadioBottomError()
    }

    private fun clearRadioBottomError(){
        binding.rgbCategory.setOnCheckedChangeListener { group, checkedId ->
            binding.txtErrorCategory.hide()
        }
    }

    private fun addImage(){
        binding.addImage.setOnClickListener {
            imageAction.setContext(requireContext())
            val intent = imageAction.dispatchTakePictureIntent()
            intent?.let {
                 startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
             try {
                 imageUri = imageAction.getUri()
                 imageUri?.let {
                     val random = (1000..9999).random().toString()
                     Glide.with(requireContext()).load(imageUri).signature(ObjectKey(random)).centerCrop().into(binding.addImage)
                 }
             } catch (e: IOException) {
                Log.d("errorAddMenu", "error: $e")
             }
        }

    }

    private fun createMenu(view: View){
        binding.btnCreateMenu.setOnClickListener {
            val title = binding.txtTitle.cleanText()
            val description = binding.txtDescription.cleanText()
            val category  = binding.rgbCategory.getCheckedText(view)
            var photo = ""

            if (validateData(title, category)) return@setOnClickListener

            imageUri?.let {
                if(imageAction.saveImage(title)){
                    photo = imageAction.getFileName()
                }
            }
            viewModel.saveMenu(MenuEntity(title, description, category, photo)).observe(viewLifecycleOwner, Observer { result ->
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.show()
                        binding.btnCreateMenu.isEnabled = false
                    }
                    is Result.Success -> {
                        cleanItems()
                        context?.toastLong("Elemento Guardado")
                    }
                    is Result.Failure -> {
                        binding.progressBar.hide()
                        binding.btnCreateMenu.isEnabled = true
                        context?.toastLong("Error: ${result.exception}")
                        Log.e("errorAddMenu", "error: ${result.exception} ")
                    }
                }
            })
        }
    }

    private fun cleanItems(){
        imageUri = null
        binding.progressBar.hide()
        binding.btnCreateMenu.isEnabled = true
        binding.txtTitle.setText("")
        binding.txtDescription.setText("")
        binding.rgbCategory.clearCheck()
        binding.addImage.setImageResource(R.drawable.ic_baseline_add_a_photo_24)
        imageAction.deleteImageTemp()
    }

    private fun validateData(title: String, category: String): Boolean {
        if (title.isEmpty()) {
            binding.txtTitle.error = "El menu no debe estar vacio"
            return true
        }

        if (category.isEmpty()) {
            binding.txtErrorCategory.show()
            return true
        } else {
            binding.txtErrorCategory.hide()
        }
        return false
    }


}
