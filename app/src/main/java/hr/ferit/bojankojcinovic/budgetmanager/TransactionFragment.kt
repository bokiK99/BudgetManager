package hr.ferit.bojankojcinovic.aplikacijaaa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import hr.ferit.bojankojcinovic.budgetmanager.Transactions


class TransactionFragment : Fragment() {

    private val db = Firebase.firestore
    private val auth = Firebase.auth


        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflate the layout for this fragment
            val view = inflater.inflate(R.layout.fragment_new_transaction, container, false)

            val saveTransactionButton = view.findViewById<Button>(R.id.SaveTransactionButton)
            val cancelButton = view.findViewById<Button>(R.id.CancelButton)
            val radioButtonIncome = view.findViewById<RadioButton>(R.id.radioButtonIncome)
            val radioButtonExpense = view.findViewById<RadioButton>(R.id.radioButtonExpense)
            val editTextTransactionName = view.findViewById<EditText>(R.id.EditTextTransactionName)
            val editTextTransactionAmount = view.findViewById<EditText>(R.id.EditTextTransactionAmount)
            val editTextTransactionNote = view.findViewById<EditText>(R.id.EditTextTransactionNote)

            saveTransactionButton.setOnClickListener {

                val name = editTextTransactionName.text.toString()
                val amount = editTextTransactionAmount.text.toString().toDouble()
                val note = editTextTransactionNote.text.toString()
                val userId = auth.currentUser?.uid
                var type = ""
                var id = ""


                if(radioButtonExpense.isChecked()){
                    type = "Trošak"
                }else if(radioButtonIncome.isChecked()){
                    type = "Prihod"
                }

                val transaction= Transactions(type, name, amount, note, userId)
                db.collection("transactions")
                    .add(transaction)
                    .addOnSuccessListener { result -> id = result.id
                        db.collection("transactions").document(id).update("id", id)
                        Toast.makeText(this@TransactionFragment.activity,"Transakcija uspješno dodana!", Toast.LENGTH_SHORT).show()
                        goBack()
                    }
                    .addOnFailureListener{
                        Toast.makeText(this@TransactionFragment.activity,"ERROR!", Toast.LENGTH_SHORT).show()
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



