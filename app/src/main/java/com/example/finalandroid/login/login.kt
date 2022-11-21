package com.example.finalandroid.login

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.finalandroid.MainActivity
import com.example.finalandroid.R

import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.dialog_progress.*
import kotlinx.android.synthetic.main.fragment_login.*

class login : Fragment() {

    private lateinit var loadingProgressBar: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPassFocus()
        userNameFocus()
        btn_login.setOnClickListener {

            checkUser()
        }
        tv_forgot_password.setOnClickListener {
            val action = loginDirections.actionLoginToResetPass()

            findNavController().navigate(action)
        }

        tv_regigster.setOnClickListener {

            val action = loginDirections.actionLoginToRegister(false)
            findNavController().navigate(action)
        }
        tv_offline_mode.setOnClickListener{


            val action = loginDirections.actionLoginToRegister(true)
            findNavController().navigate(action)
        }

    }

    private fun checkUser() {

        val useremail = ed_email.text.toString().trim { it <= ' ' }
        val userpass = ed_pass.text.toString().trim { it <= ' ' }

        if (useremail.isNotEmpty() && userpass.isNotEmpty()) {
            showProgress("Please Waite")
            FirebaseAuth.getInstance().signInWithEmailAndPassword(useremail, userpass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        hideProgress()
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        intent.putExtra("UID", FirebaseAuth.getInstance().currentUser?.uid)
                        startActivity(intent)
                        requireActivity().finish()
                        // startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        hideProgress()
                        tv_warning.text = task.exception?.message.toString()
                    }

                }
        } else {

            username()
            userpass()
        }
    }


    fun userNameFocus() {

        ed_email.setOnFocusChangeListener { _, focus ->
            if (!focus) {
                login_email.helperText = validUserName()
                tv_warning.text = null
            }

        }

    }

    fun validUserName(): String? {

        if (ed_email.text!!.isEmpty()) {

            return "Empty Email"
        }

        return null
    }

    fun userPassFocus() {

        ed_pass.setOnFocusChangeListener { _, focus ->
            if (!focus) {
                login_pass.helperText = validPass()
                tv_warning.text = null
            }

        }
    }

    fun validPass(): String? {

        if (ed_pass.text!!.isEmpty()) {

            return "Empty Pass"
        }

        return null
    }


    fun username() {

        if (ed_email.text!!.isEmpty()) {

            login_email.helperText = validUserName()
        }

    }

    fun userpass() {

        if (ed_pass.text!!.isEmpty()) {

            login_pass.helperText = validPass()
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