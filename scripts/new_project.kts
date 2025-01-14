import java.io.File
import java.util.Properties

object NewProject {

    private const val DELIMITER_ARGUMENT = "="

    private const val KEY_APP_NAME = "app-name"
    private const val KEY_HELP = "--help"
    private const val KEY_PACKAGE_NAME = "package-name"
    private const val KEY_TEMPLATE = "template"

    private const val PATTERN_APP = "^([A-Z][a-zA-Z0-9\\s]*)|([a-z][a-z0-9-]*)$"
    private const val PATTERN_PACKAGE = "^[a-z]+(\\.[a-z][a-z0-9]*)+$"

    private const val SCRIPTS_FOLDER_NAME = "scripts"
    private const val SCRIPT_VERSION_PROPERTY_NAME = "templateScriptVersion"
    private const val SEPARATOR_DOT = "."
    private const val SEPARATOR_MINUS = "-"
    private const val SEPARATOR_SLASH = "/"
    private const val SEPARATOR_SPACE = " "

    private const val TEMPLATE_APP_NAME_XML = "Template XML"
    private const val TEMPLATE_APP_NAME_COMPOSE = "Template Compose"
    private const val TEMPLATE_PACKAGE_NAME_XML = "co.nimblehq.template.xml"
    private const val TEMPLATE_PACKAGE_NAME_COMPOSE = "co.nimblehq.template.compose"
    private const val TEMPLATE_XML = "xml"
    private const val TEMPLATE_COMPOSE = "compose"
    private const val TEMPLATE_FOLDER_NAME_XML = "template-xml"
    private const val TEMPLATE_FOLDER_NAME_COMPOSE = "template-compose"

    private const val VERSION_FILE_NAME = "version.properties"

    private val helpMessage = """
        Run kscript new_project.kts to create a new project with the following arguments:
            $KEY_PACKAGE_NAME=   New package name (i.e., com.example.package)
            $KEY_APP_NAME=       New app name (i.e., MyApp, "My App", "my-app")
            $KEY_TEMPLATE=       Template (i.e., $TEMPLATE_XML, $TEMPLATE_COMPOSE)
        
        Examples:
            kscript new_project.kts $KEY_PACKAGE_NAME=co.myxmlproject.example $KEY_APP_NAME="My XML Project" $KEY_TEMPLATE=$TEMPLATE_XML
            kscript scripts/new_project.kts $KEY_PACKAGE_NAME=co.myxmlproject.example $KEY_APP_NAME="My XML Project" $KEY_TEMPLATE=$TEMPLATE_XML
    """.trimIndent()

    private val modules = listOf("app", "data", "domain")

    private val fileSeparator = File.separator

    private var appName: String = ""
        set(value) {
            field = if (value.contains(SEPARATOR_MINUS)) {
                projectFolderName = value
                value.replace(SEPARATOR_MINUS, SEPARATOR_SPACE).uppercaseEveryFirstCharacter()
            } else {
                value.uppercaseEveryFirstCharacter().also {
                    projectFolderName = it.getStringWithoutSpace()
                }
            }
        }

    private var packageName = ""

    private var projectFolderName: String = ""

    private val projectPath: String
        get() = rootPath + projectFolderName

    private val rootPath: String
        get() = System.getProperty("user.dir").let { userDir ->
            if (userDir.endsWith("$fileSeparator$SCRIPTS_FOLDER_NAME")) {
                userDir.substring(0, userDir.lastIndexOf(SCRIPTS_FOLDER_NAME))
            } else {
                "$userDir$fileSeparator"
            }
        }

    private var template: String = ""

    private val templatePackageName
        get() = if (template == TEMPLATE_XML) {
            TEMPLATE_PACKAGE_NAME_XML
        } else {
            TEMPLATE_PACKAGE_NAME_COMPOSE
        }

    private val templateFolderName
        get() = if (template == TEMPLATE_XML) {
            TEMPLATE_FOLDER_NAME_XML
        } else {
            TEMPLATE_FOLDER_NAME_COMPOSE
        }

    private val templateAppName
        get() = if (template == TEMPLATE_XML) {
            TEMPLATE_APP_NAME_XML
        } else {
            TEMPLATE_APP_NAME_COMPOSE
        }

    fun generate(args: Array<String>) {
        showScriptVersion()
        handleArguments(args)
        initializeNewProjectFolder()
        cleanNewProjectFolder()
        renamePackageNameFolders()
        renamePackageNameWithinFiles()
        renameAppName()
        buildProjectAndRunTests()
    }

    private fun showScriptVersion() {
        val properties = File(rootPath).loadProperties(VERSION_FILE_NAME)
        val scriptVersion = properties.getProperty(SCRIPT_VERSION_PROPERTY_NAME) as String
        showMessage(message = "=> \uD83D\uDC4B Running new project script version $scriptVersion... ")
    }

    private fun handleArguments(args: Array<String>) {
        var hasAppName = false
        var hasPackageName = false
        var hasTemplate = false
        args.forEach { arg ->
            when {
                arg == KEY_HELP -> {
                    showMessage(
                        message = helpMessage,
                        exitAfterMessage = true
                    )
                }
                arg.startsWith("$KEY_APP_NAME$DELIMITER_ARGUMENT") -> {
                    val (key, value) = arg.split(DELIMITER_ARGUMENT)
                    validateAppName(value)
                    hasAppName = true
                }
                arg.startsWith("$KEY_PACKAGE_NAME$DELIMITER_ARGUMENT") -> {
                    val (key, value) = arg.split(DELIMITER_ARGUMENT)
                    validatePackageName(value)
                    hasPackageName = true
                }
                arg.startsWith("$KEY_TEMPLATE$DELIMITER_ARGUMENT") -> {
                    val (key, value) = arg.split(DELIMITER_ARGUMENT)
                    validateTemplate(value)
                    hasTemplate = true
                }
                else -> {
                    showMessage(
                        message = "ERROR: Invalid argument name: $arg \n$helpMessage",
                        exitAfterMessage = true
                    )
                }
            }
        }
        when {
            !hasAppName -> showMessage(
                message = "ERROR: No app name has been provided \n$helpMessage",
                exitAfterMessage = true
            )
            !hasPackageName -> showMessage(
                message = "ERROR: No package name has been provided \n$helpMessage",
                exitAfterMessage = true
            )
            !hasTemplate -> showMessage(
                message = "ERROR: No template has been provided \n$helpMessage",
                exitAfterMessage = true
            )
        }
    }

    private fun validateAppName(value: String) {
        if (PATTERN_APP.toRegex().matches(value)) {
            appName = value.trim()
        } else {
            showMessage(
                message = "ERROR: Invalid App Name: $value (needs to follow standard pattern {MyProject} or {My Project}) or {my-project} \n$helpMessage",
                exitAfterMessage = true
            )
        }
    }

    private fun validatePackageName(value: String) {
        if (PATTERN_PACKAGE.toRegex().matches(value)) {
            packageName = value.trim()
        } else {
            showMessage(
                message = "ERROR: Invalid Package Name: $value (needs to follow standard pattern {com.example.package}) \n$helpMessage",
                exitAfterMessage = true
            )
        }
    }

    private fun validateTemplate(value: String) {
        if (value == TEMPLATE_XML || value == TEMPLATE_COMPOSE) {
            template = value.trim()
        } else {
            showMessage(
                message = "ERROR: Invalid Template: $value (can either be $TEMPLATE_XML or $TEMPLATE_COMPOSE) \n$helpMessage",
                exitAfterMessage = true
            )
        }
    }

    private fun initializeNewProjectFolder() {
        showMessage("=> 🐢 Initializing new project...")
        copyFiles(fromPath = rootPath + templateFolderName, toPath = projectPath)
        // Set gradlew file as executable, because copying files from one folder to another doesn't copy file permissions correctly (= read, write & execute).
        File(projectPath + fileSeparator + "gradlew")?.setExecutable(true)
    }

    private fun cleanNewProjectFolder() {
        executeCommand(
            "sh",
            "$projectPath${fileSeparator}gradlew",
            "-p",
            "$projectPath",
            "clean"
        )
        executeCommand(
            "sh",
            "$projectPath${fileSeparator}gradlew",
            "-p",
            "$projectPath${fileSeparator}buildSrc",
            "clean"
        )
        listOf(".idea", ".gradle", "buildSrc$fileSeparator.gradle", ".git").forEach {
            File("$projectPath$fileSeparator$it")?.let { targetFile ->
                targetFile.deleteRecursively()
            }
        }
    }

    private fun renamePackageNameFolders() {
        showMessage("=> 🔎 Renaming the package folders...")
        modules.forEach { module ->
            val srcPath = projectPath + fileSeparator + module + fileSeparator + "src"
            File(srcPath)
                .walk()
                .maxDepth(2)
                .filter { it.isDirectory && it.name == "java" }
                .forEach { javaDirectory ->
                    val oldDirectory = File(
                        javaDirectory, templatePackageName.replace(
                            oldValue = SEPARATOR_DOT,
                            newValue = fileSeparator
                        )
                    )
                    val newDirectory = File(
                        javaDirectory, packageName.replace(
                            oldValue = SEPARATOR_DOT,
                            newValue = fileSeparator
                        )
                    )

                    val tempDirectory = File(javaDirectory, "temp_directory")
                    copyFiles(
                        fromPath = oldDirectory.absolutePath,
                        toPath = tempDirectory.absolutePath
                    )
                    oldDirectory.parentFile?.parentFile?.deleteRecursively()
                    newDirectory.mkdirs()
                    copyFiles(
                        fromPath = tempDirectory.absolutePath,
                        toPath = newDirectory.absolutePath
                    )
                    tempDirectory.deleteRecursively()
                }
        }
    }

    private fun renamePackageNameWithinFiles() {
        showMessage("=> 🔎 Renaming package name within files...")
        File(projectPath)
            .walk()
            .filter { it.name.endsWithAny(".kt", ".xml", ".gradle.kts") }
            .forEach { filePath ->
                rename(
                    sourcePath = filePath.toString(),
                    oldValue = templatePackageName,
                    newValue = packageName
                )
            }
    }

    private fun buildProjectAndRunTests() {
        showMessage("=> 🛠️ Building project...")
        executeCommand(
            "sh",
            "$projectPath${fileSeparator}gradlew",
            "-p",
            "$projectPath",
            "assembleDebug"
        )
        showMessage("=> 🚓 Running tests...")
        executeCommand(
            "sh",
            "$projectPath${fileSeparator}gradlew",
            "-p",
            "$projectPath",
            ":app:testStagingDebugUnitTest",
            ":data:testDebugUnitTest",
            ":domain:test"
        )
        showMessage("=> 🚀 Done! The project is ready for development")
    }

    private fun copyFiles(fromPath: String, toPath: String) {
        val targetFolder = File(toPath)
        val sourceFolder = File(fromPath)
        sourceFolder.copyRecursively(targetFolder, true) { file, exception ->
            showMessage(exception?.message ?: "Error copying files")
            return@copyRecursively OnErrorAction.TERMINATE
        }
    }

    /**
     * Execute a shell command
     *
     * Runtime.getRuntime().exec(String) will partition a command automatically, based on white spaces.
     * If a file path contains any white spaces, it will not be able to find the file and result in an error.
     *
     * Example: "Desktop/My Projects/android-templates" -> ["Desktop/My", "Projects/android-templates"]
     * Solution: Partition the command before passing it to Runtime.getRuntime().exec(String)
     * Reference: https://stackoverflow.com/questions/33077129/java-runtime-exec-with-white-spaces-on-path-name
     *
     * @param [command]: A command partitioned into multiple arguments
     */
    private fun executeCommand(vararg command: String) {
        val process = Runtime.getRuntime().exec(command)
        process.inputStream.reader().forEachLine { println(it) }
        process.errorStream.reader().forEachLine { println(it) }
        val exitValue = process.waitFor()
        if (exitValue != 0) {
            showMessage(
                message = "❌ Something went wrong! when executing command: ${command.joinToString(" ")}",
                exitAfterMessage = true,
                exitValue = exitValue
            )
        }
    }

    private fun renameAppName() {
        showMessage("=> 🔎 Renaming app name...")
        File(projectPath)
            .walk()
            .filter { it.name == "strings.xml" }
            .forEach { filePath ->
                rename(
                    sourcePath = filePath.toString(),
                    oldValue = templateAppName,
                    newValue = appName
                )
            }
    }

    private fun rename(sourcePath: String, oldValue: String, newValue: String) {
        val sourceFile = File(sourcePath)
        var sourceText = sourceFile.readText()
        sourceText = sourceText.replace(oldValue, newValue)
        sourceFile.writeText(sourceText)
    }

    private fun showMessage(
        message: String,
        exitAfterMessage: Boolean = false,
        exitValue: Int = 0
    ) {
        println("\n${message}\n")
        if (exitAfterMessage) System.exit(exitValue)
    }

    private fun String.uppercaseEveryFirstCharacter(): String {
        return this.split(SEPARATOR_SPACE).joinToString(separator = SEPARATOR_SPACE) { string ->
            string.replaceFirstChar { it.uppercase() }
        }
    }

    private fun String.getStringWithoutSpace(): String {
        return this.replace(SEPARATOR_SPACE, "")
    }

    private fun String.endsWithAny(vararg suffixes: String): Boolean {
        return suffixes.any { endsWith(it) }
    }

    private fun File.loadProperties(fileName: String): Properties {
        val properties = Properties()
        val propertiesFile = File(this, fileName)

        if (propertiesFile.isFile) {
            properties.load(propertiesFile.inputStream())
        }
        return properties
    }
}

NewProject.generate(args)
