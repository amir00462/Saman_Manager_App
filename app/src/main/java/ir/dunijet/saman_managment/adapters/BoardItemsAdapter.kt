package ir.dunijet.saman_managment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ir.dunijet.saman_managment.R
import ir.dunijet.saman_managment.me.MyTask
import kotlinx.android.synthetic.main.item_board.view.*

open class BoardItemsAdapter(
    private val context: Context,
    private var list: ArrayList<MyTask.Task>,
    private val eventListener: TaskEvent? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_board,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val model = list[position]

        val listOfPics = listOf(
            R.drawable.ic_avatar1,
            R.drawable.ic_avatar2,
            R.drawable.ic_avatar4,
            R.drawable.ic_avatar5,
            R.drawable.ic_avatar3
        )

        Glide
            .with(context)
            .load(listOfPics[(0..4).random()])
            .centerCrop()
            .placeholder(R.drawable.ic_board_place_holder)
            .into(holder.itemView.iv_board_image)

        holder.itemView.tv_name.text = model.name
        holder.itemView.tv_created_by.text = "Subject: ${model.subject}"

        holder.itemView.setOnClickListener {
            eventListener?.onClicked(position, model)
        }

        holder.itemView.setOnLongClickListener {
            eventListener?.onLongClicked(position, model)
            true
        }


    }

    fun removeItem(model: MyTask.Task, position: Int) {

        list.remove(model)
        notifyItemRemoved(position)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface TaskEvent {
        fun onClicked(position: Int, model: MyTask.Task)
        fun onLongClicked(position: Int, model: MyTask.Task)
    }

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}