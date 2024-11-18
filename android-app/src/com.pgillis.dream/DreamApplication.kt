package com.pgillis.dream

import android.app.Application
import com.pgillis.dream.core.database.di.databaseModule
import com.pgillis.dream.core.datastore.di.settingsStoreModule
import com.pgillis.dream.core.file.di.fileManagerModule
import com.pgillis.dream.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class DreamApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@DreamApplication)
            modules(
                appModule,
                databaseModule,
                settingsStoreModule,
                fileManagerModule
            )
        }
    }
}