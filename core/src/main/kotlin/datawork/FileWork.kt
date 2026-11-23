package datawork

import java.io.File
import java.nio.file.Files


@DslMarker
annotation class FileWorkMarker

@FileWorkMarker
class EnterChecker(val file: File) {
    fun checkDirectory(bannedDirectory: Set<String>): Boolean {
        return file.name !in bannedDirectory
    }

    fun checkTypes(allowTypes: Set<String>): Boolean {
        return file.extension in allowTypes
    }

    fun checkSymbolLink(): Boolean {
        return !Files.isSymbolicLink(file.toPath())
    }
}

@FileWorkMarker
class FileWorker(val path: String) {

    fun getDirectoryFromString(): File? {
        val result = File(path)
        return if (result.isDirectory) result else null
    }

    fun getNestedDirectory(
        onEnter: EnterChecker.() -> Boolean,
        filterFiles: EnterChecker.() -> Boolean
    ): List<File> {
        val directory = File(path)

        return directory.walkTopDown().onEnter { file -> EnterChecker(file).onEnter() }
            .filter { file -> file.isFile && EnterChecker(file).filterFiles() }.toList()
    }

    fun searchSuitableFile(fileName: String): File? {
        return getNestedDirectory(onEnter = {
            checkSymbolLink()
            checkDirectory(bannedDirectory = CommonFileData.bannedDirectory)
        }) {
            checkTypes(CommonFileData.allowTypes)
        }
            .firstOrNull { file ->
                file.name == fileName
            }
    }

    fun countLinesFromFile(file: File): Int {
        if (file.isFile) {
            return file.useLines { lines -> lines.count() }
        }
        throw FileExceptions.ExceptFileNotDirectory()
    }

    fun getFilePath(file: File): String = file.relativeTo(File(path)).invariantSeparatorsPath
}
