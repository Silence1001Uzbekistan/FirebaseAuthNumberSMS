package com.example.sha1sinov

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import com.example.sha1sinov.databinding.ActivitySecondBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class SecondActivity : AppCompatActivity() {

    lateinit var binding: ActivitySecondBinding

    private lateinit var auth: FirebaseAuth
    private var timer: CountDownTimer? = null

    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseApp.initializeApp(this)

        auth = Firebase.auth

        loadTimer()



/*        binding.nextThirdActivity.setOnClickListener {

            verifyPhoneNumberWithCode(storedVerificationId, binding.inputEt.text.toString())


        }*/



        binding.nextThirdActivity.setOnClickListener {

            val numberOne = intent.extras?.getString("one")

            val intent = Intent(this, ThirdActivity::class.java)
            intent.putExtra("two", numberOne)
            startActivity(intent)

        }



        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {

                signInWithPhoneAuthCredential(p0)

            }

            override fun onVerificationFailed(p0: FirebaseException) {

                if (p0 is FirebaseAuthInvalidCredentialsException) {

                } else if (p0 is FirebaseTooManyRequestsException) {

                }

            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)

                storedVerificationId = p0
                resendToken = p1

            }

        }

    }


    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        updateUI(currentUser)


        val numberOne = intent.extras?.getString("one")

        binding.text1.text = "Bir martalik kod  $numberOne raqamiga yuborildi"

        startPhoneNumberVerification(numberOne.toString())

    }

    private fun startPhoneNumberVerification(phoneNumber: String) {

        val options = PhoneAuthOptions.newBuilder(auth).setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS).setActivity(this).setCallbacks(callbacks).build()

        PhoneAuthProvider.verifyPhoneNumber(options)

    }

    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {

        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)

        signInWithPhoneAuthCredential(credential)

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {

        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->

            if (task.isSuccessful) {

                val user = task.result?.user

                Toast.makeText(this, "Succes", Toast.LENGTH_SHORT).show()

                val numberOne = intent.extras?.getString("one")

                val intent = Intent(this, ThirdActivity::class.java)
                intent.putExtra("two", numberOne)
                startActivity(intent)

            } else {

                if (task.exception is FirebaseAuthInvalidCredentialsException) {

                }

            }

        }

    }

    private fun updateUI(user: FirebaseUser? = auth.currentUser) {

        //do something

    }

    companion object {

        private const val TAG = "MainActivity"

    }

    private fun loadTimer() {

        timer = object : CountDownTimer(90000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                if (millisUntilFinished / 1000 < 10) {
                    binding.timer.text = "00:0" + millisUntilFinished / 1000
                } else {
                    binding.timer.text = "00:" + millisUntilFinished / 1000
                }
            }

            override fun onFinish() {
                binding.timer.text = "00:00"
                binding.inputEt.isEnabled = false

            }
        }
        timer?.start()
    }

}
