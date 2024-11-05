package com.pgillis.dream.core.file

import com.pgillis.dream.core.file.parser.KsoupParser
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import okio.Path
import org.junit.Before
import org.junit.Test

class KsoupParserTest {
    // Moby Dick is open domain, this is probably best test to start

    private lateinit var assetToTest: Path
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setupEpubParser() {
        assetToTest = JvmUnitTestAssetManager.assetPath("Moby_Dick.epub")
    }

    @Test
    fun openMobyDick() = runTest(testDispatcher) {
        val result = KsoupParser.parse(assetToTest)
        println(result?.toString() ?: "Failed")
        assert(result != null)
    }
}