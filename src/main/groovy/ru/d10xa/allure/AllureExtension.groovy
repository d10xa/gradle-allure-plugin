package ru.d10xa.allure

import groovy.transform.CompileStatic
import org.gradle.api.Project

@CompileStatic
class AllureExtension {

    public static final String NAME = "allure"

    AllureExtension(Project project) {
        this.allureResultsDir = new File(project.buildDir, "/allure-results").absolutePath
        this.allureReportDir = new File(project.buildDir, "/allure-report").absolutePath
    }

    String allureResultsDir

    String allureReportDir

    String allureVersion = "1.4.24.RC3"

    String aspectjVersion = "1.8.9"

    String allureSpockGebExtensionVersion = "0.2.1"

    String allureJunitAspectjAdaptorVersion = "0.1.1"

    String configuration = "testCompile"

    boolean spock

    boolean testNG

    boolean junit

    boolean geb

    boolean aspectjweaver

    boolean clean = true

}
