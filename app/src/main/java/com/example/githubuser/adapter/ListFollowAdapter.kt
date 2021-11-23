package com.example.githubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuser.databinding.ItemRowUser2Binding
import com.example.githubuser.model.User

class ListFollowAdapter(private val listItem: List<User?>?) :
    RecyclerView.Adapter<ListFollowAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback


    inner class ListViewHolder(private val itemBinding: ItemRowUser2Binding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(user: User?) {
            with(itemBinding) {
                tvItemName.text = user?.login
                Glide.with(imgItemPhoto.context)
                    .load(user?.avatarUrl)
                    .apply(RequestOptions().override(55, 55))
                    .into(imgItemPhoto)
                tvItemDesc.text = user?.htmlUrl
                root.setOnClickListener {
                    onItemClickCallback.onItemClicked(user)
                }
            }
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListFollowAdapter.ListViewHolder {
        val itemBinding =
            ItemRowUser2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ListFollowAdapter.ListViewHolder, position: Int) {
        val user: User? = listItem?.get(position)
        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return listItem?.size ?: 0
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(user: User?)
    }
}