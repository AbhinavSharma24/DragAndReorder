package com.example.dragreorder

import android.annotation.SuppressLint
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.recycler_view_holder.view.*
import java.io.IOException

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

        Toast.makeText(this, "Long press item to play the sample song!", Toast.LENGTH_SHORT).show()

    }

    fun startDragging(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }
}

@Suppress("DEPRECATION", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MainRecyclerViewAdapter(private val activity: MainActivity):
    RecyclerView.Adapter<MainRecyclerViewAdapter.MainRecyclerViewHolder>() {
    private var songs = listOf(
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
        val fromEmoji = songs[from]
        songs.removeAt(from)
        if (to < from) {
            songs.add(to, fromEmoji)
        } else {
            songs.add(to - 1, fromEmoji)
        }
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    override fun onBindViewHolder(holder: MainRecyclerViewHolder, position: Int) {
        val song = songs[position]
        holder.setText(song)

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

            itemView.textView.setOnLongClickListener {
                /*val url = "https://drive.google.com/file/d/0B1HQtcOuMBlnUGFfQUt0VzBSRWM"     //your URL here
                var mediaPlayer: MediaPlayer? = MediaPlayer().apply {
                    setAudioStreamType(AudioManager.STREAM_MUSIC)
                    try {
                        setDataSource(url)
                        prepare()   //might take long! (for buffering, etc)
                        start()
                    }catch (e : IOException){
                        Log.d("SongCheck", e.message)
                        Toast.makeText(itemView.context, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
                mediaPlayer?.release()
                mediaPlayer = null*/

                val mediaPlayer: MediaPlayer? = MediaPlayer.create(itemView.context, R.raw.yehbaby)
                mediaPlayer?.start()
                Toast.makeText(itemView.context, "Song playing, increase the volume!", Toast.LENGTH_SHORT).show()

                return@setOnLongClickListener false
            }
        }
    }
}