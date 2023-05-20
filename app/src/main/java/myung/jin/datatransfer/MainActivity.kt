package myung.jin.datatransfer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import myung.jin.datatransfer.databinding.ActivityMainBinding
import myung.jin.datatransfer.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
}