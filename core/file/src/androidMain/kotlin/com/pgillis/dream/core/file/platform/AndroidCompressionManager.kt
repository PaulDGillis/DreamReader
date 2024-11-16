//package com.pgillis.dream.core.file.platform
//
//import android.content.Context
//import android.net.Uri
//import androidx.documentfile.provider.DocumentFile
//import com.anggrayudi.storage.file.decompressZip
//import com.anggrayudi.storage.result.ZipDecompressionResult
//import dev.zwander.kotlin.file.IPlatformFile
//import dev.zwander.kotlin.file.PlatformUriFile
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.flowOf
//import kotlinx.coroutines.flow.mapNotNull
//import okio.Path.Companion.toPath
//
//class AndroidCompressionManager(
//    private val context: Context
//): CompressionManager {
//    override fun decompressOrFind(
//        file: IPlatformFile,
//        bookCacheFolder: IPlatformFile
//    ): Flow<IPlatformFile?> {
////        val okioPath = file.getPath().toPath()
////        println(okioPath)
////        content:/com.android.externalstorage.documents/tree/primary%3ADocuments%2FBooks/document/primary%3ADocuments%2FBooks%2FCarnegie%2C%20Dale%20-%20How%20to%20Win%20Friends%20and%20Influence%20People-HarperCollins%20Canada_HarperPerennial%20Classics%20(2014).epub
//        if (bookCacheFolder.isDirectory() &&
//            bookCacheFolder.listFiles()?.isNotEmpty() == true) {
//            return flowOf(bookCacheFolder)
//        }
//        val documentFile = (file as PlatformUriFile).asDocumentFile() ?: return flowOf(null)
//        val cacheBookDocFile = bookCacheFolder.asDocumentFile() ?: return flowOf(null)
//        return documentFile.decompressZip(context, cacheBookDocFile).mapNotNull { result ->
//            if (result is ZipDecompressionResult.Completed) {
//                result.targetFolder.asPlatformFile()
//            } else null
//        }
//    }
//
//    private fun IPlatformFile.asDocumentFile(): DocumentFile? =
//        DocumentFile.fromTreeUri(context, Uri.parse(getPath()))
//
//    private fun DocumentFile.asPlatformFile() = PlatformUriFile(context, this)
//}