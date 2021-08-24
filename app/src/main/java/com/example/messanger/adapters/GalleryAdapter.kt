package com.example.messanger.adapters

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.messanger.databinding.GalleryItemBinding
import com.example.messanger.models.Image
import com.squareup.picasso.Picasso
import it.emperor.animatedcheckbox.binding.setChecked

class GalleryAdapter(var onItemClickListener: OnItemClickListener,var context: Context,var activity:Activity):ListAdapter<Image,GalleryAdapter.Vh>(MyDiffUtill()) {
    lateinit var sharedPreferences: SharedPreferences
    inner class Vh(var galleryItemBinding: GalleryItemBinding):RecyclerView.ViewHolder(galleryItemBinding.root){
        fun onBind(image: Image,position: Int){
            sharedPreferences = activity.getSharedPreferences("send",0)
            val position1 = sharedPreferences.getInt("position", -1)
            Glide.with(context).load(image.image).apply(RequestOptions().centerCrop()).into(galleryItemBinding.image)
            if (position1!=-1 && position1==position){
                galleryItemBinding.animatedcheckbox.setChecked(true)
            }
            itemView.setOnClickListener {
                onItemClickListener.onItemClick(image,position,galleryItemBinding.animatedcheckbox.isChecked())
            }
            galleryItemBinding.animatedcheckbox.setOnChangeListener {
               onItemClickListener.onCheckClick(image,position,it)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(GalleryItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position),position)
    }

    class MyDiffUtill:DiffUtil.ItemCallback<Image>(){
        override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem.equals(newItem)
        }

        override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem.equals(newItem)
        }
    }

    interface OnItemClickListener{
        fun onItemClick(image: Image,position: Int,isChecked:Boolean)
        fun onCheckClick(image: Image,position: Int,isChecked:Boolean)
    }

}