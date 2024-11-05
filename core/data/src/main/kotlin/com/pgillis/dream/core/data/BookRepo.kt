package com.pgillis.dream.core.data

import android.content.Intent
import com.pgillis.dream.core.data.model.asBook
import com.pgillis.dream.core.data.model.asEntity
import com.pgillis.dream.core.database.dao.BookDao
import com.pgillis.dream.core.database.model.BookWithManifest
import com.pgillis.dream.core.datastore.SettingsStore
import com.pgillis.dream.core.file.EpubParser
import com.pgillis.dream.core.model.Book
import dev.zwander.kotlin.file.filekit.toKmpFile
import io.github.vinceglb.filekit.core.PlatformDirectory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BookRepo @Inject constructor(
    private val bookDao: BookDao,
    private val parser: EpubParser,
    private val settingsStore: SettingsStore,
) {

    fun getBooks(): Flow<List<Book>> = bookDao.getBooks().map { it.map(BookWithManifest::asBook) }

    suspend fun insertOrSkipBookDir(libraryPlatformDirectory: PlatformDirectory) = withContext(Dispatchers.IO) {

        val libraryDirectoryFile = libraryPlatformDirectory.toKmpFile()
        settingsStore.update(this) { it.copy(libraryDir = libraryDirectoryFile.getPath()) }

        parser.loadLibrary(libraryDirectoryFile).collect { book ->
            val (bookEntity, metaDataEntity, manifestEntity) = book.asEntity()

//            bookDao.deleteOldBooks(bookEntities.map { it.id }) TODO delete book mechanism
            bookDao.insertBooks(bookEntity)
            bookDao.insertMetadata(metaDataEntity)
            bookDao.insertManifest(manifestEntity)
        }
    }

    fun persistPermissions(directoryFile: PlatformDirectory) {
        val contentResolver = context.contentResolver

        val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        // Check for the freshest data.
        contentResolver.takePersistableUriPermission(directoryFile.uri, takeFlags)
    }
}