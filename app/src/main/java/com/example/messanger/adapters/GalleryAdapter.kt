package com.example.messanger.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.messanger.databinding.GalleryItemBinding
import com.example.messanger.models.Image
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.*

class GalleryAdapter(var onItemClickListener: OnItemClickListener,var context: Context,var activity:Activity):ListAdapter<Image,GalleryAdapter.Vh>(MyDiffUtill()) {
    inner class Vh(var galleryItemBinding: GalleryItemBinding):RecyclerView.ViewHolder(galleryItemBinding.root){
        fun onBind(image: Image,position: Int){

            Glide.with(context).load(image.image).apply(RequestOptions().centerCrop()).into(galleryItemBinding.image)

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