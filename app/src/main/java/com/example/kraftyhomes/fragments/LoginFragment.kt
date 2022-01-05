package com.example.kraftyhomes.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.kraftyhomes.R
import com.example.kraftyhomes.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class LoginFragment : Fragment() {
    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var myAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.signUp.setOnClickListener {
            binding.signUp.background = ResourcesCompat.getDrawable(resources, R.drawable.switch_tracks, null)
            binding.signUp.setTextColor(resources.getColor(R.color.text_color, null))
            binding.login.background = null
            binding.signUpLayout.visibility = View.VISIBLE
            binding.loginLayout.visibility = View.GONE
            binding.login.setTextColor(resources.getColor(R.color.pink_color, null))
        }

        binding.login.setOnClickListener {
            binding.signUp.background = null
            binding.signUp.setTextColor(resources.getColor(R.color.pink_color, null))
            binding.login.background = ResourcesCompat.getDrawable(resources, R.drawable.switch_tracks, null)
            binding.signUpLayout.visibility = View.GONE
            binding.loginLayout.visibility = View.VISIBLE
            binding.login.setTextColor(resources.getColor(R.color.text_color, null))
        }

        binding.signInBtn.setOnClickListener {

        }

        binding.googleIcon.setOnClickListener {
            signIn()
        }

        myAuth = FirebaseAuth.getInstance()
        val user = myAuth.currentUser

        Handler().postDelayed({
            if (user != null){
//                val dashboardIntent = Intent(requireActivity(), DashboardActivity::class.java)
//                startActivity(dashboardIntent)
                replaceFragment(DashboardFragment())
            }
        }, 2000)

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        return root
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful){
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("LoginFragment", "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("LoginFragment", "Google sign in failed", e)
                }
            } else{
                Log.w("LoginFragment", exception.toString())

            }

        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        myAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("LoginFragment", "signInWithCredential:success")
//                    val intent = Intent(requireContext(), DashboardActivity::class.java)
//                    startActivity(intent)
                    replaceFragment(DashboardFragment())
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("LoginFragment", "signInWithCredential:failure", task.exception)
//                    updateUI(null)
                }
            }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.fragment_container, fragment)
        fragmentTransaction?.commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val RC_SIGN_IN = 100
    }
}