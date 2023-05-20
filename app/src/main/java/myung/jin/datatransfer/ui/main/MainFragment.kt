package myung.jin.datatransfer.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import myung.jin.datatransfer.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }


    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private val TAG: String = "로그"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        val view = binding.root
        return view
    }

    //프레그 먼트에서는 onViewCreated() 이 메소드에서 화면 바인딩을 불러올수 있음 onCreate() 아님
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.currentValue.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "FragmentActivity - viewModel - currentValue 라이브 데이터 값 변경 : $it")
            binding.numberTextview.text = it.toString()
        })
        //        userInput = binding.userinputEdittext.toString().toInt()
        //       뷰모델에 라이브데이터 값을 변경하는 메소드 실행
        binding.plusBtn.setOnClickListener {
            val userInput: Int
            userInput = binding.userinputEdittext.text.toString().toInt()
            viewModel.updateValue(actionType = ActionType.PLUS, userInput)
        }
        binding.minusBtn.setOnClickListener {
            val userInput: Int
            userInput = binding.userinputEdittext.text.toString().toInt()
            viewModel.updateValue(actionType = ActionType.MINUS, userInput)
        }

//        binding.plusBtn.setOnClickListener(this)
//        binding.minusBtn.setOnClickListener(this)
    }

//    override fun onClick(v: View?) {
//        when (v) {
//            binding.plusBtn -> {
//                 var userInput : Int =0
//                 userInput = binding.userinputEdittext.text.toString().toInt()
//
//                    viewModel.updateValue(actionType = ActionType.PLUS,  userInput!!)
//                }
//            binding.minusBtn -> {
//                var userInput : Int =0
//                userInput = binding.userinputEdittext.text.toString().toInt()
//                    viewModel.updateValue(actionType = ActionType.MINUS,  userInput!!)
//                }
//            }
//
//        }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}