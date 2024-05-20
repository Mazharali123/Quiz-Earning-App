package com.example.quizapplication.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapplication.QuizActivity
import com.example.quizapplication.databinding.CardItemBinding
import com.example.quizapplication.model.categoryImage

class categoryAdapter(
    var categoryList: ArrayList<categoryImage>,
    var requireActivity: FragmentActivity
) : RecyclerView.Adapter<categoryAdapter.MycategoryViewHolder>() {

    class MycategoryViewHolder(var binding: CardItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MycategoryViewHolder {
        return MycategoryViewHolder(
            CardItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = categoryList.size

    override fun onBindViewHolder(holder: MycategoryViewHolder, position: Int) {

        var datalist = categoryList[position]
        holder.binding.cardImage.setImageResource(datalist.catImage)
        holder.binding.categoryText.setText(datalist.catText)
        holder.binding.categoryBtn.setOnClickListener {

            var intent = Intent(requireActivity, QuizActivity::class.java)
            intent.putExtra("category_image", datalist.catImage)
            intent.putExtra("questionType",datalist.catText)
            requireActivity.startActivity(intent)

        }
    }

}