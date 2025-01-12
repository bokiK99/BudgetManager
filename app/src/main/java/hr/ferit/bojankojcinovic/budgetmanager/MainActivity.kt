package hr.ferit.bojankojcinovic.aplikacijaaa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import hr.ferit.bojankojcinovic.budgetmanager.*

class MainActivity : AppCompatActivity(),
    TransactionRecyclerAdapter.ContentListener {
    private val db = Firebase.firestore
    private lateinit var recyclerAdapter: TransactionRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
    override fun onItemButtonClick(index: Int, transaction: Transactions, clickType:
    ItemClickType
    ) {}


}

