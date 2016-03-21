package ru.d10xa.allure

import groovy.transform.CompileStatic
import org.gradle.api.Project

@CompileStatic
class AllureExtension {

    public static final String NAME = "allure"

    AllureExtension(Project project) {
        this.allureResultsDir = new File(project.buildDir, "/allure-results").absolutePath
        this.allureReportDir = new File(project.buildDir, "/allure-report").absolutePath
        this.allureBundleVersion = "1.4.22"
        this.geb = false;
    }

    String allureResultsDir

    String allureReportDir

    String allureBundleVersion

    boolean geb

}
