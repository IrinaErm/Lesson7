package com.example.android.lesson7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.os.HandlerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import org.json.JSONArray
import java.io.IOException
import java.io.InputStream
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private var customObjectList = mutableListOf<CustomObject>()
    private lateinit var progressBarLinearLayout: LinearLayout
    private lateinit var recyclerView: RecyclerView

    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    private val mainThreadHandler: Handler = HandlerCompat.createAsync(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        customObjectList = savedInstanceState?.getParcelableArrayList(CUSTOM_OBJECT_LIST_KEY) ?: customObjectList

        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView = findViewById(R.id.recycler_view)
        recyclerViewAdapter = RecyclerViewAdapter(customObjectList)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = recyclerViewAdapter

        progressBarLinearLayout = findViewById(R.id.progress_bar_layout)

        val threadBtn = findViewById<Button>(R.id.thread_btn)
        threadBtn.setOnClickListener {
            showProgressBar()

            val thread = Thread {
                try {
                    Thread.sleep(5000)
                    parseJSON()
                    runOnUiThread {
                        showRecyclerView()
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
            thread.start()
        }

        val executorBtn = findViewById<Button>(R.id.executor_btn)
        executorBtn.setOnClickListener {
            showProgressBar()

            executorService.execute {
                try {
                    Thread.sleep(5000)
                    parseJSON()
                    mainThreadHandler.post {
                        showRecyclerView()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    }

    private fun showProgressBar() {
        progressBarLinearLayout.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        customObjectList.clear()
    }

    private fun showRecyclerView() {
        progressBarLinearLayout.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        recyclerViewAdapter.notifyDataSetChanged()
    }

    private fun parseJSON() {
        val gson = Gson()
        val jsonArray = JSONArray(loadJSON())

        for (i in 0 until jsonArray.length()) {
            customObjectList.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), CustomObject::class.java))
        }
    }

    private fun loadJSON(): String {
        var jsonString = ""
        try {
            val inputStream: InputStream = assets.open(FILE_NAME)
            val buffer = ByteArray(inputStream.available())

            inputStream.read(buffer)
            inputStream.close()

            jsonString = String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.message?.let { Log.e("IOException", it) }
            return jsonString
        }

        return jsonString
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.apply {
            putParcelableArrayList(CUSTOM_OBJECT_LIST_KEY, ArrayList<Parcelable>(customObjectList))
        }
    }

    companion object {
        private const val CUSTOM_OBJECT_LIST_KEY = "CUSTOM_OBJECT_LIST_KEY"
        private const val FILE_NAME = "7_занятие_ДЗ_Многопоточность_Irlix.json"
    }
}