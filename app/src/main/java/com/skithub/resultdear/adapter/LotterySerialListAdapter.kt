package com.skithub.resultdear.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skithub.resultdear.R
import com.skithub.resultdear.databinding.LotterySerialItemBinding
import com.skithub.resultdear.model.LotteryNumberModel
import com.skithub.resultdear.model.LotterySerialModel

class LotterySerialListAdapter(var ctx:Context,var numberList: MutableList<LotterySerialModel>) : RecyclerView.Adapter<LotterySerialListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LotterySerialListAdapter.ViewHolder {
        return ViewHolder(LotterySerialItemBinding.inflate(LayoutInflater.from(ctx), parent, false))
    }

    override fun onBindViewHolder(holder: LotterySerialListAdapter.ViewHolder, position: Int) {
        holder.bind(numberList[position])
    }

    override fun getItemCount(): Int {
       return numberList.size
    }

    inner class ViewHolder(val binding: LotterySerialItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(num:LotterySerialModel){
            binding.txtNumber.text = num.lotteryNumber
            if(num.select){
                binding.duplicateLotteryNumberRootLayout.setBackgroundResource(R.drawable.select_item_bg)
            }else{
                binding.duplicateLotteryNumberRootLayout.setBackgroundResource(R.drawable.rounded_transparent_bg)
            }
        }
    }

}