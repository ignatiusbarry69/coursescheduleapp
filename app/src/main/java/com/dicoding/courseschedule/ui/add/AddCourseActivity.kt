package com.dicoding.courseschedule.ui.add

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.util.TimePickerFragment
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class AddCourseActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener, TimePickerFragment.DialogTimeListener {
    private lateinit var viewModel: AddCourseViewModel
    private var selectedDay: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)

        viewModel = ViewModelProvider(this, AddCourseViewModel.AddCourseViewModelFactory(this))[AddCourseViewModel::class.java]

        findViewById<ImageButton>(R.id.btn_start_time).setOnClickListener {
            val datePickerFragment = TimePickerFragment()
            datePickerFragment.show(supportFragmentManager, TIME_START)
        }

        findViewById<ImageButton>(R.id.btn_end_time).setOnClickListener {
            val datePickerFragment = TimePickerFragment()
            datePickerFragment.show(supportFragmentManager, TIME_END)
        }

        val spinner: Spinner = findViewById(R.id.spinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.day,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = this

        viewModel.saved.observe(this){
            val handled = it.getContentIfNotHandled()
            if(handled != null){
                if(handled){
                    finish()
                } else {
                    Toast.makeText(this, "Fill the empty field", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        selectedDay = pos
    }

    override fun onNothingSelected(parent: AdapterView<*>) {

    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)

        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        when (tag) {
            TIME_START -> findViewById<TextView>(R.id.tv_start_time).text = dateFormat.format(calendar.time)
            TIME_END -> findViewById<TextView>(R.id.tv_end_time).text = dateFormat.format(calendar.time)
            else -> {
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_insert -> {
                val title = findViewById<TextInputEditText>(R.id.ed_course_name).text.toString()
                val lecturer = findViewById<TextInputEditText>(R.id.ed_lecturer).text.toString()
                val note = findViewById<TextInputEditText>(R.id.ed_note).text.toString()
                val startTime = findViewById<TextView>(R.id.tv_start_time).text.toString()
                val endTime = findViewById<TextView>(R.id.tv_end_time).text.toString()
                viewModel.insertCourse(
                    courseName = title,
                    day = selectedDay,
                    startTime = startTime,
                    endTime = endTime,
                    lecturer = lecturer,
                    note = note
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val TIME_START = "start"
        private const val TIME_END = "end"
    }
}