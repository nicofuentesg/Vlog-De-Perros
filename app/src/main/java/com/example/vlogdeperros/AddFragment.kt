package com.example.vlogdeperros

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.example.vlogdeperros.databinding.FragmentAddBinding


class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private var mPhotoSelectedUri: Uri? = null

    private val galleryLauncher =   registerForActivityResult(ActivityResultContracts.StartActivityForResult()){activityResult ->

        if (activityResult.resultCode == RESULT_OK){

            mPhotoSelectedUri = activityResult.data?.data
            binding.apply {
                tiTitle.visibility = View.VISIBLE
                ivDogUpload.setImageURI(mPhotoSelectedUri)
                btnImage.visibility = View.INVISIBLE
            }

        }


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAddBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnImage.setOnClickListener {
                openGallery()
            }



        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)

    }


}