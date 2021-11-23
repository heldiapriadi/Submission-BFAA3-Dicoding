package com.example.githubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuser.databinding.ItemRowUserBinding
import com.example.githubuser.model.User

class UserAdapter(private val listUser: ArrayList<User?>?) :
    RecyclerView.Adapter<UserAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback


    inner class ListViewHolder(private val itemBinding: ItemRowUserBinding) :
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
    ): UserAdapter.ListViewHolder {
        val itemBinding =
            ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: UserAdapter.ListViewHolder, position: Int) {
        val user: User? = listUser?.get(position)
        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return listUser?.size ?: 0
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun updateData(newList: ArrayList<User?>?) {
        listUser?.clear()
        newList?.let {
            listUser?.addAll(it)
        }
        notifyDataSetChanged()
    }

    interface OnItemClickCallback {
        fun onItemClicked(user: User?)
    }

}