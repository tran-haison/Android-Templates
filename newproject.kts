import java.io.File
import java.io.InputStreamReader

object NewProject {

    // kscript newproject.kts template=coroutine package-name=co.myproject.example app-name=MyProject
    fun generate(args: Array<String>) {
        // Check arguments
        checkArguments(args)

        // Creat file
        create("CoroutineTemplate/app/src/main/java/co/nimblehq/coroutine/ui/base/test.kt")

        // Copy file
        copy(
            sourcePath = "RxJavaTemplate/data/src/main/java/co/nimblehq/rxjava/data/service/common/ApiConst.kt",
            destinationPath = "CoroutineTemplate/app/src/main/java/co/nimblehq/coroutine/model/ApiConst2.kt",
            isDirectory = false
        )

        // Remove file
        remove(
            sourcePath = "CoroutineTemplate/app/src/main/java/co/nimblehq/coroutine/di",
            isDirectory = true
        )

        // Move file
        move(
            sourcePath = "CoroutineTemplate/app/src/main/java/co/nimblehq/coroutine/lib/TypeAlias.kt",
            moveToPath = "CoroutineTemplate/app/src/main/java/co/nimblehq/coroutine/model/TypeAlias.kt"
        )

        // Rename file
        rename(
            sourcePath = "CoroutineTemplate/app/src/main/java/co/nimblehq/coroutine/extension/ViewModelExt.kt",
            renameTo = "TestExt.kt"
        )

        // Replace content of file (ex: package)
        replace(
            sourcePath = "CoroutineTemplate/app/src/main/java/co/nimblehq/coroutine/ui/base/NavigationEvent.kt",
            keyword = "co.nimblehq.coroutine.ui.base",
            changeTo = "co.nimblehq.coroutine.ui.test"
        )

        // Search files
        search(
            fileName = "NavigationEvent.kt"
        )

        // Execute gradle command
        executeCommand(
            command = "./RxJavaTemplate/gradlew -p ./RxJavaTemplate testStagingDebugUnitTest"
        )
    }

    private fun checkArguments(args: Array<String>) {
        args.forEach {
            println(it)
        }
    }

    private fun create(sourcePath: String) {
        val sourceFile = File(sourcePath)
        sourceFile.createNewFile()
    }

    private fun copy(sourcePath: String, destinationPath: String, isDirectory: Boolean) {
        val sourceFile = File(sourcePath)
        val destinationFile = File(destinationPath)
        if (isDirectory) {
            sourceFile.copyRecursively(destinationFile)
        } else {
            sourceFile.copyTo(destinationFile)
        }
    }

    private fun remove(sourcePath: String, isDirectory: Boolean) {
        val sourceFile = File(sourcePath)
        if (isDirectory) {
            sourceFile.deleteRecursively()
        } else {
            sourceFile.delete()
        }
    }

    private fun move(sourcePath: String, moveToPath: String) {
        val sourceFile = File(sourcePath)
        val movedFile = File(moveToPath)
        sourceFile.renameTo(movedFile)
    }

    private fun rename(sourcePath: String, renameTo: String) {
        val sourceFile = File(sourcePath)
        val renamedPath = sourcePath.replaceAfterLast("/", renameTo)
        val renamedFile = File(renamedPath)
        sourceFile.renameTo(renamedFile)
    }

    private fun replace(sourcePath: String, keyword: String, changeTo: String) {
        val sourceFile = File(sourcePath)
        var sourceText = sourceFile.readText()
        sourceText = sourceText.replace(keyword, changeTo)
        sourceFile.writeText(sourceText)
    }

    private fun search(searchInPath: String = "./", fileName: String): Sequence<File> {
        val searchInFile = File(searchInPath)
        val foundFiles = searchInFile.walk().filter { it.name == fileName }.also {  }
        foundFiles.forEach {
            println(it.canonicalPath)
        }
        return foundFiles
    }

    private fun executeCommand(command: String) {
        val process = Runtime.getRuntime().exec(command)
        InputStreamReader(process.inputStream).forEachLine {
            println(it)
        }
    }
}

NewProject.generate(args)
