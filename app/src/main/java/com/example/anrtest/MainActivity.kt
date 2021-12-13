package com.example.anrtest

import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import okhttp3.*
import java.io.IOException
import java.time.LocalDateTime


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ポートレートに固定
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    }

    fun onClickExecButton(view: android.view.View) {
        val start = System.currentTimeMillis()
        // 通信3回
        for (i in 1..3) {
            callAPI()
        }

        var num : Int = findViewById<EditText>(R.id.editTextNumber).text.toString().toIntOrNull()?:0

        Log.d("num", num.toString())
        for (i in 1..num * 50) {
            Thread.sleep(3)
            getRandomString()
        }
        val end = System.currentTimeMillis()
        findViewById<TextView>(R.id.textView).text = "process time(m) ${end - start}"

    }

    private fun getRandomString() : String {
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..1000)
            .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("");

    }

    // https://www.jma.go.jp/bosai/quake/data/list.json
    // 気象庁の地震データを全部持ってくる
    private fun callAPI() {
        val client: OkHttpClient = OkHttpClient()
        val url:String = "https://www.jma.go.jp/bosai/quake/data/list.json"
        val request: Request = Request.Builder().url(url).build()
        val call: Call = client.newCall(request)
        /* メインスレッド同期呼び出し */
        /*
        val response = call.execute()
        Log.d("res:", response!!.body!!.string())

         */
        /* 非同期 */
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("res:", "API call error")
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("res:", response!!.body!!.string())
            }
            })


    }

    fun onClickSecondButton(view: android.view.View) {
        var num : Int = findViewById<TextView>(R.id.textView2).text.toString().toIntOrNull()?:0
        Log.d("num2", num.toString())
        num += 1
        findViewById<TextView>(R.id.textView2).text = num.toString()
    }
}