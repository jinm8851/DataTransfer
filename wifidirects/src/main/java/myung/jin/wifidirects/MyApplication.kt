package myung.jin.wifidirects

import androidx.core.content.ContentProviderCompat.requireContext
import androidx.multidex.MultiDexApplication
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class MyApplication: MultiDexApplication() {
    companion object {
        lateinit var auth: FirebaseAuth
        var email: String? = null

        lateinit var db: FirebaseFirestore

        fun checkAuth(): Boolean {
            var currentUser = auth.currentUser
            return currentUser?.let {
                email = currentUser.email
                currentUser.isEmailVerified
            } ?: let {
                false
            }
        }



    }

    override fun onCreate() {
        super.onCreate()
        auth = Firebase.auth



        db = FirebaseFirestore.getInstance()
    }
}