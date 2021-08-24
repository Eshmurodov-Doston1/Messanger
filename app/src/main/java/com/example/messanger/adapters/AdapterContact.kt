package com.example.messanger.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.messanger.R
import com.example.messanger.databinding.ItemContactBinding
import com.example.messanger.models.Contact

class AdapterContact(var onItemClicListener: OnItemClicListener):ListAdapter<Contact,AdapterContact.Vh>(MyDiffUtill()) {
    inner class Vh(var itemContactBinding:ItemContactBinding):RecyclerView.ViewHolder(itemContactBinding.root){
        fun onBind(contact: Contact,position: Int){
            if (contact.image!=null) {
                itemContactBinding.imageContact.setImageBitmap(contact.image!!)
            }else{
                itemContactBinding.imageContact.setImageResource(R.drawable.ic_user)
            }
            itemContactBinding.nameContact.text =contact.name
            itemContactBinding.phoneNumber.text = contact.phoneNumber
            itemView.setOnClickListener {
                onItemClicListener.onItemClick(contact,position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemContactBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position),position)
    }
    class MyDiffUtill:DiffUtil.ItemCallback<Contact>(){
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem.equals(newItem)
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem.equals(newItem)
        }
    }
    fun filters(list:List<Contact>){
        submitList(list)
        notifyDataSetChanged()
    }

    interface OnItemClicListener{
        fun onItemClick(contact: Contact,position: Int)
    }
}