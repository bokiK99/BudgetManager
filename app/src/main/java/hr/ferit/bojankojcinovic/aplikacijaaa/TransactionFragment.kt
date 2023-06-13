package hr.ferit.bojankojcinovic.aplikacijaaa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class TransactionFragment : Fragment() {

    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private lateinit var recyclerAdapter: TransactionRecyclerAdapter

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflate the layout for this fragment
            val view = inflater.inflate(R.layout.fragment_transactionfrag, container, false)

            val saveTransactionButton = view.findViewById<Button>(R.id.SaveTransactionButton)
            val cancelButton = view.findViewById<Button>(R.id.CancelButton)

            saveTransactionButton.setOnClickListener {
                val name = view.findViewById<EditText>(R.id.TextInputName).text.toString()
                val amount = view.findViewById<EditText>(R.id.TextInputAmount).text.toString().toDouble()
                val note = view.findViewById<EditText>(R.id.TextInputNote).text.toString()
                val userId = auth.currentUser?.uid
                val transaction=Transactions(name, amount, note, userId)
                db.collection("transactions")
                    .add(transaction)
                    .addOnSuccessListener {
                        Toast.makeText(this@TransactionFragment.activity,"Person successfully added!", Toast.LENGTH_SHORT).show()
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



