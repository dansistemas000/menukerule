package com.menu.menukerule.ui.adapter

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.menu.menukerule.R
import com.menu.menukerule.core.AppConstants
import com.menu.menukerule.core.BaseViewHolder
import com.menu.menukerule.core.TimeUtils
import com.menu.menukerule.core.extensions.*
import com.menu.menukerule.data.model.MenuEntity
import com.menu.menukerule.databinding.MenuItemBinding
import java.io.File
import java.util.*


class MainMenuAdapter(private val menuList: MutableList<MenuEntity>, private var flag: Int, private val itemClickListener: OnMenuClickListener? = null): RecyclerView.Adapter<BaseViewHolder<*>>() {

    private lateinit var thisContext: Context
    private lateinit var dir : String


    interface OnMenuClickListener{
        fun onDeleteMenuClick(menus: MutableList<MenuEntity>, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        thisContext = parent.context
        dir = thisContext.getDirFolder(AppConstants.FOLDER_BASE).toString()

        val itemBinding = MenuItemBinding.inflate(
                LayoutInflater.from(thisContext),
                parent,
                false
        )
        val holder = HomeMenuViewHolder(itemBinding, thisContext)


        itemBinding.txtDescription.setOnClickListener{
            itemBinding.txtDescription.toggle()
        }


        itemBinding.btnClose.setOnClickListener{
            val position = holder.bindingAdapterPosition.takeIf {
                it != DiffUtil.DiffResult.NO_POSITION
            }?: return@setOnClickListener
            itemClickListener?.let{
                it.onDeleteMenuClick(menuList, position)
            }
        }


        showImageDialog(itemBinding, dir,  holder, thisContext)

        return holder
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when(holder){
            is HomeMenuViewHolder -> holder.bind(menuList[position])
        }
    }

    override fun getItemCount(): Int = menuList.size

    private inner class HomeMenuViewHolder(
            val binding: MenuItemBinding,
            val context: Context
    ):BaseViewHolder<MenuEntity>(binding.root){
        override fun bind(item: MenuEntity) {

            binding.menuTitle.text = item.title
            binding.btnClose.show()

            setCategoryColor(item.category)
            setDescription(item.description)
            showImage("mini_${item.imageName}",dir,binding.ivImage)
            setDate(item.date_at)

            if(flag == 1){
                binding.btnCheck.hide()
                binding.btnClose.show()
                //binding.btnUpdate.show()
            }else{
                binding.btnClose.hide()
               // binding.btnUpdate.hide()
                if(item.checked){
                    binding.btnCheck.show()
                }else{
                    binding.btnCheck.hide()
                }
            }


        }

        private fun setCategoryColor(category: String){
            when(category){
                "Pescado" -> {
                    binding.llMainContainer.blueBB()
                    binding.ivImage.setImageResource(R.drawable.fish)
                }
                "Carne" -> {
                    binding.llMainContainer.redBB()
                    binding.ivImage.setImageResource(R.drawable.beef)
                }
                "Pollo" -> {
                    binding.llMainContainer.yellowBB()
                    binding.ivImage.setImageResource(R.drawable.chicken)
                }
                "Otro" -> {
                    binding.llMainContainer.blackBB()
                    binding.ivImage.setImageResource(R.drawable.food)
                }

            }
        }

        private fun setDescription(description: String){
            if(description.isNotEmpty()){
                binding.txtDescription.text = description
                binding.txtDescription.show()
            }
        }


        private fun setDate(date_at: String){
            if(date_at.isNotEmpty()){
                val date = TimeUtils.parseStringToDate(date_at, "dd/MM/yyyy HH:mm:ss")
                val milliseconds = date?.time?.div(1000L)
                binding.menuDate.text = TimeUtils.getTimeAgo(milliseconds.toInt())
            }
        }
    }

    private fun showImage(fileName: String, dir :String, imageView : ImageView){
        if(fileName.isNotEmpty()){
            val file = File(dir, fileName)
            if(file.exists()){
                val myUri = Uri.parse(file.toString())
                Glide.with(thisContext).load(file).centerCrop().into(imageView)
            }
        }
    }

    private fun showImageDialog(item: MenuItemBinding, dir: String, holder: HomeMenuViewHolder, context: Context){
        item.ivImage.setOnClickListener{
            val position = holder.bindingAdapterPosition.takeIf {
                it != DiffUtil.DiffResult.NO_POSITION
            }?: return@setOnClickListener
            val fileName = menuList[position].imageName
            if(fileName != ""){
                val imageView = ImageView(context)
                val builder = Dialog(context)
                builder.requestWindowFeature(Window.FEATURE_NO_TITLE)
                builder.window?.setBackgroundDrawable(
                        ColorDrawable(Color.TRANSPARENT))
                val lp = RelativeLayout.LayoutParams(900, 900)
                imageView.layoutParams = lp
                showImage(fileName, dir,imageView)
                builder.addContentView(imageView, RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT))
                builder.show()
            }

        }

    }
}