package com.fattyleo.mykotlinapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fattyleo.mykotlinapp.data.room.UserInfoEntity
import com.fattyleo.mykotlinapp.databinding.RecyclerviewItemBinding

class UserInfoAdapter : ListAdapter<UserInfoEntity, UserInfoAdapter.UserInfoViewHolder>(UserInfoComparator()) {

    var itemClickListener: (UserInfoEntity) -> Unit = {}
    lateinit var binding : RecyclerviewItemBinding

    inner class UserInfoViewHolder(private val binding: RecyclerviewItemBinding)  : RecyclerView.ViewHolder(binding.root){

        init {
            binding.deleteIcon.setOnClickListener {
                itemClickListener(getItem(layoutPosition))
            }
        }

        fun bind(userInfoEntity: UserInfoEntity) {
            binding.itemName.text = userInfoEntity.name
            binding.itemEmail.text = userInfoEntity.email
        }
    }


    class UserInfoComparator : DiffUtil.ItemCallback<UserInfoEntity>() {
        override fun areItemsTheSame(oldItem: UserInfoEntity, newItem: UserInfoEntity): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: UserInfoEntity, newItem: UserInfoEntity): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserInfoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = RecyclerviewItemBinding.inflate(inflater, parent, false)
        return UserInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserInfoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}