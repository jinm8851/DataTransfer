package myung.jin.wifidirects


import android.app.Activity
import android.content.Intent
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem

import android.view.View
import android.view.ViewGroup



import myung.jin.wifidirects.databinding.FragmentTransferBinding




class TransferFragment : Fragment() {





    private val binding by lazy { FragmentTransferBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mytoolbar.inflateMenu(R.menu.menu_main)
        binding.mytoolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_main_auth -> {
                    val intent = Intent(requireContext(), AuthActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onStart() {
        super.onStart()
        if(!MyApplication.checkAuth()){
            binding.logoutTextView.visibility= View.VISIBLE
            binding.mainRecyclerView.visibility= View.GONE
        }else {
            binding.logoutTextView.visibility= View.GONE
            binding.mainRecyclerView.visibility= View.VISIBLE
            makeRecyclerView()
        }
    }




    private fun makeRecyclerView(){

    }


   /* //냉동코드 코드
    //계약서기능를 사용해서 권한을 취득하는 코드
    //복수의 권한을 한번에 요청하기 위해서 RequestMultiplePermissions() 사용 (한개페미션은 RequestPermission 사용)
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach { permission ->
                when {
                    //권한을 승인했을때
                    permission.value -> {
                        Snackbar.make(binding.root, "Permission granted", Snackbar.LENGTH_LONG)
                            .show()
                    }
                    // 권한을 거부했을때
                    shouldShowRequestPermissionRationale(permission.key) -> {
                        Snackbar.make(
                            binding.root,
                            "Permission required to use app.",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    //권한을 완전히 거부했을때
                    else -> {
                        Snackbar.make(binding.root, "Permission denide", Snackbar.LENGTH_LONG)
                            .show()
                        openSettings()
                    }
                }
            }
        }

    //퍼미션 체크
    private fun checkpermission() {
        if (isAllPermissionsGranted()) {
            Snackbar.make(binding.root, "Permission granted", Snackbar.LENGTH_LONG).show()

        } else {
            // 계약서를 사용하지 않을때  퍼미션을 하나라도 획득하지 못했을때 requestDangerousPerMissions() 사용
            // requestDangerousPerMissions()
            //계약서 기능을 사용할 때
            requestPermissionLauncher.launch(REQUIRED_PERMISSIONS)
        }
    }

    // 퍼미션을 받지못하고 재요청할때 코드를 확인하는 코드 REQUEST_CODE_PERMISSIONS 을 받음 엑티비티 리절트 컨트렉트를
    // 만들어서 시스템에서 전달된 결과를 처리하도록 맏듬
    private fun requestDangerousPerMissions() {
        ActivityCompat.requestPermissions(requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
    }

    //권한확인 하나라도 권한이 없으면 펄스를 반환
    private fun isAllPermissionsGranted(): Boolean = REQUIRED_PERMISSIONS.all { permission ->
        ContextCompat.checkSelfPermission(requireContext(), permission) ==
                PackageManager.PERMISSION_GRANTED
    }

    //퍼미션 확인결과 를 받음 viersion 1.2.0 버전부터 디플리케이티드해서
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //리퀘스트 코드를 받아 권한이 없으면 다시요청
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            //모든 권한이 취득된경우
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(binding.root, "Permission granted", Snackbar.LENGTH_LONG).show()
            } else {
                //권한이 취득되지 않은경우 다시 요청코드
                if (shouldShowRequestPermissionRationale(REQUIRED_PERMISSIONS[0])) {
                    Snackbar.make(
                        binding.root,
                        "Permission required to use app..",
                        Snackbar.LENGTH_LONG
                    ).show()
                    //다시 요청
                    requestDangerousPerMissions()
                } else {
                    // 다시 또 권한요청을 거부할경우 표시하고 끝냄
                    Snackbar.make(binding.root, "Permission denied", Snackbar.LENGTH_LONG).show()
                    //두번이상 권한을 거부했을경우 설정화면을 띠우는 코드
                    openSettings()
                }
            }
        }

    }

    //설정화면을 띠우는 코드
    private fun openSettings() {
        Snackbar.make(
            binding.root,
            "Permission required to use app..",
            Snackbar.LENGTH_LONG
        ).show()
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val packageName =  "myung.jin.wifidirects"
            data = Uri.fromParts("package", packageName, null)
        }.run(::startActivity)
    }

    //앱이 시작할때 런타임시가 아닌 컴파일시 메모리에 올라감 상수와 같은 열활을 함 자바의 static
    companion object {
        private val REQUIRED_PERMISSIONS: Array<String> = arrayOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,

            Manifest.permission.BLUETOOTH_CONNECT
        )
        private const val REQUEST_CODE_PERMISSIONS = 1001
    }
*/

}






/*
 registerService(8888)
        nsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, discoveryListener)
fun registerService(port: Int) {
    // NsdServiceInfo 개체를 생성하고 채웁니다.
    val serviceInfo = NsdServiceInfo().apply {
        // 이름은 충돌에 따라 변경될 수 있습니다
        // 동일한 네트워크에서 광고되는 다른 서비스와 함께.
        serviceName = "NsdChat"
        serviceType = "_nsdchat._tcp"
        setPort(port)

    }

    nsdManager = (getSystemService(Context.NSD_SERVICE) as NsdManager).apply {
        registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, registrationListener)
    }
}

fun initializeServerSocket() {
    // 사용 가능한 다음 포트에서 서버 소켓을 초기화합니다.
    serverSocket = ServerSocket(0).also { socket ->
        // 선택한 포트를 저장합니다.
        mLocalPort = socket.localPort

    }
}

private val registrationListener = object : NsdManager.RegistrationListener {

    override fun onServiceRegistered(NsdServiceInfo: NsdServiceInfo) {
        // 서비스 이름을 저장합니다. Android가 다음과 같이 변경했을 수 있습니다
        // 충돌을 해결하여 처음에 요청한 이름을 업데이트합니다
        // Android라는 이름을 사용했습니다.
        mServiceName = NsdServiceInfo.serviceName
    }

    override fun onRegistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
        // 등록에 실패했습니다! 이유를 확인하려면 디버깅 코드를 여기에 놓습니다.
    }

    override fun onServiceUnregistered(arg0: NsdServiceInfo) {
        // 서비스가 등록 취소되었습니다. 이 문제는 사용자가 전화할 때만 발생합니다
        // NsdManager.unregisterService()를 입력하고 이 수신기를 전달합니다.
    }

    override fun onUnregistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
        // 등록 취소에 실패했습니다. 이유를 확인하려면 디버깅 코드를 여기에 놓습니다.
    }
}


// 새 DiscoveryListener 인스턴스화
private val discoveryListener = object : NsdManager.DiscoveryListener {

    // 서비스 검색이 시작되는 즉시 호출됩니다.
    override fun onDiscoveryStarted(regType: String) {
        Log.d(TAG, "Service discovery started")
    }

    override fun onServiceFound(service: NsdServiceInfo) {
        // 서비스가 발견되었습니다! 그걸로 뭘 좀 해봐요.
        Log.d(TAG, "Service discovery success$service")
        when {
            service.serviceType != SERVICE_TYPE -> // 서비스 유형은 프로토콜과
                // 이 서비스의 전송 계층입니다.
                Log.d(TAG, "Unknown Service Type: ${service.serviceType}")
            service.serviceName == mServiceName -> // 서비스의 이름이 사용자에게 무엇인지 알려줍니다
                // …과의 연결. "밥의 채팅 앱"일 수도 있습니다.
                Log.d(TAG, "Same machine: $mServiceName")
            service.serviceName.contains("NsdChat") -> nsdManager.resolveService(service, resolveListener)
        }
    }

    override fun onServiceLost(service: NsdServiceInfo) {
        // 네트워크 서비스를 더 이상 사용할 수 없는 경우.
        // 내부 부기 코드는 여기에 표시됩니다.
        Log.e(TAG, "service lost: $service")
    }

    override fun onDiscoveryStopped(serviceType: String) {
        Log.i(TAG, "Discovery stopped: $serviceType")
    }

    override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
        Log.e(TAG, "Discovery failed: Error code:$errorCode")
        nsdManager.stopServiceDiscovery(this)
    }

    override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
        Log.e(TAG, "Discovery failed: Error code:$errorCode")
        nsdManager.stopServiceDiscovery(this)
    }
}
private val resolveListener = object : NsdManager.ResolveListener {

    override fun onResolveFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
        // 확인이 실패하면 호출됩니다. 오류 코드를 사용하여 디버깅합니다.
        Log.e(TAG, "Resolve failed: $errorCode")
    }

    override fun onServiceResolved(serviceInfo: NsdServiceInfo) {
        Log.e(TAG, "Resolve Succeeded. $serviceInfo")

        if (serviceInfo.serviceName == mServiceName) {
            Log.d(TAG, "Same IP.")
            return
        }
        mService = serviceInfo
        val port: Int = serviceInfo.port
        val host: InetAddress = serviceInfo.host
    }
}


//응용 프로그램의 활동

override fun onPause() {
    nsdHelper?.tearDown()
    super.onPause()
}

override fun onResume() {
    super.onResume()
    nsdHelper?.apply {
        registerService(connection.localPort)
        discoverServices()
    }
}

override fun onDestroy() {
    nsdHelper?.tearDown()
    connection.tearDown()
    super.onDestroy()
}

// NsdHelper's tearDown method
fun tearDown() {
    nsdManager.apply {
        unregisterService(registrationListener)
        stopServiceDiscovery(discoveryListener)
    }
}*/
