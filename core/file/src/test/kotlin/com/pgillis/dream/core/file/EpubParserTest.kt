package com.pgillis.dream.core.file

import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import okio.Path
import org.junit.Before
import org.junit.Test

class EpubParserTest {
    // Moby Dick is open domain, this is probably best test to start

    private lateinit var assetToTest: Path
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setupEpubParser() {
        assetToTest = JvmUnitTestAssetManager.assetPath("Moby_Dick.epub")
    }

    @Test
    fun openMobyDick() = runTest(testDispatcher) {
        val result = EpubParser.parse(assetToTest)
        println(result?.toString() ?: "Failed")
        assert(result != null)
    }
}