package com.pgillis.dream.core.data

import com.pgillis.dream.core.data.model.asBook
import com.pgillis.dream.core.data.model.asEntity
import com.pgillis.dream.core.database.dao.BookDao
import com.pgillis.dream.core.database.model.BookWithManifest
import com.pgillis.dream.core.file.EpubParser
import com.pgillis.dream.core.model.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okio.FileSystem
import okio.Path
import javax.inject.Inject

class BookRepo @Inject constructor(private val bookDao: BookDao) {

    fun getBooks(): Flow<List<Book>> = bookDao.getBooks().map { it.map(BookWithManifest::asBook) }

    suspend fun insertOrSkipBookDir(libraryPath: Path) = withContext(Dispatchers.IO) {
        val fileSystem = FileSystem.SYSTEM
        val libraryDir = fileSystem.list(libraryPath)

        val library = libraryDir.mapNotNull {
            val file = fileSystem.metadataOrNull(it)
            if (file?.isRegularFile == true) {
                EpubParser.parse(it)?.asEntity()
            } else null
        }

        val bookEntities = library.map { it.first }
        val metaDataEntities = library.map { it.second }
        val manifestEntities = library.flatMap { it.third }

        bookDao.deleteOldBooks(bookEntities.map { it.id })
        bookDao.insertBooks(bookEntities)
        bookDao.insertMetadata(metaDataEntities)
        bookDao.insertManifest(manifestEntities)
    }
}