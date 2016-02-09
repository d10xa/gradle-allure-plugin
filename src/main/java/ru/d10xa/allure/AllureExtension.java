package ru.d10xa.allure;

import org.gradle.api.Project;

import java.io.File;

public class AllureExtension {

    public static final String NAME = "allure";

    private String allureResultsDir;

    private String allureReportDir;

    private String allureBundleVersion;

    public AllureExtension(Project project) {
        this.allureResultsDir = new File(project.getBuildDir(), "/allure-results").getAbsolutePath();
        this.allureReportDir = new File(project.getBuildDir(), "/allure-report").getAbsolutePath();
        this.allureBundleVersion = "1.4.22";
    }

    public String getAllureResultsDir() {
        return allureResultsDir;
    }

    public void setAllureResultsDir(String allureResultsDir) {
        this.allureResultsDir = allureResultsDir;
    }

    public String getAllureReportDir() {
        return allureReportDir;
    }

    public void setAllureReportDir(String allureReportDir) {
        this.allureReportDir = allureReportDir;
    }

    public String getAllureBundleVersion() {
        return allureBundleVersion;
    }

    public void setAllureBundleVersion(String allureBundleVersion) {
        this.allureBundleVersion = allureBundleVersion;
    }
}
