package com.samprama.mobileapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.samprama.mobileapp.R
import com.samprama.mobileapp.dialog.ActionButtonClickListener
import com.samprama.mobileapp.viewmodel.RegistrationModel

class SearchListItemAdapter(
    private val items: ArrayList<RegistrationModel>,
    private val listener: ActionButtonClickListener) :
    RecyclerView.Adapter<SearchListItemAdapter.ViewHolder>(), Filterable {

    private var filteredItems = ArrayList<RegistrationModel>()

    init {
        filteredItems.addAll(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_customer_list_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = filteredItems[position]
        holder.mobileCompanyNameTextView.text = data.mobileCompanyName
        holder.modelNumberTextView.text = data.modelNumber
        holder.customerNameTextView.text=data.customerName
        holder.mobileNoTextView.text = data.customerMobileNo
        holder.villageTextView.text = data.villageName
        holder.problemTextView.text= data.problem
        holder.totalCostTextView.text= "\u20B9"+data.cost
        holder.depositTextView.text="\u20B9"+data.deposit
        holder.pendingTextView.text="\u20B9"+data.pending
        holder.date.text = data.date
        holder.customerNameTextView.setOnClickListener(View.OnClickListener {
            listener.onItemClick(data,position)
        })
    }

    override fun getItemCount(): Int {
        return filteredItems.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mobileCompanyNameTextView: TextView = itemView.findViewById(R.id.text_row_mobile_comp_nm)
        val modelNumberTextView: TextView = itemView.findViewById(R.id.text_row_model_no)
        val customerNameTextView: TextView = itemView.findViewById(R.id.text_row_customer_nm)
        val mobileNoTextView: TextView = itemView.findViewById(R.id.text_row_mobile_no)
        val villageTextView: TextView = itemView.findViewById(R.id.text_row_village)
        val problemTextView: TextView = itemView.findViewById(R.id.text_row_problem)
        val totalCostTextView: TextView = itemView.findViewById(R.id.text_row_cost)
        val depositTextView:TextView = itemView.findViewById(R.id.text_row_deposit)
        val pendingTextView:TextView = itemView.findViewById(R.id.text_row_pending)
        val date : TextView = itemView.findViewById(R.id.text_row_date)

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val queryString = constraint?.toString()?.toLowerCase()
                val filterResults = FilterResults()
                val filteredList = ArrayList<RegistrationModel>()

                if (queryString.isNullOrEmpty()) {
                    filteredList.addAll(items)
                } else {
                    for (item in items) {
                        if (item.customerMobileNo.toString().contains(queryString) || item.customerName.toLowerCase().contains(queryString)) {
                            filteredList.add(item)
                        }
                    }
                }

                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredItems.clear()
                filteredItems.addAll(results?.values as ArrayList<RegistrationModel>)
                notifyDataSetChanged()
            }
        }
    }
}