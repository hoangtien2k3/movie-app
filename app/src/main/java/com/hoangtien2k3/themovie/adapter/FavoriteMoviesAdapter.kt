package com.hoangtien2k3.themovie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.hoangtien2k3.themovie.databinding.ItemMoviesCommonBinding
import com.hoangtien2k3.themovie.db.MoviesEntity
import com.hoangtien2k3.themovie.utils.Constants.POSTER_BASE_URL
import javax.inject.Inject

class FavoriteMoviesAdapter @Inject constructor() : RecyclerView.Adapter<FavoriteMoviesAdapter.ViewHolder>() {

    private lateinit var binding: ItemMoviesCommonBinding
    private var moviesList = emptyList<MoviesEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemMoviesCommonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(moviesList[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount(): Int = moviesList.size

    inner class ViewHolder() : RecyclerView.ViewHolder(binding.root) {
        fun setData(item: MoviesEntity) {
            binding.apply {
                tvMovieName.text = item.title
                tvRate.text = item.rate
                tvMovieDateRelease.text = item.year
                tvLang.text = item.lang

                val moviePosterURL = POSTER_BASE_URL + item.poster
                imgMovie.load(moviePosterURL) {
                    crossfade(true)
                    crossfade(800)
                    scale(Scale.FIT)
                }

                root.setOnClickListener {
                    onItemClickListener?.let {
                        it(item)
                    }
                }

            }
        }

    }

    private var onItemClickListener : ((MoviesEntity) -> Unit)? = null
    fun setOnItemClickListener(listener: (MoviesEntity) -> Unit) {
        onItemClickListener=listener
    }


    fun bind(data:List<MoviesEntity >){
        val moviesDiffUtils = MoviesDiffUtils(moviesList,data)
        val diffUtils = DiffUtil.calculateDiff(moviesDiffUtils)
        moviesList=data
        diffUtils.dispatchUpdatesTo(this)
    }

    //callback
    class MoviesDiffUtils(private val oldItem:List<MoviesEntity>, private val newItem:List<MoviesEntity>) : DiffUtil.Callback(){
        override fun getOldListSize(): Int {
            return oldItem.size
        }

        override fun getNewListSize(): Int {
            return newItem.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            // === data type is compred here
            return oldItem[oldItemPosition] === newItem[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItem[oldItemPosition] === newItem[newItemPosition]
        }

    }
}