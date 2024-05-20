package com.example.quizapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapplication.databinding.HistoryItemBinding
import com.example.quizapplication.model.historyModel
import java.sql.Date
import java.sql.Timestamp

class HistoryAdapter(var historyList: ArrayList<historyModel>) :
    RecyclerView.Adapter<HistoryAdapter.MyHistoryViewHolder>() {

    class MyHistoryViewHolder(var binding: HistoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHistoryViewHolder {
        return MyHistoryViewHolder(
            HistoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = historyList.size

    override fun onBindViewHolder(holder: MyHistoryViewHolder, position: Int) {

        var listPosition = historyList[position]
        var timeStampe = Timestamp(listPosition.timeAndDate.toLong())
        holder.binding.date.text = Date(timeStampe.time).toString()
        holder.binding.Time.text =  if(listPosition.isWithDrawal){"- Money Withdrawal" }else{"+ Money Add"}
        holder.binding.coin.text = listPosition.timeAndDate

    }
}