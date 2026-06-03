package eu.livesport.mdevcamp26

import android.app.Application
import eu.livesport.mdevcamp26.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class WcApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@WcApplication)
            modules(appModule)
        }
    }
}
