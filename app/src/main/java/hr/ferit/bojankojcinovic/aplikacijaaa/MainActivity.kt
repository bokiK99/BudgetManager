package hr.ferit.bojankojcinovic.aplikacijaaa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(),
    TransactionRecyclerAdapter.ContentListener {
    private val db = Firebase.firestore
    private lateinit var recyclerAdapter: TransactionRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
    private fun setupFrags(){
        val transaction = supportFragmentManager.beginTransaction()
        var detailFragment = DetailFragment()
        var mainFragment = MainFragment()
        var transactionFragment = TransactionFragment()

        transaction.replace(R.id.fragmentContainerView, mainFragment)
        transaction.commit()
    }

    override fun onItemButtonClick(index: Int, transaction: Transactions, clickType:
    ItemClickType) {
        if (clickType == ItemClickType.EDIT) {
            db.collection("persons")
                .document(transaction.id)
                .set(transaction)
        }
        else if (clickType == ItemClickType.REMOVE) {
            recyclerAdapter.removeItem(index)
            db.collection("persons")
                .document(transaction.id)
                .delete()
        }
    }


}

