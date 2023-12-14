package com.example.clock

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


interface OnItemClickListener {
    fun onItemClick(position: Int)
}

class BaoThucAdapter(private var mList: MutableList<BaoThuc>, private val context: Context, private val listener: OnItemClickListener, private val dao: AlarmDao) : RecyclerView.Adapter<BaoThucAdapter.ViewHolder>(){

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_baothuc, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]

        // sets the image to the imageview from our itemHolder class
        //holder.imageView.setImageResource(ItemsViewModel.image)

        // sets the text to the textview from our itemHolder class
        holder.itemView.setOnClickListener {
            listener.onItemClick(position)
        }
        var x = ItemsViewModel.hour.toString()
        var y = ""
        if (ItemsViewModel.min.toString().length == 1) {
            y = "0" + ItemsViewModel.min.toString()
        } else {
            y = ItemsViewModel.min.toString()
        }

        val a = x + ":" + y
        holder.time.text = a

        holder.itemView.findViewById<Button>(R.id.bin).setOnClickListener() {
            mList.removeAt(position)
            CoroutineScope(Dispatchers.IO).launch {
                dao.deleteAlarm(ItemsViewModel) // Gọi phương thức xóa từ DAO
            }
            cancelAlarm(context, position)

            notifyDataSetChanged()
        }

        holder.itemView.findViewById<Button>(R.id.onoff).setOnClickListener() {
            if (mList[position].state == false) {
                cancelAlarm(context, position)
            } else {
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, mList[position].year)
                calendar.set(Calendar.MONTH, mList[position].month)
                calendar.set(Calendar.DAY_OF_MONTH, mList[position].day)
                calendar.set(Calendar.HOUR_OF_DAY, mList[position].hour)
                calendar.set(Calendar.MINUTE, mList[position].min)

                val timeInMillis = calendar.timeInMillis
                setAlarm(context, timeInMillis, position)
            }

        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    fun setAlarm(context: Context, alarmTime: Long, requestCode: Int) {
        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, alarmIntent, 0)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            alarmTime,
            pendingIntent
        )
    }

    fun cancelAlarm(context: Context, requestCode: Int) {
        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, alarmIntent, 0)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val time: TextView = itemView.findViewById(R.id.time)

    }
}
