package com.example.dragreorder

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.recycler_view_holder.view.*

class MainActivity : AppCompatActivity() {

    private val itemTouchHelper by lazy {
        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(UP or DOWN or START or END, 0) {

            override fun onMove(recyclerView: RecyclerView,
                                viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                val adapter = recyclerView.adapter as MainRecyclerViewAdapter
                val from = viewHolder.adapterPosition
                val to = target.adapterPosition
                adapter.moveItem(from, to)
                adapter.notifyItemMoved(from, to)

                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)

                if (actionState == ACTION_STATE_DRAG) {
                    viewHolder?.itemView?.alpha = 0.5f
                }
            }

            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                super.clearView(recyclerView, viewHolder)

                viewHolder.itemView.alpha = 1.0f
            }
        }

        ItemTouchHelper(simpleItemTouchCallback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager
        recyclerView.adapter = MainRecyclerViewAdapter(this)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    fun startDragging(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }
}

class MainRecyclerViewAdapter(private val activity: MainActivity):
    RecyclerView.Adapter<MainRecyclerViewAdapter.MainRecyclerViewHolder>() {
    private var emojis = listOf(
        "Ghost, Confetti",
        "Fireflies, Owl City",
        "We are Young, Fun",
        "21 Guns, Green Day",
        "3 AM, A Finding Hope",
        "Make you mine, Public",
        "Can we  kiss forever, Kina",
        "Mirrors, Justin Timberlake",
        "Dusk Till Dawn, Zyan & Sia",
        "Chlorine, Twenty One Pilots",
        "Beautiful People, Ed Sheeran",
        "Capital Letters, Hailee Steinfeld",
        "Something just like this, Coldplay",
        "Someone you loved, Lewis Capaldi",
        "Don't give up on me, Andy Grammar",
        "What if I told you that I love you, Ali Gatie",
        "I don't wanna live forever, Zyan & Taylor Swift",
        "What goes around... Comes around, Justin Timberlake"
    ).toMutableList()

    fun moveItem(from: Int, to: Int) {
        val fromEmoji = emojis[from]
        emojis.removeAt(from)
        if (to < from) {
            emojis.add(to, fromEmoji)
        } else {
            emojis.add(to - 1, fromEmoji)
        }
    }

    override fun getItemCount(): Int {
        return emojis.size
    }

    override fun onBindViewHolder(holder: MainRecyclerViewHolder, position: Int) {
        val emoji = emojis[position]
        holder.setText(emoji)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRecyclerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_holder, parent, false)
        val viewHolder = MainRecyclerViewHolder(itemView)

        viewHolder.itemView.handleView.setOnTouchListener { _, event ->
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                activity.startDragging(viewHolder)
            }
            return@setOnTouchListener true
        }

        return viewHolder
    }

    class MainRecyclerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun setText(text: String) {
            itemView.textView.text = text
        }
    }
}