package com.chokus.konye.privatememo.activity

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.chokus.konye.privatememo.datamodel.NoteClass
import com.chokus.konye.privatememo.datamanager.NoteRealmManager
import com.chokus.konye.privatememo.adapter.NoteRecyclerAdapter
import com.chokus.konye.privatememo.R
import com.chokus.konye.privatememo.Util.Utility
import kotlinx.android.synthetic.main.content_note_list.*
import kotlinx.android.synthetic.main.security_dialog.*
import java.io.File
import java.io.FileOutputStream

import javax.inject.Inject
import kotlin.properties.Delegates

class NoteListActivity : AppCompatActivity() {
    private var noteRecyclerAdapter : NoteRecyclerAdapter? = null
    private var notesList: MutableList<NoteClass> = mutableListOf<NoteClass>()
    private var noteDrawerLayout: DrawerLayout? = null
    private var noteDrawerRelativeLayout: RelativeLayout? = null
    private var topMenuIcon: ImageView? = null
    private  var file: File? = null
    @Inject lateinit var noteRealmManager : NoteRealmManager
    private lateinit var linearLayoutManager : LinearLayoutManager
    private lateinit var gridLayoutManager: GridLayoutManager
    companion object {
        const val NOTE_TITLE = "com.chokus.konye.privatememo NoteTitle"
        const val NOTE_CONTENT = "com.chokus.konye.privatememo NoteContent"
        const val DATE_CREATED = "com.chokus.konye.privatememo DateCreated"
        val MY_REQUEST_CAMERA = 101
        val MY_REQUEST_WRITE_CAMERA = 102
        val CAPTURE_CAMERA = 103

        val MY_REQUEST_READ_GALLERY = 104
        val MY_REQUEST_WRITE_GALLERY = 105
        val MY_REQUEST_GALLERY = 106
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_list)
        setFullScreen()
        //initializing lateinit variables
        noteRealmManager = NoteRealmManager()
        //this gridLayoutManager with number of columns included
        val noOfColumns : Int = Utility.calculateNoOfColumns(this)
        gridLayoutManager = GridLayoutManager(this, noOfColumns)
        //linearLayoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        notesList = noteRealmManager.findAll()
        viewActions()
        sideMenuWidgets()
        //add a realmchange listener
    }

    private fun viewActions() {
        note_recyclerView.layoutManager = gridLayoutManager as RecyclerView.LayoutManager?
        noteRecyclerAdapter = NoteRecyclerAdapter(notesList, this)
        note_recyclerView.adapter = noteRecyclerAdapter
        noteRecyclerAdapter!!.notifyDataSetChanged()
        new_note_imageView.setOnClickListener {
            val intent = Intent(applicationContext, NoteActivity::class.java)
            intent.putExtra(NOTE_TITLE, "")
            intent.putExtra(NOTE_CONTENT, "")
            startActivity(intent)
        }
        search_imgView.setOnClickListener {
            search_imgView.visibility = View.GONE
            search_box_relativeLayout.visibility = View.VISIBLE
        }
        cancel_imgView.setOnClickListener {
            search_box_relativeLayout.visibility = View.GONE
            search_imgView.visibility = View.VISIBLE
        }
        add_photo_imageView.setOnClickListener {
            //for getting image from camera or from gallery
            pickImageSourceDialog()
        }
        removeBackground()
    }

    private fun removeBackground(){
        if(notesList.isEmpty()){
            noteList_bg.visibility = View.VISIBLE
        }else{
            noteList_bg.visibility = View.GONE
        }
    }

    private fun sideMenuWidgets() {
        noteDrawerLayout = findViewById<View>(R.id.note_drawer_layout) as DrawerLayout
        noteDrawerRelativeLayout = findViewById<View>(R.id.note_relative_drawer_layout) as RelativeLayout
        topMenuIcon = findViewById<View>(R.id.top_menu_icon) as ImageView
        topMenuIcon!!.setOnClickListener { noteDrawerLayout!!.openDrawer(noteDrawerRelativeLayout!!) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //with when we select the request code we asked for in the StartActivityForResult
        when(requestCode){
            CAPTURE_CAMERA ->
                user_display_imageView.setImageURI(Uri.parse("file:///" + file))

            MY_REQUEST_GALLERY ->
                getImageFromGallery(data)
        }
    }

    private fun getImageFromGallery(data: Intent?){
        try {
            val inputStream = contentResolver.openInputStream(data?.data)
            file = getFile()
            val fileOutputStream = FileOutputStream(file)
            val buffer = ByteArray(1024)
            var bytesRead : Int
            while (true){
                bytesRead = inputStream.read(buffer)
                if (bytesRead == -1) break
                fileOutputStream.write(buffer, 0, bytesRead)
            }
            fileOutputStream.close()
            inputStream.close()
            user_display_imageView.setImageURI(Uri.parse("file:///" + file))
        }catch (e : Exception){
            Log.e("", "Error while creating temp file", e)
        }
    }

    private fun checkPermissionCamera(){
        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf<String>(Manifest.permission.CAMERA), MY_REQUEST_CAMERA)
        }else{
            //do something about camera here
            catchPhoto()
        }
    }

    private fun checkPermissionCWES(){
        //CWES meaning camera write external storage
        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE), MY_REQUEST_WRITE_CAMERA)
        }else{
            // check permission for camera cos there is already permission to write to external storage
            checkPermissionCamera()
        }
    }

    private fun catchPhoto(){
        file = getFile()
        if (file != null){
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try{
                //val photoUri = Uri.fromFile(file)
                val photoUri = FileProvider.getUriForFile(this,this.applicationContext.packageName + ".my.package.name.provider", file!!)
                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,photoUri)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivityForResult(intent, CAPTURE_CAMERA)
            }catch (e : ActivityNotFoundException){
                //do nothing
            }
        }else{
            toastMethod("please check your sd card")
        }
    }

    fun getFile() : File?{
        val fileDir = File(""+ Environment.getExternalStorageDirectory() + "/Android/data/" + applicationContext.packageName + "/Files")
        if(!fileDir.exists()){
            if(!fileDir.mkdir()){
                return null
            }
        }
        val mediaFile = File(fileDir.path + File.separator + "temp.jpg")
        return mediaFile
    }

    private fun checkPermissionRG(){
        //RG meaning read gallery
        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE), MY_REQUEST_READ_GALLERY)
        }else{
            checkPermissionWG()
        }
    }

    private fun checkPermissionWG(){
        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE), MY_REQUEST_WRITE_GALLERY)
        }else{
            getPhotos()
        }
    }

    private fun getPhotos(){
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, MY_REQUEST_GALLERY)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            MY_REQUEST_CAMERA -> catchPhoto()
            MY_REQUEST_WRITE_CAMERA -> checkPermissionCamera()
            MY_REQUEST_READ_GALLERY -> checkPermissionWG()
            MY_REQUEST_WRITE_GALLERY -> getPhotos()
        }
    }

    private fun pickImageSourceDialog(){
        val alertDialog : AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialog.setTitle("Pick Image Source")
        alertDialog.setMessage("Where do you want to Select Item Image  from?")
        alertDialog.setPositiveButton("Take Photo", DialogInterface.OnClickListener { dialog, which ->
            dialog.cancel()
            checkPermissionCWES()
        })
        alertDialog.setNegativeButton("Gallery", DialogInterface.OnClickListener { dialog, which ->
            dialog.cancel()
            checkPermissionRG()
        })
        val dialog : AlertDialog = alertDialog.create()
        dialog.show()

        //get the alertDialog button references
        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
        //change color of button text
        positiveButton.setTextColor(resources.getColor(R.color.colorThemeDarker))
        negativeButton.setTextColor(resources.getColor(R.color.colorThemeDarker))
    }

    private fun setFullScreen() {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun toastMethod(message : String?){
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}

