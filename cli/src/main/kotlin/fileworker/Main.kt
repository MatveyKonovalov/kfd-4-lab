package fileworker


import datawork.FileExceptions
import datawork.getAnalyze
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Path not provided")
        exitProcess(1)
    }

    val path = args[0]
    try {
        val analyze = getAnalyze(path)
        val extensions = analyze.extensions
        val topThree = analyze.topThree

        extensions.forEach { extension ->
            println("${extension.extension}: ${extension.cntFiles} files, ${extension.cntLines} lines")
        }

        topThree.forEach { fileReport ->
            println("${fileReport.path} (${fileReport.amountLines})")
        }
    } catch (e: FileExceptions.IncorrectDirectoryWay) {
        println(e.message)
        exitProcess(2)
    } catch (e: FileExceptions.ExceptFileNotDirectory){
        println(e.message)
        exitProcess(3)
    }
}





