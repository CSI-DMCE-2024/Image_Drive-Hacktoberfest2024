package com.example.material.adapters

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.material.R
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.example.material.adapters.photos

class RecycleAdapter(val array: ArrayList<Any>) : RecyclerView.Adapter<RecycleAdapter.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_IMAGE = 0
        const val VIEW_TYPE_VIDEO = 1
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.image)
        val playerView: PlayerView = view.findViewById(R.id.video_view)
        val downloadButton: Button = view.findViewById(R.id.button)
        val deleteButton: ImageView = view.findViewById(R.id.delete)
    }

    override fun getItemCount(): Int {
        return array.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (array[position] is Video) {
            true -> VIEW_TYPE_VIDEO
            false -> VIEW_TYPE_IMAGE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutId = if (viewType == VIEW_TYPE_VIDEO) {
            R.layout.video_item // New layout file for video items
        } else {
            R.layout.gallery // Existing layout for images
        }
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (val item = array[position]) {
            is photos -> {
                // Handle displaying images
                Picasso.get()
                    .load(item.photourl)
                    .resize(500, 900)
                    .centerCrop()
                    .into(holder.imageView)

                holder.downloadButton.setOnClickListener {
                    // Download image functionality
                }

                holder.deleteButton.setOnClickListener {
                    // Delete image functionality
                }

                holder.playerView.visibility = View.GONE
            }
            is Video -> {
                // Handle displaying videos
                holder.playerView.visibility = View.VISIBLE
                val player = SimpleExoPlayer.Builder(holder.playerView.context).build()
                holder.playerView.player = player
                val mediaItem = MediaItem.fromUri(item.videoUrl)
                player.setMediaItem(mediaItem)
                player.prepare()
                player.playWhenReady = false // You can change this as per your requirement

                holder.downloadButton.setOnClickListener {
                    // Download video functionality
                }

                holder.deleteButton.setOnClickListener {
                    // Delete video functionality
                }
            }
        }
    }
}