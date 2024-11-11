package com.pgillis.dream.core.datastore.di

actual fun producePath(): String {
    throw Exception("iOS not ready yet")
//    producePath = {
//        val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
//            directory = NSDocumentDirectory,
//            inDomain = NSUserDomainMask,
//            appropriateForURL = null,
//            create = false,
//            error = null,
//        )
//        requireNotNull(documentDirectory).path + "/$dataStoreFileName"
//    }
}