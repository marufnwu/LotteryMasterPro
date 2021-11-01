package com.skithub.resultdear.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skithub.resultdear.R
import com.skithub.resultdear.databinding.OldResultRecyclerViewModelLayoutBinding
import com.skithub.resultdear.model.LotteryNumberModel
import com.skithub.resultdear.model.LotteryPdfModel
import com.skithub.resultdear.ui.lottery_result_info.LotteryResultInfoActivity
import com.skithub.resultdear.utils.Constants

class OldResultRecyclerAdapter(val context: Context, val list: MutableList<LotteryNumberModel>): RecyclerView.Adapter<OldResultRecyclerAdapter.OldResultRecyclerViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OldResultRecyclerViewHolder {
        val binding=OldResultRecyclerViewModelLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return OldResultRecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OldResultRecyclerViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }


    inner class OldResultRecyclerViewHolder(val binding: OldResultRecyclerViewModelLayoutBinding): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        fun bind(item: LotteryNumberModel) {
            binding.dateTextView.text=item.winDate
            binding.timeTextView.text=item.winTime
            binding.dayNameTextView.text=item.winDayName
            binding.oldResultRootLayout.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            v?.let {
                val oldResultIntent= Intent(context,LotteryResultInfoActivity::class.java)
                when (it.id) {
                    R.id.oldResultRootLayout -> {
                        oldResultIntent.putExtra(Constants.resultTimeKey,list[adapterPosition].winTime)
                        oldResultIntent.putExtra(Constants.resultDateKey,list[adapterPosition].winDate)
                        oldResultIntent.putExtra(Constants.isVersusResultKey,false)
                        oldResultIntent.putExtra(Constants.resultSlotIdKey,list[adapterPosition].SlotId)
                        context.startActivity(oldResultIntent)
                    }
                }
            }
        }
    }



}