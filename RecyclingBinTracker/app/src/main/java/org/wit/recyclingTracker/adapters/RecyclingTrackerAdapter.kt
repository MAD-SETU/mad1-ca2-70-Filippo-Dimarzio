package org.wit.recyclingTracker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.wit.recyclingTracker.databinding.CardRecyclingTrackerBinding
import org.wit.recyclingTracker.models.RecyclingTrackerModel


interface RecyclingTrackerListener{
    fun onRecyclingTrackerClick(recycling: RecyclingTrackerModel)
}
class RecyclingTrackerAdapter(private var recyclingTrackers: List<RecyclingTrackerModel>,
                              private val listener: RecyclingTrackerListener) :
    RecyclerView.Adapter<RecyclingTrackerAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardRecyclingTrackerBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val recyclingTracker = recyclingTrackers[holder.adapterPosition]
        holder.bind(recyclingTracker, listener)
    }

    override fun getItemCount(): Int = recyclingTrackers.size

    class MainHolder(private val binding : CardRecyclingTrackerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recyclingTracker: RecyclingTrackerModel, listener: RecyclingTrackerListener) {
            binding.recyclingTrackerTitle.text = recyclingTracker.title
            binding.description.text = recyclingTracker.description
            binding.dateAdded.text = recyclingTracker.date
            Picasso.get().load(recyclingTracker.image).resize(200,200).into(binding.recyclingTrackerImage)
            binding.root.setOnClickListener { listener.onRecyclingTrackerClick(recyclingTracker) }
        }
    }
}
