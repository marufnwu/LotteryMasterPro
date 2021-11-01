package com.skithub.resultdear.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.skithub.resultdear.databinding.LayoutResultSlotBinding
import com.skithub.resultdear.model.ResultSlot
import com.skithub.resultdear.ui.lottery_result_info.LotteryResultInfoActivity
import com.skithub.resultdear.utils.CommonMethod
import com.skithub.resultdear.utils.Constants
import com.smb.glowbutton.NeonButton
import java.util.*

class ResultSlotAdapter(var resultSlots : MutableList<ResultSlot>, var context: Context) : RecyclerView.Adapter<ResultSlotAdapter.MyViewHolder>() {

    var onButtonClickListener : ((ResultSlot)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutResultSlotBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val resultSlot = resultSlots[position]
        holder.btnResultSlot.text = resultSlot.name!!
        Log.d("Name", resultSlot.name!!)

        holder.btnResultSlot.setOnClickListener{
            onButtonClickListener!!.invoke(resultSlot)
        }

    }

    override fun getItemCount(): Int {
        return  resultSlots.size
    }


    class MyViewHolder(view: LayoutResultSlotBinding) : RecyclerView.ViewHolder(view.root) {
        var btnResultSlot : NeonButton = view.btnResultSlot

    }
}