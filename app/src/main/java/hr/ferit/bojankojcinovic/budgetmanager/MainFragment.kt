package hr.ferit.bojankojcinovic.aplikacijaaa

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import hr.ferit.bojankojcinovic.budgetmanager.*

class MainFragment : Fragment(), TransactionRecyclerAdapter.ContentListener {
    private var auth = Firebase.auth
    private val db = Firebase.firestore
    private lateinit var recyclerAdapter: TransactionRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        val newTransactionButton = view.findViewById<Button>(R.id.NewTransactionButton)
        val logOutButton = view.findViewById<Button>(R.id.LogOutButton)

        val detailLabel = view.findViewById<TextView>(R.id.textViewEmail)
        detailLabel.text = ("Email: " + auth.currentUser?.email)


        newTransactionButton.setOnClickListener {
            val fragment = TransactionFragment()

            val fragmentTransaction: FragmentTransaction? =
                activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainerView, fragment)
            fragmentTransaction?.commit()
        }
        logOutButton.setOnClickListener {
            auth.signOut()
            val fragment = LoginFragment()

            val fragmentTransaction: FragmentTransaction? =
                activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainerView, fragment)
            fragmentTransaction?.commit()
        }

        loadData(view)
        return view
    }

    fun loadData(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.transactionList)
        val totalIncomeTextView = view.findViewById<TextView>(R.id.textViewTotalIncome)
        val totalExpensesTextView = view.findViewById<TextView>(R.id.textViewTotalExpenses)
        var totalIncome = 0.0
        var totalExpense = 0.0
        db.collection("transactions")
            .whereEqualTo("userId", auth.currentUser?.uid)
            .get()
            .addOnSuccessListener { result ->
                val transactionList = ArrayList<Transactions>()
                for (data in result.documents) {
                    val transaction = Transactions()
                    transaction.type = data.data?.get("type").toString()
                    transaction.name = data.data?.get("name").toString()
                    transaction.amount = data.data?.get("amount").toString().toDouble()
                    transaction.note = data.data?.get("note").toString()
                    transaction.userId = data.data?.get("userId").toString()
                    transaction.id = data.id
                    transactionList.add(transaction)
                    if(transaction.amount!! > 0 && transaction.type == "Prihod")
                        totalIncome += transaction.amount!!
                    if(transaction.amount!! > 0 && transaction.type == "Trošak")
                        totalExpense += transaction.amount!!
                }
                totalIncomeTextView.text = "Ukupni prihodi: " + totalIncome.toString() + "€"
                totalExpensesTextView.text = "Ukupni troškovi: " + totalExpense.toString() + "€"
                recyclerAdapter = TransactionRecyclerAdapter(transactionList,
                    this@MainFragment)
                recyclerView.apply {
                    layoutManager = LinearLayoutManager(this@MainFragment.context)
                    adapter = recyclerAdapter
                }
            }

            .addOnFailureListener { exception ->
                Log.w("MainActivity", "Error getting documents.",
                    exception)
            }

    }

    override fun onItemButtonClick( index: Int, transaction: Transactions, clickType: ItemClickType) {
        if (clickType == ItemClickType.REMOVE) {
            recyclerAdapter.removeItem(index)
            db.collection("transactions")
                .document(transaction.id)
                .delete()
            view?.let { loadData(it) }
        }
        else if (clickType == ItemClickType.EDIT) {
            val bundle = Bundle()
            val fragment = EditFragment()

            db.collection("transactions")
                .document(transaction.id)
                .get()
            bundle.putString("id", transaction.id)
            bundle.putString("type", transaction.type)
            bundle.putString("name", transaction.name)
            transaction.amount?.let { bundle.putDouble("amount", it) }
            bundle.putString("note", transaction.note)
            fragment.arguments = bundle
            val fragmentTransaction: FragmentTransaction? =
                activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainerView, fragment)
            fragmentTransaction?.commit()
        }
    }
}