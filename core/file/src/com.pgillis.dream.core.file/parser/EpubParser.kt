package com.pgillis.dream.core.file.parser

import com.pgillis.dream.core.model.Book
import dev.zwander.kotlin.file.IPlatformFile

interface EpubParser {
    fun parse(bookCacheDirectory: IPlatformFile): Book
}