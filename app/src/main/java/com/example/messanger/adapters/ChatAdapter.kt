package com.example.messanger.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.messanger.databinding.FromItemBinding
import com.example.messanger.databinding.ToItemBinding
import com.example.messanger.models.Message
import com.example.messanger.models.User

class ChatAdapter(var list: List<Message>,var uid:String):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class FromVh(var fromItemBinding: FromItemBinding):RecyclerView.ViewHolder(fromItemBinding.root){
        fun onBindFrom(message: Message,position: Int){
            fromItemBinding.fromText.text = message.message
            fromItemBinding.time.text = message.timeStamp?.substring(message.timeStamp?.lastIndexOf(" ")!!)
            itemView.setOnLongClickListener {

                true
            }
        }
    }
    inner class ToVh(var toItemBinding: ToItemBinding):RecyclerView.ViewHolder(toItemBinding.root){
        fun onBindTo(message: Message,position: Int){
            toItemBinding.toText.text = message.message
            toItemBinding.time.text = message.timeStamp?.substring(message.timeStamp?.lastIndexOf(" ")!!)
            itemView.setOnLongClickListener {
                true
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
       if (list[position].idMessage==uid){
           return 1
       }
        return 2

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType==1){
            return FromVh(FromItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }else{
            return ToVh(ToItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position)==1){
            val fromVh = holder as FromVh
            fromVh.onBindFrom(list[position],position)
        }else{
            val toVh = holder as ToVh
            toVh.onBindTo(list[position],position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}