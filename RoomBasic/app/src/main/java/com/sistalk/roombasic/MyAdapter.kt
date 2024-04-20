package com.sistalk.roombasic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class MyAdapter : Adapter<MyAdapter.MyViewHolder>() {

    private var allWords: List<Word> = ArrayList()


    fun setAllWords(words: List<Word>) {
        allWords = words
    }

    class MyViewHolder(itemView: View) : ViewHolder(itemView) {
        var textViewNumber: TextView = itemView.findViewById(R.id.textViewNumber)
        var textviewEnglish: TextView = itemView.findViewById(R.id.textViewEnglish)
        var textViewChinese: TextView = itemView.findViewById(R.id.textViewChinese)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        /// 加载xml layout
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.cell_card,parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return allWords.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val word = allWords[position]
        holder.textViewNumber.text = String.format("%d",position)
        holder.textviewEnglish.text = word.word
        holder.textViewChinese.text = word.chineseMeaning
    }
}