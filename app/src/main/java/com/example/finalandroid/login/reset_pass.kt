package com.example.finalandroid.login

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.finalandroid.R
import com.google.firebase.auth.FirebaseAuth
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.android.synthetic.main.dialog_progress.*
import kotlinx.android.synthetic.main.fragment_reset_pass.*

class reset_pass : Fragment() {

    private lateinit var loadingProgressBar: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reset_pass, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_reset_pass.setOnClickListener {
            resetEmail()
        }

    }

    private fun resetEmail() {
        if (validateEmail()) {
            showProgress("Please Waite")

            val email = ed_reset_email.text.toString().trim { it <= ' ' }

            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener {

                    task ->
                if (task.isSuccessful) {
                    hideProgress()

                    val action = reset_passDirections.actionResetPassToLogin()

                    findNavController().navigate(action)
                    FancyToast.makeText(
                        requireContext(),
                        "Reset email sent",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.SUCCESS,
                        false
                    ).show()

                } else {
                    hideProgress()

                    tv_reset_error.text = task.exception!!.message.toString()
                }
            }
        } else {
            userEmailFocus()
        }
    }

    private fun validateEmail(): Boolean {

        if (ed_reset_email.text!!.isNotEmpty()) {

            return true
        }
        return false
    }


    fun userEmailFocus() {

        if (ed_reset_email.text!!.isEmpty()) {

            reset_email.helperText = validUserEmail()
        }

    }

    fun validUserEmail(): String? {

        if (ed_reset_email.text!!.isEmpty()) {

            return "Empty Email"
        }
        return null
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