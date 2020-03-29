package com.example.movieappmvvmwithpagination.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieappmvvmwithpagination.R
import com.example.movieappmvvmwithpagination.data.api.POSTER_BASE_URL
import com.example.movieappmvvmwithpagination.data.repository.NetworkSate
import com.example.movieappmvvmwithpagination.data.vo.Movie
import com.example.movieappmvvmwithpagination.view.ui.SingleMovieActivity
import kotlinx.android.synthetic.main.item_movie.view.*
import kotlinx.android.synthetic.main.item_network_state.view.*

class PopularMoviePagedListAdapter(val context: Context) :
    PagedListAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffCallback()) {

    val MOVIE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2
    private var networkSate: NetworkSate? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View

        return if (viewType == MOVIE_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.item_movie, parent, false)
            MovieItemViewHolder(view)
        } else {
            view = layoutInflater.inflate(R.layout.item_network_state, parent, false)
            NetworkStateItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == MOVIE_VIEW_TYPE) {
            (holder as MovieItemViewHolder).bind(getItem(position), context)
        } else {
            (holder as NetworkStateItemViewHolder).bind(networkSate)
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkSate != null && networkSate != NetworkSate.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        } else {
            MOVIE_VIEW_TYPE
        }
    }

    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {

        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    class MovieItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(movie: Movie?, context: Context) {
            itemView.cv_movie_title.text = movie?.title
            itemView.cv_movie_release_date.text = movie?.releaseDate

            val moviePosterURL = POSTER_BASE_URL + movie?.posterPath
            Glide.with(itemView.context).load(moviePosterURL).into(itemView.cv_movie_poster)

            itemView.setOnClickListener {
                val intent = Intent(context, SingleMovieActivity::class.java)
                intent.putExtra("id", movie?.id)
                context.startActivity(intent)
            }
        }
    }

    class NetworkStateItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(networkSate: NetworkSate?) {
            // item progressbar
            if (networkSate != null && networkSate == NetworkSate.LOADING) {
                itemView.item_progressbar.visibility = View.VISIBLE
            } else {
                itemView.item_progressbar.visibility = View.GONE
            }
            // item error text
            if (networkSate != null && networkSate == NetworkSate.ERROR) {
                itemView.item_error_text.text = networkSate.msg
                itemView.item_error_text.visibility = View.VISIBLE
            } else if (networkSate != null && networkSate == NetworkSate.END_OF_LIST) {
                itemView.item_error_text.text = networkSate.msg
                itemView.item_error_text.visibility = View.VISIBLE
            } else {
                itemView.item_error_text.visibility = View.GONE
            }
        }
    }

    fun setNetworkState(newNetworkState: NetworkSate) {
        val previousState = networkSate
        val hadExtraRow = hasExtraRow()
        networkSate = newNetworkState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {                          // hadExtraRow is true && hasExtraRow is false
                notifyItemRemoved(super.getItemCount()) // remove the progressbar at the end
            } else {                                    // hadExtraRow is false && hasExtraRow is true
                notifyItemInserted(super.getItemCount()) // add the progressbar at the end
            }
        } else if (hasExtraRow && previousState != networkSate) { // hadExtraRow is true && hasExtraRow is true
            notifyItemChanged(itemCount - 1)
        }
    }
}