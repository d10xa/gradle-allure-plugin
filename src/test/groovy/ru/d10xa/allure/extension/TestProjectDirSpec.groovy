package ru.d10xa.allure.extension

import spock.lang.Shared
import spock.lang.Specification

class TestProjectDirSpec extends Specification {

    @TestProjectDir(dir = "allure-report-task", clean = false, copy = false)
    @Shared
    File testProjectDirectory

    def 'expect shared test project directory exists'() {
        expect:
        testProjectDirectory.exists()
        testProjectDirectory.isDirectory()
    }

}
