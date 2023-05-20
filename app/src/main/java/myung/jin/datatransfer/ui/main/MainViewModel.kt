package myung.jin.datatransfer.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


enum class ActionType {
    PLUS,MINUS
}

// 데이터의 변경 = 라이프사이클에 관계없이 데이터를 가지고 있음
// 뷰모델은 데이트의 변경사항을 알려주는 라이브 데이터를 가지고 있고
class MainViewModel : ViewModel() {
    companion object {
        const val TAG: String = "로그"
    }
    // 뮤터블 라이브 데이터  = 수정 가능한 코드
    // 라이브 데이터 = 값이 변경 안됨

    //내부에서 설정하는 자료형은 뮤터블로 변경가능하도록 설정

    private val _currentValue = MutableLiveData<Int>()
    // 외부에서 호출할때
    // 변경되지 않은 데이터를 가져올때 이름을 _언더스코어없이 설정
    //공개적으로 가져오는 변수는 private 이 아니 퍼블릭으로 외부에서도 접근가능하도록 설정
    //하지만 ㄱㅄ을 직접 라이브데이터에 접근하지 않고 뷰모델을 통해 가져올수 있도록 설정
    val currentValue: LiveData<Int>
    get() = _currentValue
    //초기값 설정
    init {
        Log.d(TAG,"ViewModel - 생성자 호출")
        //뮤터블라이브데이터 이기 때문에 여기서 변경 가능함
        _currentValue.value = 0
    }

    fun updateValue(actionType: ActionType,input: Int){
        when(actionType){
            ActionType.PLUS ->
                _currentValue.value = _currentValue.value?.plus(input)
            ActionType.MINUS ->
                _currentValue.value = _currentValue.value?.minus(input)
        }
    }
}