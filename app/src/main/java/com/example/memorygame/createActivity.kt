package com.example.memorygame

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.memorygame.databinding.ActivityCreateBinding
import com.example.memorygame.models.Boradsize
import com.example.memorygame.utils.EXTRA_BOARD_SIZE

class createActivity : AppCompatActivity() {
    companion object{
      private const val PICK_PHOTO_CODE=65
        const val REQUEST_PHOTO_CODE=25
        private const val READ_PHOTO_PERMISSION=android.Manifest.permission.READ_MEDIA_IMAGES
    }
    lateinit var imagePickerViewAdapter: ImagePickerViewAdapter
     lateinit var binding:ActivityCreateBinding
    private lateinit var boardsize:Boradsize
    private var numImagerequired=0
     var imageList = mutableListOf<Uri>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding= ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)


        boardsize=intent.getSerializableExtra(EXTRA_BOARD_SIZE) as Boradsize
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
         numImagerequired=boardsize.getNumparis()
        supportActionBar?.title="choose pics (0/$numImagerequired)"
        binding.imagePicker.layoutManager=GridLayoutManager(this,boardsize.getwidth())
      imagePickerViewAdapter=ImagePickerViewAdapter(this,imageList,boardsize,object :ImagePickerViewAdapter.ImageClickListener{
            override fun onPlaceholderClicked() {
                if (ispermissionGranted(this@createActivity, READ_PHOTO_PERMISSION)) {
                    launchIntentForPhotos()
                }
                else{
                    Log.d("createActivity", "Requesting permission for external storage")
                    requestPermision(this@createActivity, READ_PHOTO_PERMISSION, REQUEST_PHOTO_CODE)
                }
            }
        })
        binding.imagePicker.adapter=imagePickerViewAdapter

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode== REQUEST_PHOTO_CODE){
            Log.d("createActivity", "Code is correct")
            Log.d("createActivityc", "grantResults: ${grantResults.joinToString()}, requestCode: $requestCode")
            if (grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED ){
                Log.d("createActivityb", "grantResults: ${grantResults.joinToString()}, requestCode: $requestCode")
                Log.d("createActivityl", "launching intent after permission")
                launchIntentForPhotos()
            }else{
                Toast.makeText(this, "In order to create game,you need to provide permission", Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId== android.R.id.home){
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != PICK_PHOTO_CODE||resultCode !=Activity.RESULT_OK|| data==null){
            Log.w("createAcitivitty","Did not get data from  the launched activity ,user likely canceled")
            return
        }
        val selectedUri=data.data
        val clipData=data.clipData
        if (clipData!=null){
            for (i in 0 until clipData.itemCount){
                val clipitem=clipData.getItemAt(i)
                if (imageList.size<numImagerequired){
                    imageList.add(clipitem.uri)
                }
            }
        }else if (selectedUri!=null){
            imageList.add(selectedUri)
        }
        imagePickerViewAdapter.notifyDataSetChanged()
        supportActionBar?.title="${imageList.size}/$numImagerequired"


    }
    private fun launchIntentForPhotos() {
        val intent =Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
        startActivityForResult(Intent.createChooser(intent,"choose Photo"), PICK_PHOTO_CODE)
    }


}