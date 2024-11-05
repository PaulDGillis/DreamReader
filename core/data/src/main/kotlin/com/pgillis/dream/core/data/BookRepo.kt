package com.pgillis.dream.core.data

import com.pgillis.dream.core.data.model.asBook
import com.pgillis.dream.core.data.model.asBookEntity
import com.pgillis.dream.core.data.model.asManifestDataEntities
import com.pgillis.dream.core.data.model.asMetaDataEntity
import com.pgillis.dream.core.database.dao.BookDao
import com.pgillis.dream.core.database.model.BookWithManifest
import com.pgillis.dream.core.datastore.SettingsStore
import com.pgillis.dream.core.file.EpubParser
import com.pgillis.dream.core.model.Book
import dev.zwander.kotlin.file.IPlatformFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BookRepo @Inject constructor(
    private val bookDao: BookDao,
    private val parser: EpubParser,
    private val settingsStore: SettingsStore,
) {

    fun getBooks(): Flow<List<Book>> = bookDao.getBooks().map { it.map(BookWithManifest::asBook) }

    suspend fun insertOrSkipBookDir(libraryDirectory: IPlatformFile) = withContext(Dispatchers.IO) {

        settingsStore.update(this) { it.copy(libraryDir = libraryDirectory.getPath()) }

        combine(parser.loadLibrary(libraryDirectory)) { it.toList() }
            .collect { books ->
                val bookEntities = books.map { it.asBookEntity() }
                val metadataEntities = books.map { it.asMetaDataEntity() }
                val manifestEntities = books.flatMap { it.asManifestDataEntities() }

                bookDao.deleteOldBooks(books.map { it.id }) // TODO delete book mechanism
                bookDao.insertBooks(bookEntities)
                bookDao.insertMetadata(metadataEntities)
                bookDao.insertManifest(manifestEntities)
            }
    }
}