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
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.vlogdeperros.databinding.FragmentAddBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private var mPhotoSelectedUri: Uri? = null

    //Creamos las configuraciones iciales para la base de datos
    private lateinit var mStorageReference: StorageReference
    private lateinit var mDatabaseReference: DatabaseReference

    private val PATH_DOG = "dogs"

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

            btnPublic.setOnClickListener {
                uploadPhoto()
            }

        }

        mStorageReference = FirebaseStorage.getInstance().reference
        mDatabaseReference = FirebaseDatabase.getInstance().reference.child(PATH_DOG)
    }

    private fun uploadPhoto() {
        binding.progressBar.visibility = View.VISIBLE
        //Guardamos la foto en la base de datos de firebase
        val storeReference = mStorageReference.child("my_photo")
        val key = mDatabaseReference.push().key!!

        if (mPhotoSelectedUri != null){
            storeReference.putFile(mPhotoSelectedUri!!).addOnProgressListener {
                val progress = (100 * it.bytesTransferred / it.totalByteCount).toDouble()
                binding.progressBar.progress = progress.toInt()
                binding.tvTitle.text = "$progress"
            }
                .addOnCompleteListener {
                    binding.progressBar.visibility = View.GONE
                 }

                .addOnSuccessListener {
                    Toast.makeText(context,getString(R.string.completed_upload), Toast.LENGTH_SHORT).show()
                    it.storage.downloadUrl.addOnSuccessListener {
                    savePhoto(key,it.toString(),binding.etTitle.text.toString().trim())
                    binding.tvTitle.text = getString(R.string.title_public)
                }

            }
                .addOnFailureListener{
                Toast.makeText(context,getString(R.string.fail_upload), Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)

    }

    private fun savePhoto(key:String, url:String, title:String){
        val dog = DogDataClass(title = title, photoUrl = url)
        mDatabaseReference.child(key).setValue(dog)


    }


}