package com.javid.zohotask.ui.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.javid.zohotask.data.model.modelclass.Result
import com.javid.zohotask.databinding.LayoutListItemBinding

class ResultListAdapter(
    private val context: Context,
    private val clickListener: (Result) -> Unit,
    private val addToDatabase: (Result) -> Unit
): PagingDataAdapter<Result, ResultListAdapter.ResultListViewHolder>(COMPARATOR) {

    class ResultListViewHolder(private val binding: LayoutListItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            context: Context,
            result: Result?,
            clickListener: (Result) -> Unit,
            addToDatabase: (Result) -> Unit
        ) {
            binding.tvFirstNameText.text = result?.name?.title
                .plus(". ")
                .plus(result?.name?.first)
                .plus(" ")
                .plus(result?.name?.last)
            binding.tvEmailText.text = result?.email
            binding.tvPhoneText.text = result?.phone

            result?.picture?.let {
                Log.d("Image", "${it.large}")
                Glide.with(context)
                    .load(it.large)
                    .error(com.google.android.material.R.drawable.mtrl_ic_error)
                    .listener(
                        object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding.progressBarImage.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding.progressBarImage.visibility = View.GONE
                                return false
                            }

                        }
                    ).into(binding.ivProfileImage)
            }

            binding.mcViewList.setOnClickListener {
                result?.let { it1 -> clickListener(it1) }
            }

            result?.let { addToDatabase(it) }
        }

    }

    override fun onBindViewHolder(holder: ResultListViewHolder, position: Int) {
        getItem(position).let {
            holder.bind(context, it, clickListener, addToDatabase)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultListViewHolder {
        val binding: LayoutListItemBinding =
            LayoutListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResultListViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Result>() {
            override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
                return oldItem == newItem
            }

        }
    }

}