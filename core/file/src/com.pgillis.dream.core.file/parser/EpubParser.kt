package com.pgillis.dream.core.file.parser

import com.pgillis.dream.core.model.Book
import okio.FileSystem
import okio.Path

interface EpubParser {
    fun parse(fs: FileSystem, libraryDir: Path, bookCacheDirectory: Path): Book
}