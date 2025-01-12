package hr.ferit.bojankojcinovic.budgetmanager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import hr.ferit.bojankojcinovic.aplikacijaaa.MainFragment
import hr.ferit.bojankojcinovic.aplikacijaaa.R

class EditFragment : Fragment() {
    private val db = Firebase.firestore
    private val auth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit, container, false)

        val saveTransactionButton = view.findViewById<Button>(R.id.SaveTransactionButton2)
        val cancelButton = view.findViewById<Button>(R.id.CancelButton2)
        val radioButtonIncome = view.findViewById<RadioButton>(R.id.radioButtonIncome2)
        val radioButtonExpense = view.findViewById<RadioButton>(R.id.radioButtonExpense2)
        val editTextTransactionName = view.findViewById<EditText>(R.id.EditTextTransactionName2)
        val editTextTransactionAmount = view.findViewById<EditText>(R.id.EditTextTransactionAmount2)
        val editTextTransactionNote = view.findViewById<EditText>(R.id.EditTextTransactionNote2)

        if(arguments?.getString("type").toString() == "Prihod"){
            radioButtonIncome.isChecked = true
        }else if(arguments?.getString("type").toString() == "Trošak"){
            radioButtonExpense.isChecked = true
        }

        editTextTransactionName.setText(arguments?.getString("name").toString())
        editTextTransactionAmount.setText(arguments?.getDouble("amount").toString())
        editTextTransactionNote.setText(arguments?.getString("note").toString())
        var id = arguments?.getString("id").toString()

        saveTransactionButton.setOnClickListener {

            val name = editTextTransactionName.text.toString()
            val amount = editTextTransactionAmount.text.toString().toDouble()
            val note = editTextTransactionNote.text.toString()
            val userId = auth.currentUser?.uid
            var type = ""

            if(radioButtonExpense.isChecked()){
                type = "Trošak"
            }else if(radioButtonIncome.isChecked()){
                type = "Prihod"
            }

            val transaction= Transactions(type, name, amount, note, userId, id)
            db.collection("transactions")
                .document(id)
                .set(transaction)
                .addOnSuccessListener {
                    Toast.makeText(this@EditFragment.activity,"Transakcija uspješno uređena!", Toast.LENGTH_SHORT).show()
                    goBack()
                }
                .addOnFailureListener{
                    Toast.makeText(this@EditFragment.activity,"ERROR!", Toast.LENGTH_SHORT).show()
                }
        }

        cancelButton.setOnClickListener {
            goBack()
        }

        return view
    }
    private fun goBack() {
        val fragment = MainFragment()

        val fragmentTransaction: FragmentTransaction? =
            activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.fragmentContainerView, fragment)
        fragmentTransaction?.commit()
    }
}