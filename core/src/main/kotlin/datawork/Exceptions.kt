package datawork

sealed class FileExceptions(message: String) : IllegalArgumentException(message) {
    data class IncorrectDirectoryWay(override val message: String = "Incorrect way") : FileExceptions(message)
    data class ExceptFileNotDirectory(override val message: String = "Incorrect file type: expect file") :
        FileExceptions(message)
}
