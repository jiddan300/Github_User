package com.example.githubuser.acitivityUI

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.api.ItemsItem
import com.example.githubuser.R
import com.example.githubuser.acitivityUI.detail.DetailActivity

class ListUserAdapter(private val listUser: List<ItemsItem>) : RecyclerView.Adapter<ListUserAdapter.ViewHolder>()  {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.row_listuser, viewGroup, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.tvUsername)
            .load(listUser[position].avatarUrl)
            .into(holder.iconUser)
        holder.tvUsername.text = listUser[position].login


        holder.itemView.setOnClickListener{
            val intentDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            intentDetail.putExtra("key_user", listUser[position].login)
            holder.itemView.context.startActivity(intentDetail)
        }
    }

    override fun getItemCount() = listUser.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUsername: TextView = itemView.findViewById(R.id.tv_username)
        val iconUser : ImageView = itemView.findViewById(R.id.photo_icon)
    }
}