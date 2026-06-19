package com.vehiclemaintenance.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vehiclemaintenance.app.data.models.ServiceLog

class ServiceLogAdapter(
    private var logs: List<ServiceLog>
) : RecyclerView.Adapter<ServiceLogAdapter.ServiceLogViewHolder>() {

    fun updateData(newLogs: List<ServiceLog>) {
        logs = newLogs
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceLogViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_service_log, parent, false)
        return ServiceLogViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceLogViewHolder, position: Int) {
        holder.bind(logs[position])
    }

    override fun getItemCount() = logs.size

    class ServiceLogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val typeText: TextView = itemView.findViewById(R.id.text_service_type)
        private val dateText: TextView = itemView.findViewById(R.id.text_service_date)
        private val notesText: TextView = itemView.findViewById(R.id.text_service_notes)

        fun bind(log: ServiceLog) {
            typeText.text = log.serviceType
            dateText.text = log.dateLogged
            if (!log.notes.isNullOrBlank()) {
                notesText.text = log.notes
                notesText.visibility = View.VISIBLE
            } else {
                notesText.visibility = View.GONE
            }
        }
    }
}
