package com.example.finalandroid.login

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.finalandroid.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.dialog_progress.*
import kotlinx.android.synthetic.main.fragment_register.*


class register : Fragment() {


    private lateinit var loadingProgressBar: Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_register.setOnClickListener {

            registerUser()

        }
    }

    private fun registerUser() {

        if (valiteUser()) {

            val email = ed_reg_email.text.toString().trim { it <= ' ' }
            val password = ed_reg_pass.text.toString().trim { it <= ' ' }
            val userName = ed_reg_user_name.text.toString().trim { it <= ' ' }
            val phone = ed_reg_phone.text.toString().trim { it <= ' ' }

            val user = hashMapOf(

                "Name" to userName,
                "Email" to email,
                "Phone" to phone
            )

            val db = Firebase.firestore
            val Users = db.collection("Users")

            showProgress("Please Waite")
            val query = Users.whereEqualTo("Email", email).get().addOnSuccessListener {

                    query ->
                if (query.isEmpty) {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener {

                                task ->
                            if (task.isSuccessful) {
                                hideProgress()
                                val sharedPreferences =
                                    requireActivity().getSharedPreferences(
                                        "name",
                                        Context.MODE_PRIVATE
                                    )
                                val editor = sharedPreferences.edit()
                                editor.apply {

                                    putString("name_key", userName)
                                }.apply()


                                val firebaseUser: FirebaseUser = task.result!!.user!!

                                Users.document(firebaseUser.uid).set(user)
                                Toast.makeText(
                                    requireContext(),
                                    "Account Created",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val action = registerDirections.actionRegisterToLogin()
                                findNavController().navigate(action)
                            } else {
                                hideProgress()

                                Log.i("FIREBASE", task.exception!!.message.toString());
                                tv_reg_warning.text = task.exception?.message.toString()
                            }
                        }

                } else {
                    hideProgress()
                    tv_reg_warning.text = "Email Existed"

                }

            }
        }
    }

    private fun valiteUser(): Boolean {


        return when {

            TextUtils.isEmpty(ed_reg_email.text.toString().trim { it <= ' ' }) -> {

                reg_email.helperText = "Empty Email"
                false
            }
            TextUtils.isEmpty(ed_reg_user_name.text.toString().trim { it <= ' ' }) -> {

                reg_user_name.helperText = "Empty User name"
                false
            }
            TextUtils.isEmpty(ed_reg_pass.text.toString().trim { it <= ' ' }) -> {

                reg_pass.helperText = "Empty Password"
                false
            }
            TextUtils.isEmpty(ed_reg_phone.text.toString().trim { it <= ' ' }) -> {

                reg_phone.helperText = "Empty Phone Number"
                false
            }

            else -> {

                true
            }
        }
    }


    fun showProgress(text: String) {

        loadingProgressBar = Dialog(requireContext())

        loadingProgressBar.setContentView(R.layout.dialog_progress)

        loadingProgressBar.window?.setBackgroundDrawableResource(R.drawable.bg_white_rounded)


        loadingProgressBar.tv_loading.text = text

        loadingProgressBar.setCancelable(false)

        loadingProgressBar.setCanceledOnTouchOutside(false)

        loadingProgressBar.show()
    }

    fun hideProgress() {
        loadingProgressBar.dismiss()

    }
}