package com.pgillis.dream.core.file

import dev.zwander.kotlin.file.IPlatformFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch

internal fun IPlatformFile.listFilesRecursively(): List<IPlatformFile> =
    listFiles()
        ?.filter { it.nameWithoutExtension != ".cache"}
        ?.flatMap { file ->
            if (file.isDirectory()) {
                file.listFilesRecursively()
            } else listOf(file)
        } ?: emptyList()

internal fun IPlatformFile.listEpubFilesRecursively(): Flow<IPlatformFile> = channelFlow {
    val allFiles = listFiles()?.filter { it.nameWithoutExtension != ".cache"}

    launch {
        allFiles
            ?.filter { it.isFile() && it.getName().contains(".epub") }
            ?.forEach { send(it) }
    }

    allFiles
        ?.filter { it.isDirectory() }
        ?.forEach { dir ->
            launch {
                dir.listEpubFilesRecursively().collect {
                    send(it)
                }
            }
        }
}

internal fun IPlatformFile.findChildByPath(path: String): IPlatformFile? {
    val pathParts = path.split("/")
    var current = this
    pathParts.forEachIndexed { index, pathPart ->
        current = current.child(pathPart, isDirectory = pathParts.lastIndex != index) ?: return null
    }
    return current
}
