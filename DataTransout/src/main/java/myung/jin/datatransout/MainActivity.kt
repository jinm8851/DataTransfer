package myung.jin.datatransout

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope

import kotlinx.coroutines.launch
import myung.jin.datatransout.databinding.ActivityMainBinding
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream
import java.io.OutputStream
import java.lang.Exception
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.NetworkInterface
import java.net.ServerSocket
import java.net.Socket


class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var helper: RoomHelper
    private lateinit var bikeMemoDao: BikeMemoDao
    private val bikeListMS = mutableListOf<BikeMemo>()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        //헬퍼에 바이크메모 적용
        helper = Room.databaseBuilder(this, RoomHelper::class.java, "bike_memo")
            .fallbackToDestructiveMigration()
            .build()
        bikeMemoDao = helper.bikeMemoDao()

        checkMemo()


      //  val isConnected = isWifiConnected(applicationContext)




// 사용 예시



        binding.sendbtn.setOnClickListener {
                    bikeListMS.clear()
                    bikeListMS.addAll(bikeMemoDao.getAll())
                    sendBikeMemosOverWifi(bikeListMS) //127.0.0.1 컴터ip
                    bikeListMS.clear()
        }

        binding.receivebtn.setOnClickListener {
                    receiveBikeMemosOverWifi()
        }
    }


    fun receiveBikeMemosOverWifi() {
        // 데이터 수신을 비동기적으로 처리
        GlobalScope.launch(Dispatchers.IO) {
            // DatagramSocket을 사용하여 UDP 패킷을 수신
            val serverSocket = ServerSocket(8888)
            try {
                //연결대기
                Toast.makeText(this@MainActivity,"수신대기중..",Toast.LENGTH_LONG).show()
                val socket = serverSocket.accept()
                Toast.makeText(this@MainActivity,"연결성공 : ${socket.inetAddress.hostAddress}",Toast.LENGTH_LONG).show()

                //데이터수신
                val inputStream: InputStream = socket.getInputStream()
                val buffer = ByteArray(1024)
                val bytesRead = inputStream.read(buffer)
                // 수신한 데이터를 문자열로 변환
                val receivedData = buffer.decodeToString(0,bytesRead)
                // 직렬화된 문자열을 BikeMemo 리스트로 변환
                val bikeMemos = deserializeBikeMemos(receivedData)
                // Room 데이터베이스에 BikeMemo 리스트를 저장
                saveBikeMemosToRoomDatabase(bikeMemos)
                Toast.makeText(this@MainActivity,"받은데이터 ${receivedData}",Toast.LENGTH_LONG).show()

            }finally {
                serverSocket.close()
            }

           /* val inetSocketAddress = InetSocketAddress("localhost",8888)
            socket.connect(inetSocketAddress)

            val bytes = ByteArray(1024)




            val inputStream = socket.getInputStream()
           val readByteArray = inputStream.read(bytes)
            val receivePacket = DatagramPacket(receiveData, receiveData.size)
            socket.receive(receivePacket)
            socket.close()

            // 수신한 데이터를 문자열로 변환
            val receivedData = String(bytes, 0, readByteArray)

            // 직렬화된 문자열을 BikeMemo 리스트로 변환
            val bikeMemos = deserializeBikeMemos(receivedData)

            // Room 데이터베이스에 BikeMemo 리스트를 저장
            saveBikeMemosToRoomDatabase(bikeMemos)

            if (!socket.isClosed){
                socket.close()
            }*/
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
        for (i in 0 until bikeMemos.size) {

            val bikeMemo = BikeMemo(
                bikeMemos[i].model,
                bikeMemos[i].purchaseDate,
                bikeMemos[i].date,
                bikeMemos[i].km,
                bikeMemos[i].refer,
                bikeMemos[i].amount,
                bikeMemos[i].note,
                bikeMemos[i].year
            )
        insertBikeMemo(bikeMemo)
        }

    }







    fun sendBikeMemosOverWifi(bikeMemos: List<BikeMemo>) {

        val ipAddress = getIpAddress(this)
        if (ipAddress != null) {
            println("IP 주소: $ipAddress")
            Toast.makeText(this,"IP 주소: $ipAddress",Toast.LENGTH_LONG).show()
        } else {
            println("IP 주소를 찾을 수 없습니다.")
        }
        // Room 데이터베이스에서 조회한 BikeMemo 리스트를 직렬화하여 문자열로 변환
        val serializedData = serializeBikeMemos(bikeMemos)

        // 데이터 전송을 비동기적으로 처리


        GlobalScope.launch(Dispatchers.IO) {
            // DatagramSocket을 사용하여 UDP 패킷을 전송
            val socket = Socket(ipAddress,8888)

            try {
                val outputStream:OutputStream = socket.getOutputStream()
                outputStream.write(serializedData.toByteArray())
                outputStream.flush()
                Toast.makeText(this@MainActivity,"데이터를 전송했습니다",Toast.LENGTH_LONG).show()
            }finally {
                socket.close()
            }
           /* val inetSocketAddress = InetSocketAddress("localhost",8888)
            serverSocket.bind(inetSocketAddress)
            while (true){
               val socket = serverSocket.accept()
                val isa : InetSocketAddress = socket.getRemoteSocketAddress() as InetSocketAddress
                System.out.println("InetSocketAddress"+isa.hostName)
            val sendData = serializedData.toByteArray()
                val outputStream = socket.getOutputStream()
                outputStream.write(sendData)
                outputStream.flush()
            }


            if (!serverSocket.isClosed){

                serverSocket.close()
            }*/
          /*  val sendPacket = DatagramPacket(sendData, sendData.size, inetSocketAddress, 8888)
            socket.send(sendPacket)
            socket.close()*/
        }


    }

    fun serializeBikeMemos(bikeMemos: List<BikeMemo>): String {
        // BikeMemo 리스트를 직렬화하여 문자열로 변환하는 작업 수행
        // 여기에서는 예시로 JSON 형식을 사용하겠습니다.
        // 실제로는 Gson, Jackson 등의 라이브러리를 사용하는 것이 좋습니다.
        val jsonArray = JSONArray()
        for (bikeMemo in bikeMemos) {
            val jsonObject = JSONObject()
            jsonObject.put("no", bikeMemo.no)
            jsonObject.put("model", bikeMemo.model)
            jsonObject.put("purchaseDate", bikeMemo.purchaseDate)
            jsonObject.put("date", bikeMemo.date)
            jsonObject.put("km", bikeMemo.km)
            jsonObject.put("refer", bikeMemo.refer)
            jsonObject.put("amount", bikeMemo.amount)
            jsonObject.put("note", bikeMemo.note)
            jsonObject.put("year", bikeMemo.year)
            jsonArray.put(jsonObject)
        }
        return jsonArray.toString()
    }



    fun isWifiConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
            return networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.type == ConnectivityManager.TYPE_WIFI
        }
    }


    //버튼을 눌렀을때 앱의 값을 불러와 널체크 후 값입력 메모를 만들고 인서트
    private fun checkMemo() {
        with(binding) {
            //null check 후 메모 리스트를 만들어서 바이크리스트에 적용 후 인서트
            var bikeName1 = "bikeName.text.toString()+1"
            if (bikeName1.isEmpty()) {

                bikeName1 = ""
            }

            //구입날짜나 다른것이 비어있으면 참고를 빈 문자로 변경
            var startDate1 = "2021-01-01+1"
            if (startDate1.isEmpty()) {

                startDate1 = ""
            }

            var repairDate1 = "2023-01-01+1"
            if (repairDate1.isEmpty()) {
                repairDate1 = ""
            }

            //텍스트를 받아 인트로 변환
            val km1: Int = 10001

            var content1 = "content.text.toString()+1"
            if (content1.isEmpty()) {
                content1 = ""
            }


            val amount1: Int = 150001


            var note1 = "note.text.toString()+1"
            if (note1.isEmpty()) {
                note1 = ""
            }

            val year = "20231"
// 바이크 메모에 생성자 로 넘겨줌
            val memo = BikeMemo(
                bikeName1,
                startDate1,
                repairDate1,
                km1,
                content1,
                amount1,
                note1,
                year
            )
            insertBikeMemo(memo)
        }
    }
    /* // 1. Wi-Fi 연결 확인 및 데이터 전송 시작
     fun sendDataOverWifi(targetIpAddress: String) {
         // 2. Room 데이터베이스에서 데이터 조회
         val data = fetchDataFromRoomDatabase()

         // 3. 데이터를 직렬화하여 전송
         val serializedData = serializeData(data)

         // 4. 전송 시작
         startDataTransfer(targetIpAddress, serializedData)
     }

     // 2. Room 데이터베이스에서 데이터 조회
     fun fetchDataFromRoomDatabase(): List<Data> {
         // Room 데이터베이스 쿼리 실행하여 데이터 가져오기
         // ...

         return fetchedData
     }

     // 3. 데이터 직렬화
     fun serializeData(data: List<Data>): String {
         // 데이터를 직렬화하는 작업 수행
         // ...

         return serializedData
     }

     // 4. 데이터 전송
     fun startDataTransfer(targetIpAddress: String, serializedData: String) {
         // 네트워크 통신을 사용하여 데이터 전송
         // ...

         // 전송 완료 후 처리 작업
         // ...
     }*/

    //바이크 메모를 룸에 입력
    private fun insertBikeMemo(memo: BikeMemo) {
        CoroutineScope(Dispatchers.IO).launch {
            bikeMemoDao.insert(memo)

        }
    }

    // IP 주소 가져오기
    fun getIpAddress(context: Context): String? {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return null
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
                ?: return null

            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as android.net.wifi.WifiManager
                val wifiInfo = wifiManager.connectionInfo
                val ipAddress = wifiInfo.ipAddress
                return InetAddress.getByAddress(
                    byteArrayOf(
                        (ipAddress and 0xff).toByte(),
                        (ipAddress shr 8 and 0xff).toByte(),
                        (ipAddress shr 16 and 0xff).toByte(),
                        (ipAddress shr 24 and 0xff).toByte()
                    )
                ).hostAddress
            }
        } else {
            val interfaces = NetworkInterface.getNetworkInterfaces()
            while (interfaces.hasMoreElements()) {
                val networkInterface = interfaces.nextElement()
                val addresses = networkInterface.inetAddresses
                while (addresses.hasMoreElements()) {
                    val address = addresses.nextElement()
                    if (!address.isLoopbackAddress) {
                        return address.hostAddress
                    }
                }
            }
        }

        return null
    }

}