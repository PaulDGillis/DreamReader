package com.pgillis.dream.core.file.platform

import dev.zwander.kotlin.file.IPlatformFile

class FileManager {
    internal fun listFilesRecursively(directory: IPlatformFile): List<IPlatformFile> =
        directory.listFiles()
            ?.filter { it.nameWithoutExtension != ".cache"}
            ?.flatMap { file ->
                if (file.isDirectory()) {
                    listFilesRecursively(file)
                } else listOf(file)
            } ?: emptyList()

    internal fun decompressEpub(directory: List<IPlatformFile>) {
        directory
    }
}