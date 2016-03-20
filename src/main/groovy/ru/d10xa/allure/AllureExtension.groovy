package ru.d10xa.allure

import groovy.transform.CompileStatic
import org.gradle.api.Project

@CompileStatic
class AllureExtension {

    public static final String NAME = "allure"

    AllureExtension(Project project) {
        allureResultsDir = new File(project.buildDir, "/allure-results").absolutePath
        allureReportDir = new File(project.buildDir, "/allure-report").absolutePath
        allureBundleVersion = "1.4.22"
        this.geb = false;
    }

    String allureResultsDir

    String allureReportDir

    String allureBundleVersion

    boolean geb

}
