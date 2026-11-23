package datawork

object CommonFileData {
    private val _bannedDirectory = setOf("git", "gradle")
    private val _allowTypes = setOf("kt", "kts", "md")

    val bannedDirectory
        get() = _bannedDirectory.toSet()
    val allowTypes
        get() = _allowTypes.toSet()
}
