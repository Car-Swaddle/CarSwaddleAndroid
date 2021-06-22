package com.carswaddle.carswaddlemechanic.ui.profile.personalInfo.details

import android.content.Intent
import android.os.Bundle
import android.os.Environment
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
import com.carswaddle.foundation.generic.PathUtil
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import java.net.URI


class IdentificationDocumentFragment : Fragment() {

    private lateinit var viewModel: IdentificationDocumentViewModel

    private lateinit var selectFrontProgressButton: ProgressButton
    private lateinit var selectBackProgressButton: ProgressButton

    private lateinit var frontExplainTextView: TextView
    private lateinit var backExplainTextView: TextView

    private lateinit var frontImageView: ImageView
    private lateinit var backImageView: ImageView
    
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

        selectFrontProgressButton = root.findViewById(R.id.selectFrontProgressButton)
        selectBackProgressButton = root.findViewById(R.id.selectBackProgressButton)

        frontExplainTextView = root.findViewById(R.id.frontExplainTextView)
        backExplainTextView = root.findViewById(R.id.backExplainTextView)

        frontImageView = root.findViewById(R.id.frontImageView)
        backImageView = root.findViewById(R.id.backImageView)
        
        selectFrontProgressButton.button.setOnClickListener {
            currentSelection = IdDocumentImageSide.front
            showImagePicker()
        }

        selectBackProgressButton.button.setOnClickListener {
            currentSelection = IdDocumentImageSide.back
            showImagePicker()
        }

        return root
    }

    private fun showImagePicker() {
        ImagePicker.create(this)
            .single()
            .returnMode(ReturnMode.ALL)
            .showCamera(true)
            .theme(R.style.ImagePickerTheme)
            .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val sel = currentSelection
        if (ImagePicker.shouldHandle(requestCode, resultCode, data) && sel != null) {
            val image = ImagePicker.getFirstImageOrNull(data)
            val imageView: ImageView
            when (sel) {
                IdDocumentImageSide.front -> {
                    imageView = frontImageView
                    didSetFrontImage = true
                    selectFrontProgressButton.isLoading = true
                }
                IdDocumentImageSide.back -> { 
                    imageView = backImageView
                    didSetBackImage = true
                    selectBackProgressButton.isLoading = true
                }
            }
            val pathUtilPath = PathUtil.getPath(requireContext(), image.uri)
            Glide.with(imageView)
                .load(image.uri)
                .into(imageView)

            viewModel.uploadIdDocument(pathUtilPath ?: "", sel, requireContext()) {
                requireActivity().runOnUiThread {
                    when (sel) {
                        IdDocumentImageSide.front -> selectFrontProgressButton.isLoading = false
                        IdDocumentImageSide.back -> selectBackProgressButton.isLoading = false
                    }
                    if (it == null) {
                        finishSuccessfully(sel)
                    }
                }
            }
        }
        currentSelection = null
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun finishSuccessfully(side: IdDocumentImageSide) {
        val text: String = when (side) {
            IdDocumentImageSide.front -> "Car Swaddle successfully saved the front of your document."
            IdDocumentImageSide.back -> "Car Swaddle successfully saved the back of your document."
        }
        Toast.makeText(
            requireContext(),
            text,
            Toast.LENGTH_SHORT
        ).show()
    }

}

