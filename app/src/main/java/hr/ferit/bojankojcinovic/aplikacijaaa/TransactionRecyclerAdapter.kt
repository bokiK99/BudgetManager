package hr.ferit.bojankojcinovic.aplikacijaaa

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

enum class ItemClickType {
    EDIT,
    REMOVE,
}

class TransactionRecyclerAdapter (
    val items: ArrayList<Transactions>,
    val listener: ContentListener
    ): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            RecyclerView.ViewHolder {
        return TransactionsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.recycler_item,
                parent, false
            )
        )
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TransactionsViewHolder -> {
                holder.bind(position ,listener, items[position])
            }
        }
    }
    override fun getItemCount(): Int {
        return items.size
    }

    fun removeItem(index: Int) {
        items.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, items.size)
    }

    class TransactionsViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        private val transactionName =
            view.findViewById<TextView>(R.id.transactionName)
        private val transactionAmount =
            view.findViewById<TextView>(R.id.transactionAmount)
        private val transactionNotice =
            view.findViewById<TextView>(R.id.transactionNotice)
        private val editBtn =
            view.findViewById<ImageButton>(R.id.editButton)
        private val deleteBtn =
            view.findViewById<ImageButton>(R.id.deleteButton)
        fun bind(
            index: Int,
            listener: ContentListener,
            transaction: Transactions
        ) {
            transactionName.setText(transaction.name)
            transactionNotice.setText(transaction.note)
            transactionAmount.setText(transaction.amount.toString())


            editBtn.setOnClickListener {
                transaction.name = transactionName.text.toString()
                transaction.note = transactionNotice.text.toString()
                listener.onItemButtonClick(index, transaction,
                    ItemClickType.EDIT)
            }
            deleteBtn.setOnClickListener {
                listener.onItemButtonClick(index, transaction,
                    ItemClickType.REMOVE)
            }
        }
    }
    interface ContentListener {
        fun onItemButtonClick(index: Int, transaction: Transactions, clickType:
        ItemClickType)
    }
}

