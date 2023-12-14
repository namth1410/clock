package com.example.clock

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.clock.databinding.BaoThucBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.*

private lateinit var binding_: BaoThucBinding


class BaoThucFrag : Fragment(R.layout.bao_thuc) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BaoThucAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bao_thuc, container, false)
        binding_ = BaoThucBinding.inflate(layoutInflater)
        recyclerView = view.findViewById(R.id.recyclerView)
        var add = view.findViewById<FloatingActionButton>(R.id.add)

        val appDatabase = Room.databaseBuilder(requireContext(), AppDatabase::class.java, "app-database").build()
        val alarmDao = appDatabase.alarmDao()

        val allAlarms = alarmDao.getAllAlarms()
        allAlarms.observe(requireActivity()) { alarms ->
            list_baothuc.clear()
            for (i in alarms) {
                list_baothuc.add(i)
                recyclerView.adapter?.notifyDataSetChanged()
            }
        }

        add.setOnClickListener() {
            val currentDateTime: LocalDateTime = LocalDateTime.now()
            val materialTimePicker: MaterialTimePicker = MaterialTimePicker.Builder()
                .setTitleText("SELECT YOUR TIMING")
                .setHour(currentDateTime.hour)
                .setMinute(currentDateTime.minute + 1)
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .build()

            materialTimePicker.show(parentFragmentManager, "MainActivity")

            materialTimePicker.addOnPositiveButtonClickListener {

                val pickedHour: Int = materialTimePicker.hour
                val pickedMinute: Int = materialTimePicker.minute

                val formattedTime: String = when {
                    pickedHour > 12 -> {
                        if (pickedMinute < 10) {
                            "${materialTimePicker.hour - 12}:0${materialTimePicker.minute} pm"
                        } else {
                            "${materialTimePicker.hour - 12}:${materialTimePicker.minute} pm"
                        }
                    }
                    pickedHour == 12 -> {
                        if (pickedMinute < 10) {
                            "${materialTimePicker.hour}:0${materialTimePicker.minute} pm"
                        } else {
                            "${materialTimePicker.hour}:${materialTimePicker.minute} pm"
                        }
                    }
                    pickedHour == 0 -> {
                        if (pickedMinute < 10) {
                            "${materialTimePicker.hour + 12}:0${materialTimePicker.minute} am"
                        } else {
                            "${materialTimePicker.hour + 12}:${materialTimePicker.minute} am"
                        }
                    }
                    else -> {
                        if (pickedMinute < 10) {
                            "${materialTimePicker.hour}:0${materialTimePicker.minute} am"
                        } else {
                            "${materialTimePicker.hour}:${materialTimePicker.minute} am"
                        }
                    }
                }


                val hour: Int = pickedHour // Giờ mong muốn
                val minute: Int = pickedMinute // Phút mong muốn

                val newDateTime: LocalDateTime = currentDateTime
                    .withHour(hour)
                    .withMinute(minute)

                var i = BaoThuc(day = newDateTime.dayOfMonth, month = newDateTime.monthValue, year = newDateTime.year, hour = hour, min = minute, state = false)
                list_baothuc.add(i)
                recyclerView.adapter?.notifyDataSetChanged()


                val appDatabase = Room.databaseBuilder(requireActivity().applicationContext, AppDatabase::class.java, "app-database").build()
                val alarmDao = appDatabase.alarmDao()

                GlobalScope.launch(Dispatchers.IO) {
                    // Thực hiện các hoạt động truy vấn cơ sở dữ liệu ở đây
                    alarmDao.insertAlarm(i)
                }

                val calendar: Calendar = Calendar.getInstance().apply {
                    timeInMillis = System.currentTimeMillis()
                    set(Calendar.HOUR_OF_DAY, pickedHour)
                    set(Calendar.MINUTE, pickedMinute)
                }

                val alarmTime = calendar.timeInMillis
                setAlarm(requireContext(), alarmTime, list_baothuc.size - 1)

//                val alarmIntent = Intent(requireContext(), AlarmReceiver::class.java)
//                val pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, alarmIntent, 0)
//
//                // Thiết lập báo thức sử dụng AlarmManager
//                val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as? AlarmManager
//                println(calendar.timeInMillis)
//                alarmManager?.setExact(
//                    AlarmManager.RTC_WAKEUP,
//                    calendar.timeInMillis,
//                    pendingIntent
//                )
            }
        }

        val adapterRecyclerCartAdapter =
            BaoThucAdapter(list_baothuc, requireContext(), object :
                OnItemClickListener {
                override fun onItemClick(position: Int) {

                }
            }, alarmDao)


        recyclerView.layoutManager = LinearLayoutManager(activity)

        val VerticalLayout = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerView.setLayoutManager(VerticalLayout)
        recyclerView.adapter = adapterRecyclerCartAdapter
        recyclerView.adapter?.notifyDataSetChanged()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

//        populateData()
    }

    private fun setupRecyclerView() {
        val appDatabase = Room.databaseBuilder(requireContext(), AppDatabase::class.java, "app-database").build()
        val alarmDao = appDatabase.alarmDao()

        adapter = BaoThucAdapter(list_baothuc, requireContext(), object :
            OnItemClickListener {
            override fun onItemClick(position: Int) {
                // Xử lý sự kiện click ở đây

                val itemView = recyclerView.findViewHolderForAdapterPosition(position)?.itemView

                // Truy cập vào TextView trong item
                val bin = itemView?.findViewById<Button>(R.id.bin)

                // Thực hiện các thay đổi trên TextView
                if (bin?.visibility == View.GONE) {
                    bin?.visibility = View.VISIBLE
                } else {
                    bin?.visibility = View.GONE
                }
                // Thực hiện các thay đổi khác trên các phần tử khác trong item

                recyclerView.adapter?.notifyDataSetChanged()
            }
        }, alarmDao) // Khởi tạo Adapter với danh sách ban đầu là rỗng
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter?.notifyDataSetChanged()
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


}