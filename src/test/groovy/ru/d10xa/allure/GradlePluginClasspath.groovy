package ru.d10xa.allure

import groovy.transform.CompileStatic
import org.junit.rules.ExternalResource

@CompileStatic
class GradlePluginClasspath extends ExternalResource {

    URL pluginClasspathResource

    @Override
    protected void before() throws Throwable {
        pluginClasspathResource = getClass().classLoader.getResource("plugin-classpath.txt")
        if (pluginClasspathResource == null) {
            throw new IllegalStateException("Did not find plugin classpath resource, run `testClasses` build task.")
        }
    }

    public List get() {
        pluginClasspathResource.readLines().collect { new File(it) }
    }

}
