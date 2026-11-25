package datawork

import java.io.File

data class ExtensionReport(val extension: String, val cntFiles: Int, val cntLines: Int)
data class FileReport(val path: String, val amountLines: Int) : Comparable<FileReport> {
    override fun compareTo(other: FileReport): Int {
        return if (other == this) {
            0
        } else if (other.amountLines == this.amountLines) {
            path.compareTo(other.path)
        } else {
            -amountLines.compareTo(other.amountLines)
        }
    }
}

data class Analyze(val extensions: List<ExtensionReport>, val topThree: List<FileReport>)

private data class ExtensionCalc(val amountFiles: Int, val amountLines: Int)

fun makeAnaLyze(files: List<File>, countLines: (File) -> Int, getPath: (File) -> String): Analyze {

    val extensionsData = mutableMapOf<String, ExtensionCalc>()
    val fileReports = mutableListOf<FileReport>()

    files.forEach { file ->
        val extension = file.extension
        val amountLines = countLines(file)
        fileReports.add(FileReport(path = getPath(file), amountLines = amountLines))

        if (extension in extensionsData) {
            val report = extensionsData[extension]!!
            extensionsData[extension] = ExtensionCalc(
                amountFiles = report.amountFiles + 1,
                amountLines = report.amountLines + amountLines
            )

        } else {
            extensionsData[extension] = ExtensionCalc(amountFiles = 1, amountLines = amountLines)
        }
    }

    val sortExtensions = extensionsData.keys.sorted().map { key ->
        val excalc = extensionsData[key]!!
        ExtensionReport(extension = key, cntFiles = excalc.amountFiles, cntLines = excalc.amountLines)
    }


    fileReports.sort()
    val topThree = fileReports.take(3)

    return Analyze(extensions = sortExtensions, topThree = topThree)
}

fun getAnalyze(path: String): Analyze{
    val fileWorker = FileWorker(path)

    if (fileWorker.getDirectoryFromString() == null) {
        throw FileExceptions.IncorrectDirectoryWay("Directory $path is not exists")
    }

    val files = fileWorker.getNestedDirectory(onEnter = {
        checkSymbolLink() && checkDirectory(CommonFileData.bannedDirectory)
    }) {
        checkSymbolLink() && checkTypes(CommonFileData.allowTypes)
    }

    return makeAnaLyze(files, fileWorker::countLinesFromFile, fileWorker::getFilePath)
}

