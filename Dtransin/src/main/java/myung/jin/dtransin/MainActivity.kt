package myung.jin.dtransin


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.net.DatagramPacket
import java.net.DatagramSocket


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun receiveBikeMemosOverWifi() {
        // 데이터 수신을 비동기적으로 처리
        GlobalScope.launch(Dispatchers.IO) {
            // DatagramSocket을 사용하여 UDP 패킷을 수신
            val socket = DatagramSocket(8888)
            val receiveData = ByteArray(1024)
            val receivePacket = DatagramPacket(receiveData, receiveData.size)
            socket.receive(receivePacket)
            socket.close()

            // 수신한 데이터를 문자열로 변환
            val receivedData = String(receivePacket.data, 0, receivePacket.length)

            // 직렬화된 문자열을 BikeMemo 리스트로 변환
            val bikeMemos = deserializeBikeMemos(receivedData)

            // Room 데이터베이스에 BikeMemo 리스트를 저장
            saveBikeMemosToRoomDatabase(bikeMemos)
        }
    }

    fun deserializeBikeMemos(data: String): List<BikeMemo> {
        // 문자열을 BikeMemo 리스트로 역직렬화하는 작업 수행
        // 여기에서는 예시로 JSON 형식을 사용하겠습니다.
        // 실제로는 Gson, Jackson 등의 라이브러리를 사용하는 것이 좋습니다.
        val bikeMemos = mutableListOf<BikeMemo>()
        val jsonArray = JSONArray(data)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val bikeMemo = BikeMemo(
                jsonObject.getString("model"),
                jsonObject.getString("purchaseDate"),
                jsonObject.getString("date"),
                jsonObject.getInt("km"),
                jsonObject.getString("refer"),
                jsonObject.getInt("amount"),
                jsonObject.getString("note"),
                jsonObject.getString("year")
            )
            bikeMemos.add(bikeMemo)
        }
        return bikeMemos
    }

    fun saveBikeMemosToRoomDatabase(bikeMemos: List<BikeMemo>) {
        // Room 데이터베이스에 BikeMemo 리스트 저장하는 작업 수행
        // ...
    }
}