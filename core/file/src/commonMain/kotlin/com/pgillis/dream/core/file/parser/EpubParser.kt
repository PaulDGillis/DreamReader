package com.pgillis.dream.core.file.parser

import com.pgillis.dream.core.model.Book
import dev.zwander.kotlin.file.IPlatformFile
import okio.Path

interface EpubParser {
    fun parse(bookCacheDirectory: Path): Book
}