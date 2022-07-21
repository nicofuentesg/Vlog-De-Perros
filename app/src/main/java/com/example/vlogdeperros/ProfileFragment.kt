package com.example.vlogdeperros

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.vlogdeperros.databinding.ActivityMainBinding
import com.example.vlogdeperros.databinding.FragmentProfileBinding
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(layoutInflater)

        takeValues()

        return binding.root
    }

    private fun takeValues() {
        binding.apply {
            tvNameUser.text = FirebaseAuth.getInstance().currentUser?.displayName
            tvMailUser.text = FirebaseAuth.getInstance().currentUser?.email
            btnLogout.setOnClickListener {
                signOut()
            }
        }
    }

    private fun signOut() {
        context?.let {
            AuthUI.getInstance().signOut(it).addOnSuccessListener {
                Toast.makeText(context,getString(R.string.goodbye),Toast.LENGTH_SHORT).show()
            }
        }
    }

}