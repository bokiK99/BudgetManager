package hr.ferit.bojankojcinovic.budgetmanager

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import hr.ferit.bojankojcinovic.aplikacijaaa.MainFragment
import hr.ferit.bojankojcinovic.aplikacijaaa.R


class SignupFragment : Fragment() {

    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = Firebase.auth
        val view = inflater.inflate(R.layout.fragment_signup, container, false)
        val finishSignupButton = view.findViewById<Button>(R.id.FinishSignupButton)
        val cancelSignupButton = view.findViewById<Button>(R.id.CancelSignupButton)
        val passwordEditText = view.findViewById<EditText>(R.id.editTextPasswordSignUp)
        val passwordConfirmationEditText = view.findViewById<EditText>(R.id.editTextPasswordConfirm)
        val emailEditText = view.findViewById<EditText>(R.id.editTextEmailSignUp)

        finishSignupButton.setOnClickListener{
            if (emailEditText.text.toString() == "" || passwordEditText.text.toString() == "" || passwordConfirmationEditText.text.toString() == "") {
                Toast.makeText(
                    this@SignupFragment.activity,
                    "Prazno polje za unos",
                    Toast.LENGTH_SHORT
                ).show()
            }
            if(passwordEditText.text.toString() == passwordConfirmationEditText.text.toString() && passwordEditText.text.toString() != "" ){
                activity?.let { currentActivity ->
                    auth.createUserWithEmailAndPassword(emailEditText.text.toString(), passwordEditText.text.toString())
                        .addOnCompleteListener(currentActivity) { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "createUserWithEmail:success")
                                val currentUser = auth.currentUser
                                if (currentUser != null) {
                                    goToMain()
                                }

                            } else {
                                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                Toast.makeText(
                                    this@SignupFragment.activity,
                                    "Authentication failed.",
                                    Toast.LENGTH_SHORT,
                                ).show()
                            }
                        }
                }
            }else if( passwordEditText.text.toString() != passwordConfirmationEditText.text.toString()){
                Toast.makeText(this@SignupFragment.activity,
                    "Lozinke se ne poklapaju",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }

        cancelSignupButton.setOnClickListener{
            goToLogin()
        }
        return view
    }
    private fun goToMain() {
        val fragment = MainFragment()

        val fragmentTransaction: FragmentTransaction? =
            activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.fragmentContainerView, fragment)
        fragmentTransaction?.commit()
    }
    private fun goToLogin() {
        val fragment = LoginFragment()

        val fragmentTransaction: FragmentTransaction? =
            activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.fragmentContainerView, fragment)
        fragmentTransaction?.commit()

    }



}