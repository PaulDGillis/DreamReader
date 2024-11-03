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
import javax.inject.Inject

class BookRepo @Inject constructor(
    private val bookDao: BookDao,
    private val parser: EpubParser
) {

    fun getBooks(): Flow<List<Book>> = bookDao.getBooks().map { it.map(BookWithManifest::asBook) }

    suspend fun insertOrSkipBookDir(treeUri: String) = withContext(Dispatchers.IO) {
        parser.loadLibrary(treeUri).collect { book ->
            val (bookEntity, metaDataEntity, manifestEntity) = book.asEntity()

//            bookDao.deleteOldBooks(bookEntities.map { it.id }) TODO delete book mechanism
            bookDao.insertBooks(bookEntity)
            bookDao.insertMetadata(metaDataEntity)
            bookDao.insertManifest(manifestEntity)
        }
    }
}