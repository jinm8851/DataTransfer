package myung.jin.datatrans


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import myung.jin.datatrans.databinding.ActivityMainBinding

/*
* 런타임 퍼미션 작성코드
* 예를 들면 로케이션 같이 화면 안에서 퍼미션 요청할때 사용
* 1.2.0 버전 이후 부터는 계약서 사용으로 바뀜
* androidx.activity.result.contract (계약서방식)
* ActivityResultContract 사용 엑티비티에서 결과를 가져올수 있음
* ActivityResultContracts.RequestMultiplePremissions 여러개의 퍼미션을 한벙에사용
* ActivityResultContracts..requestPermission 한개의 퍼미션을 사용할때 사용
* 그레들디펜던시설정 */

class MainActivity : AppCompatActivity() {
    //바인딩을 바이레이즈로 사용하면 스넥바에 적용가능
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.permission.setOnClickListener {
            checkpermission()
        }
    }

    //냉동코드 코드
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
        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
    }

    //권한확인 하나라도 권한이 없으면 펄스를 반환
    private fun isAllPermissionsGranted(): Boolean = REQUIRED_PERMISSIONS.all { permission ->
        ContextCompat.checkSelfPermission(this, permission) ==
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
             data = Uri.fromParts("package", packageName, null)
         }.run(::startActivity)
     }

    //앱이 시작할때 런타임시가 아닌 컴파일시 메모리에 올라감 상수와 같은 열활을 함 자바의 static
    companion object {
        private val REQUIRED_PERMISSIONS: Array<String> = arrayOf(
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CHANGE_NETWORK_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        private const val REQUEST_CODE_PERMISSIONS = 1001
    }
}