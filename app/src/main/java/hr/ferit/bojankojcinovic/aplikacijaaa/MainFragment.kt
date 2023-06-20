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

        val detailButton = view.findViewById<Button>(R.id.DetailButton)
        val newTransactionButton = view.findViewById<Button>(R.id.NewTransactionButton)
        val logOutButton = view.findViewById<Button>(R.id.LogOutButton)

        val detailLabel = view.findViewById<TextView>(R.id.textViewEmail)
        detailLabel.text = ("Email: " + auth.currentUser?.email)

        detailButton.setOnClickListener {
            val fragment = DetailFragment()

            val fragmentTransaction: FragmentTransaction? =
                activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainerView, fragment)
            fragmentTransaction?.commit()
        }
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
        var totalIncome = 0.0
        var totalExpense = 0.0
        db.collection("transactions")
            .whereEqualTo("userId", auth.currentUser?.uid)
            .get()
            .addOnSuccessListener { result ->
                val transactionList = ArrayList<Transactions>()
                for (data in result.documents) {
                    val transaction = Transactions()
                    transaction.name = data.data?.get("name").toString()
                    transaction.amount = data.data?.get("amount").toString().toDouble()
                    transaction.note = data.data?.get("note").toString()
                    transaction.userId = data.data?.get("userId").toString()
                    transaction.id = data.id
                    transactionList.add(transaction)
                    if(transaction.amount!! > 0)
                        totalIncome += transaction.amount!!
                    if(transaction.amount!! < 0)
                        totalExpense += transaction.amount!!
                }
                totalIncomeTextView.text = totalIncome.toString()
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

    override fun onItemButtonClick(
        index: Int,
        transaction: Transactions,
        clickType: ItemClickType
    ) {
        TODO("Not yet implemented")
    }
}