package com.example.messanger.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.messanger.databinding.ItemUserRvBinding
import com.example.messanger.models.User
import com.squareup.picasso.Picasso

class AdapterUser(var onItemClickListener: OnItemClickListener):ListAdapter<User,AdapterUser.Vh>(MyDiffUtill()) {
    inner class Vh(var itemUserRvBinding: ItemUserRvBinding):RecyclerView.ViewHolder(itemUserRvBinding.root){
        fun onBind(user: User,position: Int){
            Picasso.get().load(user.userImage).into(itemUserRvBinding.image)
            itemUserRvBinding.nameUser.text = "${user.name} ${user.surname}"

            itemView.setOnClickListener {
                onItemClickListener.itemClick(user,position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):Vh {
        return Vh(ItemUserRvBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position),position)
    }

    class MyDiffUtill:DiffUtil.ItemCallback<User>(){
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.equals(newItem)
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.equals(newItem)
        }

    }
    interface OnItemClickListener{
        fun itemClick(user: User,position: Int)
    }
}