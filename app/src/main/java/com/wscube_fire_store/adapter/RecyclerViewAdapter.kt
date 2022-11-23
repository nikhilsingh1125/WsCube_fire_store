package com.wscube_fire_store.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.wscube_fire_store.MainActivity
import com.wscube_fire_store.NotesAddActivity
import com.wscube_fire_store.R
import com.wscube_fire_store.model.Notes
import io.grpc.InternalChannelz.id

class RecyclerViewAdapter(val context: Context, val arrList: ArrayList<Notes>) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {


    class ViewHolder(itemView :View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.txtTitle)
        val context = itemView.findViewById<TextView>(R.id.txtContext)
        val edit = itemView.findViewById<ImageView>(R.id.imgEdit)
        val delete = itemView.findViewById<ImageView>(R.id.imgDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_note,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = arrList[position]

        holder.title.text = model.title
        holder.context.text = model.description

        holder.edit.setOnClickListener {
            val intent = Intent(context,NotesAddActivity::class.java)
            intent.putExtra("isEdit","Y")
            intent.putExtra("id",model.id)
            intent.putExtra("title",model.title)
            intent.putExtra("context",model.description)
            context.startActivity(intent)
        }

        holder.delete.setOnClickListener {


            (context as MainActivity).setDelete(position)
        }

    }

    override fun getItemCount(): Int {
        return arrList.size
    }


}