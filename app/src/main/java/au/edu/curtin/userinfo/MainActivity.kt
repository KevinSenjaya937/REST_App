package au.edu.curtin.userinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private var executorService = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val backGroundTaskHandler = BackGroundTaskHandler(this)
        executorService.execute(backGroundTaskHandler)
    }
}