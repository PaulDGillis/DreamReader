package com.pgillis.dream

import android.app.Application
import com.pgillis.dream.core.database.di.DatabaseModule
import com.pgillis.dream.core.datastore.di.SettingsStoreModule
import com.pgillis.dream.core.file.di.FileManagerModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.ksp.generated.*

class DreamApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@DreamApplication)
            modules(
                defaultModule,
                DatabaseModule().module,
                SettingsStoreModule().module,
                FileManagerModule().module
            )
        }
    }
}