package com.pgillis.dream.core.file.platform

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.anggrayudi.storage.file.decompressZip
import com.anggrayudi.storage.result.ZipDecompressionResult
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.zwander.kotlin.file.IPlatformFile
import dev.zwander.kotlin.file.PlatformUriFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class AndroidCompressionManager @Inject constructor(
    @ApplicationContext private val context: Context
): CompressionManager {
    override fun decompressOrFind(
        file: IPlatformFile,
        bookCacheFolder: IPlatformFile
    ): Flow<IPlatformFile?> {
        if (bookCacheFolder.isDirectory() &&
            bookCacheFolder.listFiles()?.isNotEmpty() == true) {
            return flowOf(bookCacheFolder)
        }
        val documentFile = (file as PlatformUriFile).asDocumentFile() ?: return flowOf(null)
        val cacheBookDocFile = bookCacheFolder.asDocumentFile() ?: return flowOf(null)
        return documentFile.decompressZip(context, cacheBookDocFile).mapNotNull { result ->
            if (result is ZipDecompressionResult.Completed) {
                result.targetFolder.asPlatformFile()
            } else null
        }
    }

    private fun IPlatformFile.asDocumentFile(): DocumentFile? =
        DocumentFile.fromTreeUri(context, Uri.parse(getPath()))

    private fun DocumentFile.asPlatformFile() = PlatformUriFile(context, this)
}