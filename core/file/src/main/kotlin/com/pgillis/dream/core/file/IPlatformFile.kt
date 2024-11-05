package com.pgillis.dream.core.file

import dev.zwander.kotlin.file.IPlatformFile
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine

internal fun IPlatformFile.listFilesRecursively(): List<IPlatformFile> =
    listFiles()
        ?.filter { it.nameWithoutExtension != ".cache"}
        ?.flatMap { file ->
            if (file.isDirectory()) {
                file.listFilesRecursively()
            } else listOf(file)
        } ?: emptyList()

internal fun IPlatformFile.listEpubFilesRecursively(): Flow<IPlatformFile> = callbackFlow {
    val allFiles = listFiles()?.filter { it.nameWithoutExtension != ".cache"}
    allFiles
        ?.filter { it.isFile() && it.getName().contains(".epub") }
        ?.forEach { trySend(it) }

    val fileFlows = allFiles
        ?.filter { it.isDirectory() }
        ?.map { dir -> dir.listEpubFilesRecursively() }
        ?.combine {

        }
    combine(fileFlows) {
        it.toList()
    }

    awaitClose()
//        ?.flatMap { file ->
//            if (file.isDirectory()) {
//                file.listEpubFilesRecursively().collect { emit() }
//            } else listOf(file)
//        } ?: emptyList()
}

internal fun IPlatformFile.findChildByPath(path: String): IPlatformFile? {
    val pathParts = path.split("/")
    var current = this
    pathParts.forEachIndexed { index, pathPart ->
        current = current.child(pathPart, isDirectory = pathParts.lastIndex != index) ?: return null
    }
    return current
}
