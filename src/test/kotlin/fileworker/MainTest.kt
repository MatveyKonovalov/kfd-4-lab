package fileworker

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class MainTest {
    @Test
    fun `test directory 1 expect success`() {
        val path = "./src/test/kotlin/testdirectories/test1"
        val analyze = getAnalyze(path)
        val expectedExtensions = listOf(
            ExtensionReport("kt", 2, 4),
            ExtensionReport("md", 1, 0)
        )

        val expectedFiles = listOf(
            FileReport("src/A.kt", 2),
            FileReport("src/B.kt", 2),
            FileReport("README.md", 0)
        )

        assertEquals(expectedExtensions, analyze.extensions)
        assertEquals(expectedFiles, analyze.topThree)
    }

    @Test
    fun `test empty directory 2 expect empty reports`() {
        val path = "./src/test/kotlin/testdirectories/test2"
        val analyze = getAnalyze(path)

        assertEquals(emptyList<ExtensionReport>(), analyze.extensions)
        assertEquals(emptyList<FileReport>(), analyze.topThree)
    }

    @Test
    fun `test incorrect directory path expect exception`(){
        val incorrectPath = "./src/test/kotlin/testdirectories/unknown"
        assertThrows<FileExceptions.IncorrectDirectoryWay>{
            val analyze = getAnalyze(incorrectPath)
        }
    }
}
