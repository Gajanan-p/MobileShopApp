package com.samprama.mobileapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.samprama.mobileapp.R
import com.samprama.mobileapp.dialog.ActionButtonClickListener
import com.samprama.mobileapp.viewmodel.RegistrationModel

class TransactionsAdapter(
    // on below line we are passing variables as course list and context
    private var dataList: ArrayList<RegistrationModel>,
    private val listener: ActionButtonClickListener):
    RecyclerView.Adapter<TransactionsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int):
            TransactionsAdapter.ViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.row_trasaction_list_latout,
            parent, false
        )

        // at last we are returning our view holder
        // class with our item View File.
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // on below line we are setting data to our text view and our image view.
        val data = dataList[position]
        holder.customerNameTextView.text=data.customerName
        holder.totalCostTextView.text= "\u20B9"+data.cost
        holder.date.text = data.date
        holder.depositTextView.text = "\u20B9"+data.deposit
        holder.pendingTextView.text = "\u20B9"+data.pending
        holder.customerNameTextView.setOnClickListener(View.OnClickListener {
            listener.onItemClick(data,position)
        })
    }

    override fun getItemCount(): Int {

        return dataList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // on below line we are initializing our course name text view and our image view.
        val customerNameTextView: TextView = itemView.findViewById(R.id.text_row_transaction_customer_nm)
        val totalCostTextView: TextView = itemView.findViewById(R.id.text_row_transaction_total_amount)
        val date : TextView = itemView.findViewById(R.id.text_row_transaction_date)
        val depositTextView: TextView = itemView.findViewById(R.id.text_row_transaction_deposit)
        val pendingTextView: TextView = itemView.findViewById(R.id.text_row_transaction_pending)
    }

}