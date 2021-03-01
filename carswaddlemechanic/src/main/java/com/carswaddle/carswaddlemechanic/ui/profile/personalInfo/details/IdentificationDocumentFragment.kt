package com.carswaddle.carswaddlemechanic.ui.profile.personalInfo.details

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.carswaddle.carswaddleandroid.services.IdDocumentImageSide
import com.carswaddle.carswaddleandroid.ui.view.ProgressButton
import com.carswaddle.carswaddlemechanic.R
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import java.io.File
import java.net.URI
import java.net.URISyntaxException


class IdentificationDocumentFragment : Fragment() {

    private lateinit var viewModel: IdentificationDocumentViewModel

    private lateinit var selectFrontTextView: TextView
    private lateinit var selectBackTextView: TextView
    
    private lateinit var frontExplainTextView: TextView
    private lateinit var backExplainTextView: TextView

    private lateinit var frontImageView: ImageView
    private lateinit var backImageView: ImageView
    
    private lateinit var saveButton: ProgressButton
    
    private var currentSelection: IdDocumentImageSide? = null
    
    private var didSetFrontImage: Boolean = false
    set(newValue) {
        field = newValue
        if (newValue == true) {
            frontExplainTextView.visibility = View.GONE
        }
    }
    private var didSetBackImage: Boolean = false
        set(newValue) {
            field = newValue
            if (newValue == true) {
                backExplainTextView.visibility = View.GONE
            }
        }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(IdentificationDocumentViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_identification_document, container, false)

        selectFrontTextView = root.findViewById(R.id.selectFrontTextView)
        selectBackTextView = root.findViewById(R.id.selectBackTextView)
        
        frontExplainTextView = root.findViewById(R.id.frontExplainTextView)
        backExplainTextView = root.findViewById(R.id.backExplainTextView)

        frontImageView = root.findViewById(R.id.frontImageView)
        backImageView = root.findViewById(R.id.backImageView)

        saveButton = root.findViewById(R.id.saveDocumentsButton)

        selectFrontTextView.setOnClickListener {
            currentSelection = IdDocumentImageSide.front
            showImagePicker()
        }
        
        selectBackTextView.setOnClickListener {
            currentSelection = IdDocumentImageSide.back
            showImagePicker()
        }
        
        saveButton.button.setOnClickListener {
            saveDocuments()
        }
        
        return root
    }
    
    private fun showImagePicker() {
//        ImagePicker.create(this)
//            .returnMode(ReturnMode.ALL) // set whether pick action or camera action should return immediate result or not. Only works in single mode for image picker
//            .folderMode(false) // set folder mode (false by default)
//            .single()
////            .toolbarFolderTitle("Folder") // folder selection title
//            .toolbarImageTitle("Tap to select")
//            .toolbarDoneButtonText("DONE") // done button text
//            .start(0) // image selection title


//        askPermission(
//            "android.permission.CAMERA",
//            "android.permission.WRITE_EXTERNAL_STORAGE"
//        ) {
//            // camera or gallery or TODO
//            print("hey")
//        }.onDeclined { e ->
//            if (e.hasDenied()) {
////                AlertDialog.Builder(this)
////                    .setMessage(getString(R.string.grant_permission))
////                    .setPositiveButton(getString(R.string.yes)) { dialog, which ->
////                        e.askAgain()
////                    } //ask again
////                    .setNegativeButton(getString(R.string.no)) { dialog, which ->
////                        dialog.dismiss()
////                    }
////                    .show()
//            }
//
//            if (e.hasForeverDenied()) {
//                e.goToSettings()
//            }
//        }

        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/myfolder/");
        path.mkdirs()
        ImagePicker.create(this)
            .single()
            .returnMode(ReturnMode.ALL)
            .showCamera(true)
            .folderMode(true)
            .imageFullDirectory(path.getAbsolutePath())
            .start()
        
//        ImagePicker.create(this)
//            .returnMode(ReturnMode.ALL)
//            .single()
//            .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val images = ImagePicker.getImages(data)
        val sel = currentSelection
        if (ImagePicker.shouldHandle(requestCode, resultCode, data) && sel != null) {
//            val i = images[0].uri
            val image = ImagePicker.getFirstImageOrNull(data)
            val imageView: ImageView
            when (sel) {
                IdDocumentImageSide.front -> {
                    imageView = frontImageView
                    didSetFrontImage = true
                }
                IdDocumentImageSide.back -> {
                    imageView = backImageView
                    didSetBackImage = true
                }
            }
            
            val uri = URI(image.uri.path)
//            val file = File(uri)

            val pathUtilPath = PathUtil.getPath(requireContext(), image.uri);
            
//            Glide.with(imageView)
            Glide.with(imageView)
                .load(image.uri)
                .into(imageView)
            
            viewModel.uploadIdDocument(pathUtilPath ?: "", sel, requireContext()) {
                print("hey there")
            }
        }
        currentSelection = null
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun saveDocuments() {
//        val newDOB = selectedDate
//        if (newDOB == null) {
//            return
//        }
//        saveButton.isLoading = true
//        viewModel.updateDateOfBirth(newDOB.time, requireContext()) {
//            if (it == null) {
//                requireActivity().runOnUiThread {
//                    finishSuccessfully()
//                }
//            }
//        }
    }

    private fun finishSuccessfully() {
        saveButton.isLoading = false
        findNavController().popBackStack()
        Toast.makeText(
            requireContext(),
            "Car Swaddle successfully saved your identification documents.",
            Toast.LENGTH_SHORT
        ).show()
    }

}


/**
 * Created by Aki on 1/7/2017.
 */
object PathUtil {
    
    /*
     * Gets the file path of the given Uri.
     */
    @SuppressLint("NewApi")
    @Throws(URISyntaxException::class)
    fun getPath(context: Context, uri: Uri): String? {
        var uri = uri
        val needToCheckUri = Build.VERSION.SDK_INT >= 19
        var selection: String? = null
        var selectionArgs: Array<String>? = null
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        // deal with different Uris.
        if (needToCheckUri && DocumentsContract.isDocumentUri(context.applicationContext, uri)) {
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                uri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
                )
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                if ("image" == type) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                selection = "_id=?"
                selectionArgs = arrayOf(split[1])
            }
        }
        if ("content".equals(uri.scheme, ignoreCase = true)) {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            var cursor: Cursor? = null
            try {
                cursor =
                    context.contentResolver.query(uri, projection, selection, selectionArgs, null)
                val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index)
                }
            } catch (e: Exception) {
            }
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }
}
