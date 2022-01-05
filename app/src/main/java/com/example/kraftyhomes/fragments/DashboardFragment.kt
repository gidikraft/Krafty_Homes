package com.example.kraftyhomes.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.kraftyhomes.MainActivity
import com.example.kraftyhomes.R
import com.example.kraftyhomes.databinding.FragmentDashboardBinding
import com.google.firebase.auth.FirebaseAuth


class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        if (currentUser != null){
            binding.userIdExample.text = currentUser.uid
            binding.userNameExample.text = currentUser.displayName
            binding.userEmailExample.text = currentUser.email

            Glide.with(this).load("http://goo.gl/gEgYUd").into(binding.userImage)
        }

        binding.signOutBtn.setOnClickListener {
            mAuth.signOut()
            replaceFragment(LoginFragment())
        }
        return root
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.fragment_container, fragment)
        fragmentTransaction?.commit()
    }
}