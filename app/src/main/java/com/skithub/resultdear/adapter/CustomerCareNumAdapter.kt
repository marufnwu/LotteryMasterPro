package com.skithub.resultdear.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skithub.resultdear.databinding.CustomerNumberItemBinding
import com.skithub.resultdear.model.response.CustomerNumber
import com.skithub.resultdear.model.response.Number

class CustomerCareNumAdapter(val ctx:Context, val contactList : MutableList<Number> ) : RecyclerView.Adapter<CustomerCareNumAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: CustomerNumberItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(customerNumber: Number){

            binding.pnOne.text = customerNumber.number
            binding.PhoneOne.setOnClickListener {

                    val dialIntent = Intent(Intent.ACTION_DIAL)
                    dialIntent.data = Uri.parse("tel:" + customerNumber.number)
                    ctx.startActivity(dialIntent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerCareNumAdapter.ViewHolder {
        return ViewHolder(CustomerNumberItemBinding.inflate(LayoutInflater.from(ctx),  parent, false))
    }

    override fun onBindViewHolder(holder: CustomerCareNumAdapter.ViewHolder, position: Int) {
        holder.bind(contactList[position])
    }

    override fun getItemCount(): Int {
        return contactList.size
    }
}