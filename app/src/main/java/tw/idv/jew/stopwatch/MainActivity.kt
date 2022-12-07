package tw.idv.jew.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer

class MainActivity : AppCompatActivity() {

    lateinit var stopwatch: Chronometer //馬錶
    var running = false //馬錶是否正在執行？
    var offset: Long = 0 //馬錶暫停或重新啟動的offset

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //取得馬錶的參考
        stopwatch = findViewById<Chronometer>(R.id.stopwatch)
        //用start按鈕啟動馬錶，如果它還沒有開始執行的話
        val startButton = findViewById<Button>(R.id.start_button)
        startButton.setOnClickListener {
            if (!running) {
                setBaseTime()
                stopwatch.start()
                running = true
            }
        }

        //pause按鈕會在馬錶正在執行時暫停
        val pauseButton = findViewById<Button>(R.id.pause_button)
        pauseButton.setOnClickListener {
            if (running) {
                saveOffset()
                stopwatch.stop()
                running = false
            }
        }

        //用reset按鈕來將offset與馬錶設為0
        val resetButton = findViewById<Button>(R.id.reset_button)
        resetButton.setOnClickListener {
            if (running) {  //修正書本版的重置按鈕bug
                stopwatch.stop()
                running = false
            }
            offset = 0
            setBaseTime()
        }
    }

    //更改stopwatch.base時間，允許任何offset
    fun setBaseTime() {
        stopwatch.base = SystemClock.elapsedRealtime() - offset
    }

    //紀錄offset
    fun saveOffset() {
        offset = SystemClock.elapsedRealtime() - stopwatch.base
    }
}