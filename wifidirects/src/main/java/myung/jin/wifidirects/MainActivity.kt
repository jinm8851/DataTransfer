package myung.jin.wifidirects

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.registerReceiver
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import myung.jin.wifidirects.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {


    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val adapter = MyFragmentPagerAdapter(this)
        binding.viewpager.adapter = adapter
    }



    // 뷰페이져2 관련 크레스
    class MyFragmentPagerAdapter(activiy: FragmentActivity): FragmentStateAdapter(activiy){
        private val fragment: List<Fragment>
        init {
            fragment = listOf(MainFragment(),TotalFragment(),TransferFragment())
        }

        override fun getItemCount(): Int {
            return fragment.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragment[position]
        }
    }
    // EditText가 아닌 다를 영역을 터치했을 때, 키보드가 내려가게 된다
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken,0)
        return super.dispatchTouchEvent(ev)
    }








}

/*
val manager: WifiP2pManager? by lazy(LazyThreadSafetyMode.NONE) {
    getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager?
}

var mChannel: WifiP2pManager.Channel? = null
var receiver: BroadcastReceiver? = null

var TAG = "wifidirectdemo"
private var isWifiP2pEnabled = false

private var retryChannel = false
var intentFilter = IntentFilter()
    mChannel = manager?.initialize(this, mainLooper, null)
    mChannel?.also { channel ->
        receiver = manager?.let { WiFiDirectBroadcastReceiver(it, channel, this) }
    }

    intentFilter = IntentFilter().apply {
        addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
    }

    manager?.discoverPeers(mChannel, object : WifiP2pManager.ActionListener {

        override fun onSuccess() {
            Log.d (TAG, "search start");
            Toast.makeText(MainActivity.this, "탐색 시작", Toast.LENGTH_LONG).show();
        }

        override fun onFailure(reasonCode: Int) {
            Log.d (TAG, "search failed");
            Toast.makeText(MainActivity.this, "탐색 실패", Toast.LENGTH_LONG).show();
        }
    })

    val device: WifiP2pDevice = ...
    val config = WifiP2pConfig()
    config.deviceAddress = device.deviceAddress
    mChannel?.also { channel ->
        manager?.connect(channel, config, object : WifiP2pManager.ActionListener {

            override fun onSuccess() {
                //success logic
            }

            override fun onFailure(reason: Int) {
                //failure logic
            }
        })
    }


}

*/
/* register the broadcast receiver with the intent values to be matched *//*

*/
/* 일치시킬 의도 값으로 브로드캐스트 수신기 등록 *//*

override fun onResume() {
    super.onResume()
    val mReceiver = null
    mReceiver?.also { receiver:BroadcastReceiver ->
        registerReceiver(receiver, intentFilter)
    }
}

*/
/* unregister the broadcast receiver *//*
 */
/* 브로드캐스트 수신기 등록 취소 *//*

override fun onPause() {
    super.onPause()
    val mReceiver = null
    mReceiver?.also { receiver:BroadcastReceiver ->
        unregisterReceiver(receiver)
    }
}

fun setIsWifiP2pEnabled(isWifiP2pEnabled: Boolean) {
    this.isWifiP2pEnabled = isWifiP2pEnabled
}*/
