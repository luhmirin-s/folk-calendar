package lv.luhmirins.folk

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.realm.Realm

@HiltAndroidApp
class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}