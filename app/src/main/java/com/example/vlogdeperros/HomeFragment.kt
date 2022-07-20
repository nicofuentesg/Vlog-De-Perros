package com.example.vlogdeperros

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.vlogdeperros.databinding.FragmentHomeBinding
import com.example.vlogdeperros.databinding.ItemDogBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get () = _binding!!

    private lateinit var mFirebaseAdapter: FirebaseRecyclerAdapter <DogDataClass,DogViewHolder>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager

    private lateinit var mContext: Context




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(layoutInflater)
        //Creamos la consulta
        iniciarConsulta()
        iniciarAdapter()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        mFirebaseAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        mFirebaseAdapter.stopListening()
    }


    private fun iniciarConsulta() {


        val query = FirebaseDatabase.getInstance().reference.child("dogs")

        val options = FirebaseRecyclerOptions.Builder<DogDataClass>()
            .setQuery(query,DogDataClass::class.java)
            .build()


        mFirebaseAdapter = object : FirebaseRecyclerAdapter<DogDataClass,DogViewHolder>(options){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {

                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dog, parent, false)
                return DogViewHolder(view)
            }

            override fun onBindViewHolder(holder: DogViewHolder, position: Int, model: DogDataClass) {
                val item = getItem(position)
                holder.iniciar(item)
            }


            override fun onDataChanged() {
                super.onDataChanged()
                binding.progressBar.visibility = View.GONE
            }

            override fun onError(error: DatabaseError) {
                super.onError(error)
                Toast.makeText(parentFragment!!.context, error.message , Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun iniciarAdapter() {
        mLayoutManager = LinearLayoutManager(context)

        binding.recyclerViewDog.apply {
            setHasFixedSize(true)
            layoutManager = mLayoutManager
            adapter = mFirebaseAdapter
        }

    }


    inner class DogViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val binding = ItemDogBinding.bind(view)

        fun iniciar(dog: DogDataClass){

            binding.tvNameDog.text = dog.title
            Glide.with(binding.ivDog.context)
                .load(dog.photoUrl).
                diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(binding.ivDog)

        }
    }



}