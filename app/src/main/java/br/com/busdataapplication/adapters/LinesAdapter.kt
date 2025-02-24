package br.com.busdataapplication.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.busdataapplication.R
import br.com.busdataapplication.models.BusLine

class LinesAdapter(
    private val lines: ArrayList<BusLine>,
    private val onItemClickListener: AdapterView.OnItemClickListener? = null
) : RecyclerView.Adapter<LinesAdapter.ViewHolder>() {

    companion object {

        // Aleatory Method Result
        fun getLineColor(numSign: Int?): Int {
            return when (numSign) {
                10 -> Color.BLUE
                51 -> Color.GREEN
                11 -> Color.rgb(255, 165, 0)
                21 -> Color.YELLOW
                else -> Color.GRAY
            }
        }

        fun getOriginToDestinationBySense(line: BusLine): String {
            val sense = line.sense
            val main = line.mainToSecondary
            val secondary = line.secondaryToMain
            if (sense != null && sense == 2) return "$secondary - $main"
            return "$main - $secondary"
        }

        fun getNumericSign(line: BusLine): String {
            return "${line.firstNumericSign}-${line.secondNumericSign}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bus_line, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val line = lines[position]
        holder.color.setBackgroundColor(getLineColor(line.secondNumericSign))
        holder.destinationCode.text = getNumericSign(line)
        holder.destinationName.text = getOriginToDestinationBySense(line)
    }

    override fun getItemCount(): Int {
        return lines.size
    }

    fun getItem(position: Int): BusLine {
        return lines[position]
    }

    fun updateLines(lines: List<BusLine>) {
        this.lines.clear()
        this.lines.addAll(lines)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val color = itemView.findViewById<View>(R.id.bus_line_color) as View
        val destinationCode = itemView.findViewById<View>(R.id.bus_destination_code) as TextView
        val destinationName = itemView.findViewById<View>(R.id.destination) as TextView

        init {
            itemView.setOnClickListener {
                onItemClickListener?.onItemClick(null, it, adapterPosition, 0)
            }
        }
    }

}