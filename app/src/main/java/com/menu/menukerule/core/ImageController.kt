package com.menu.menukerule.core

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import com.menu.menukerule.core.extensions.getDirFolder
import com.menu.menukerule.core.extensions.getDirTemp
import java.io.*


class ImageController {
    private var fileName = ""
    private  lateinit  var context: Context
    private lateinit var  mImageUri : Uri

    fun setContext(context: Context){
        this.context = context
    }

    fun getFileName() = fileName

    fun getUri() = mImageUri

    fun saveImage(fileName: String) : Boolean{
        val inFile  = File(context.getDirTemp(), AppConstants.TEMP_FILE)
        val outFilePath = File(context.getDirFolder(AppConstants.FOLDER_BASE).toString())
        if(!outFilePath.exists()) outFilePath.mkdirs()
        val currentDate = TimeUtils.getCurrentDate("ddMMyyyy")
        val randomNumber = (100..999).random().toString()
        this.fileName = "$fileName-$currentDate-$randomNumber.jpg"
        val outFile = File(context.getDirFolder(AppConstants.FOLDER_BASE), this.fileName)

        return try{
            val fIn = FileInputStream(inFile)
            val fOut = FileOutputStream(outFile)
            val buffer = ByteArray(1024)
            var c: Int

            while (fIn.read(buffer).also { c = it } != -1){
                fOut.write(buffer, 0, c)
            }

            fOut.flush()
            fOut.close()
            fIn.close()

            /*Log.d("orientation", "width: ${imageBitmap.width} height ${imageBitmap2.height}")
                 if(imageBitmap?.width > imageBitmap?.height){
                     val matrix = Matrix();
                     matrix.postRotate(90F);
                     val bit =  Bitmap.createBitmap(imageBitmap2, 0, 0, imageBitmap2.width, imageBitmap2.height, matrix, true)
                     binding.addImage.setImageBitmap(bit)
                 }else{
                     binding.addImage.setImageBitmap(imageBitmap2)
                 }*/
            saveMiniImage(inFile, outFilePath)
        } catch (e: FileNotFoundException) {
            Log.d("errorSaveImage", "error: $e")
            false
        } catch (e: IOException) {
            Log.d("errorSaveImage", "error: $e")
            false
        }
    }

    private fun saveMiniImage(inFile: File, outFilePath: File) : Boolean{
         val miniFileName = "mini_${this.fileName}"
        val file = File(outFilePath, miniFileName)
        val bitmap = BitmapFactory.decodeFile(inFile.toString())

        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 1000, 1000, false)

        return try{
            val fOut = FileOutputStream(file)
            resizedBitmap?.compress(Bitmap.CompressFormat.JPEG, 80, fOut)
            fOut.flush()
            fOut.close()
            true
        } catch (e: FileNotFoundException) {
            Log.d("errorSaveImage", "error: $e")
            false
        } catch (e: IOException) {
            Log.d("errorSaveImage", "error: $e")
            false
        }
    }

    private fun createImageTemp(context: Context): File? {
        val storageDir = context.getDirTemp()
        if(storageDir?.exists() == false) storageDir?.mkdirs()
        /*return File.createTempFile(
                imageFileName,  *//* prefix *//*
                ".jpg",  *//* suffix *//*
                storageDir *//* directory *//*
        )*/
        return File(storageDir.toString(), AppConstants.TEMP_FILE)
    }

    fun dispatchTakePictureIntent() : Intent? {
        deleteImageTemp()
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        return if (takePictureIntent.resolveActivity(context.packageManager) != null) {
            val imageFile: File? = createImageTemp(context)
            if (imageFile != null) {
                this.mImageUri = FileProvider.getUriForFile(
                        context,
                        "com.menu.menukerule.provider",
                        imageFile
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri)
                takePictureIntent
            } else {
                null
            }
        }else{
            null
        }
    }

     fun deleteImageTemp(){
        val dir = context.getDirTemp().toString()
        val fileName = AppConstants.TEMP_FILE
        deleteImage(dir,fileName)
    }

    private fun deleteImage(dir: String, fileName: String){
        val file = File(dir, fileName)
        try {
            if(file.exists()){
                file.delete()
            }
        }catch (e: Exception){
            Log.d("errorDeleteImage", "error : $e")
        }
    }

    fun deleteImageMenu(fileName: String){
        val dir = context.getDirFolder(AppConstants.FOLDER_BASE).toString()
        deleteImage(dir,fileName)
        val miniFileName = "mini_$fileName"
        deleteImage(dir,miniFileName)
    }


}