package ru.d10xa.allure

import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

@CompileStatic
public class AllureReportTask extends JavaExec {

    public static final String NAME = "allureReport"

    public static final String ALLURE_MAIN_CLASS = "ru.yandex.qatools.allure.AllureMain"

    public static final String CONFIGURATION_NAME = "allureReport"

    private Set<Object> resultDirs

    private Object reportDir

    private final static Closure<String> RESULT_DIR_COLLECTOR = {
        if (it instanceof Project) {
            return it.extensions.getByType(AllureExtension).allureResultsDir
        }
        return it as String
    }

    public AllureReportTask() {
        this.resultDirs = new LinkedHashSet<Object>()
        this.outputs.upToDateWhen { false }
    }

    @Override
    @TaskAction
    public void exec() {
        List<String> dirs = resultDirsArguments()
                .findAll {
            def f = new File(it)
            f.directory && f.list().size() > 0
        }

        if (dirs.size() == 0) {
            logger.warn("No allure results found. The report is not generated")
            return
        }
        if (this.getReportDir() == null) {
            logger.warn("Allure report dir is null")
            return
        }
        args(dirs + getReportDir().toString())
        setMain(ALLURE_MAIN_CLASS)
        classpath(project.configurations.getByName("allureReport"))
        super.exec()
    }

    @OutputDirectory
    private File getReportDir() {
        this.@reportDir ?
                project.file(this.@reportDir.toString()) :
                project.file(allureExtension.allureReportDir)
    }

    private List<String> resultDirsArguments() {
        def arguments = this.resultDirs.collect(RESULT_DIR_COLLECTOR)

        if (logger.debugEnabled) {
            def dirs = arguments.collect { new File(it) }
            dirs.findAll { !it.directory }.each {
                logger.debug "resultsDir: $it is not directory"
            }
            dirs.findAll { it.directory && it.list().length == 0 }.each {
                logger.debug "resultsDir: $it is empty directory"
            }
        }

        if (arguments.empty) {
            arguments.add(allureExtension.allureResultsDir)
        }
        arguments
    }

    public void from(Object... results) {
        this.resultDirs = new LinkedHashSet<Object>()
        for (Object result : results) {
            this.resultDirs.add(result)
        }
    }

    public void to(Object reportDir) {
        this.reportDir = reportDir
    }

    private AllureExtension getAllureExtension() {
        return project.extensions.findByType(AllureExtension.class)
    }

}
