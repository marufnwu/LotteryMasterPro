package com.skithub.resultdear.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skithub.resultdear.R
import com.skithub.resultdear.databinding.DuplicateLotteryNumberRecyclerViewModelLayoutBinding
import com.skithub.resultdear.model.LotteryNumberModel
import com.skithub.resultdear.ui.common_number_details.CommonNumberDetailsActivity
import com.skithub.resultdear.ui.middle_details.MiddleDetailsActivity
import com.skithub.resultdear.ui.middle_details.OneStMiddleDetailsActivity
import com.skithub.resultdear.ui.middle_details.TwoNDmiddleDetailsActivity
import com.skithub.resultdear.ui.middle_number.MiddleNumberActivity
import com.skithub.resultdear.ui.middle_play_less.PlaylessActivity
import com.skithub.resultdear.ui.tow_nd_middle_plays_more.OneStMiddleNumberActivity
import com.skithub.resultdear.ui.tow_nd_middle_plays_more.TwoNdMiddlePlaysMoreActivity
import com.skithub.resultdear.utils.Constants
import com.skithub.resultdear.utils.MyExtensions.shortToast

class DuplicateLotteryNumberRecyclerAdapter(val context: Context, val list: MutableList<LotteryNumberModel>): RecyclerView.Adapter<DuplicateLotteryNumberRecyclerAdapter.DuplicateLotteryNumberRecyclerViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DuplicateLotteryNumberRecyclerViewHolder {
        val binding= DuplicateLotteryNumberRecyclerViewModelLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DuplicateLotteryNumberRecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DuplicateLotteryNumberRecyclerViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }


    inner class DuplicateLotteryNumberRecyclerViewHolder(val binding: DuplicateLotteryNumberRecyclerViewModelLayoutBinding): RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {

        fun bind(item: LotteryNumberModel) {
            try {
                if (context is MiddleNumberActivity) {
                    binding.lotteryNumberTextView.text= item.lotteryNumber
                }else if (context is PlaylessActivity) {
                    binding.lotteryNumberTextView.text= item.lotteryNumber
                }else if (context is TwoNdMiddlePlaysMoreActivity) {
                    binding.lotteryNumberTextView.text= item.lotteryNumber
                }else if (context is OneStMiddleNumberActivity) {
                    binding.lotteryNumberTextView.text= item.lotteryNumber
                } else {
                    binding.lotteryNumberTextView.text="${item.lotterySerialNumber} ${item.lotteryNumber}"
                }
                binding.duplicateLotteryNumberRootLayout.setOnClickListener(this)
            } catch (e: Exception) {}
        }

        override fun onClick(v: View?) {
            v?.let {
                val lotteryIntent: Intent
                if (context is MiddleNumberActivity) {
                    lotteryIntent = Intent(context,MiddleDetailsActivity::class.java)
                }else if(context is PlaylessActivity){
                    lotteryIntent = Intent(context,MiddleDetailsActivity::class.java)
                }else if(context is TwoNdMiddlePlaysMoreActivity){
                    lotteryIntent = Intent(context,TwoNDmiddleDetailsActivity::class.java)
                }else if(context is OneStMiddleNumberActivity){
                    lotteryIntent = Intent(context,OneStMiddleDetailsActivity::class.java)
                }else{
                     lotteryIntent = Intent(context,CommonNumberDetailsActivity::class.java)
                }

                when (it.id) {
                    R.id.duplicateLotteryNumberRootLayout -> {

                        lotteryIntent.putExtra(Constants.lotteryNumberKey,list[adapterPosition].lotteryNumber)
                        context.startActivity(lotteryIntent)

                    }
                }
            }
        }
    }




}