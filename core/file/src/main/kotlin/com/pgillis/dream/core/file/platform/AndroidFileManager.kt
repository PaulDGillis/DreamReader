package com.pgillis.dream.core.file.platform

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.anggrayudi.storage.file.DocumentFileCompat
import com.anggrayudi.storage.file.baseName
import com.anggrayudi.storage.file.decompressZip
import com.anggrayudi.storage.file.extension
import com.anggrayudi.storage.file.findFolder
import com.anggrayudi.storage.file.openInputStream
import com.anggrayudi.storage.result.ZipDecompressionResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.merge
import java.io.BufferedInputStream
import javax.inject.Inject

class AndroidFileManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun persistPermissions(treeUri: Uri): String {
        val contentResolver = context.contentResolver

        val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        // Check for the freshest data.
        contentResolver.takePersistableUriPermission(treeUri, takeFlags)

        return treeUri.toString() // DocumentFileCompat.fromUri(context, treeUri)?.getAbsolutePath(context)
    }

    internal fun decompressEpubs(treeUri: String): Flow<DocumentFile?> {
        val treeDirectory = DocumentFileCompat.fromUri(context, Uri.parse(treeUri))
        if (treeDirectory != null) {
            val fileFlows = listFiles(treeDirectory).map {
                decompressOrFind(it, treeDirectory)
            }
            return fileFlows.merge()
        }
        return flowOf(null)
    }

    internal fun listFiles(treeDirectory: DocumentFile): List<DocumentFile> {
        // TODO maybe should be val other = DocumentFileCompat.getAccessibleAbsolutePaths(context)
        if (!treeDirectory.isDirectory) return emptyList()

        return listFilesRecursively(treeDirectory)
    }

    private fun listFilesRecursively(directory: DocumentFile): List<DocumentFile> {
        val documentFiles = mutableListOf<DocumentFile>()
        directory.listFiles().forEach {
            if (it.isDirectory && it.baseName != ".cache") {
                documentFiles += listFilesRecursively(it)
            } else if (it.isFile) {
                documentFiles.add(it)
            }
        }
        return documentFiles
    }

    internal fun openBufferedStream(documentFile: DocumentFile): BufferedInputStream {
        val bufferedInStream = BufferedInputStream(documentFile.openInputStream(context))
        return bufferedInStream
    }

    private fun decompressOrFind(documentFile: DocumentFile, treeDoc: DocumentFile): Flow<DocumentFile?> {
        if (!documentFile.isFile || documentFile.extension != "epub" || documentFile.baseName.isEmpty())
            return flowOf(null)

        val rootTargetFolder = treeDoc.findFolder(".cache") ?: treeDoc.createDirectory(".cache") ?: return flowOf(null)

        val targetFolder = rootTargetFolder.findFolder(documentFile.baseName).let { target ->
            if (target == null || !target.exists()) {
                rootTargetFolder.createDirectory(documentFile.baseName)
            } else target
        } ?: return flowOf(null)


        return documentFile.decompressZip(context, targetFolder).mapNotNull { result ->
            if (result is ZipDecompressionResult.Completed) {
                result.targetFolder
            } else null
        }
    }
}