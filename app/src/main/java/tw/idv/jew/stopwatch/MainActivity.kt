package tw.idv.jew.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer
import tw.idv.jew.stopwatch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    lateinit var stopwatch: Chronometer //馬錶
    var running = false //馬錶是否正在執行？
    var offset: Long = 0 //馬錶暫停或重新啟動的offset

    //加入Bundle的索引鍵String
    val OFFSET_KEY = "offset"
    val RUNNING_KEY = "running"
    val BASE_KEY = "base"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //取得馬錶的參考
        stopwatch = findViewById<Chronometer>(R.id.stopwatch)

        //恢復之前的狀態
        if (savedInstanceState != null) {
            offset = savedInstanceState.getLong(OFFSET_KEY)
            running = savedInstanceState.getBoolean(RUNNING_KEY)
            if (running) {
                stopwatch.base = savedInstanceState.getLong(BASE_KEY)
                stopwatch.start()
            } else setBaseTime()
        }

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

    override fun onPause() {
        super.onPause()

        if (running) {
            saveOffset()
            stopwatch.stop()
        }
    }

    override fun onResume() {
        super.onResume()

        if (running) {
            setBaseTime()
            stopwatch.start()
            offset = 0
        }
    }

    //用onSaveInstanceState方法來儲存offset、running與stopwatch.base屬性
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putLong(OFFSET_KEY, offset)
        savedInstanceState.putBoolean(RUNNING_KEY, running)
        savedInstanceState.putLong(BASE_KEY, stopwatch.base)
        super.onSaveInstanceState(savedInstanceState)
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