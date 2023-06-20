package hr.ferit.bojankojcinovic.aplikacijaaa

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
        val password = view.findViewById<EditText>(R.id.editTextPasswordSignUp).text.toString()
        val passwordConfirmation = view.findViewById<EditText>(R.id.editTextPasswordConfirm).text.toString()
        val emailSignup = view.findViewById<EditText>(R.id.editTextEmailSignUp).text.toString()

        finishSignupButton.setOnClickListener{
            if(password == passwordConfirmation){
                activity?.let { currentActivity ->
                    auth.createUserWithEmailAndPassword(emailSignup, password)
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
            }else{
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