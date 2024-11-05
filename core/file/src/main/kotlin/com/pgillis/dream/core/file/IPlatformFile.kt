package com.pgillis.dream.core.file

import dev.zwander.kotlin.file.IPlatformFile

internal fun IPlatformFile.listFilesRecursively(): List<IPlatformFile> =
    listFiles()
        ?.filter { it.nameWithoutExtension != ".cache"}
        ?.flatMap { file ->
            if (file.isDirectory()) {
                file.listFilesRecursively()
            } else listOf(file)
        } ?: emptyList()

internal fun IPlatformFile.listFilesRecursivelyAsMap(): Map<String, IPlatformFile> {
    val outMap = mutableMapOf<String, IPlatformFile>()
    listFiles()
        ?.filter { it.nameWithoutExtension != ".cache" }
        ?.forEach { file ->
            val result = if (file.isDirectory()) {
                file.listFilesRecursivelyAsMap().mapKeys { (key, _) -> "${file.getName()}/$key" }
            } else mapOf(Pair(file.getName(), file))
            outMap.putAll(result)
        }
    return outMap
}
