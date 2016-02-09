package ru.d10xa.allure

import groovy.transform.CompileStatic
import org.junit.rules.ExternalResource

@CompileStatic
class GradlePluginClasspath extends ExternalResource {

    private URL pluginClasspathResource

    @Override
    protected void before() throws Throwable {
        this.pluginClasspathResource = getClass().classLoader.getResource("plugin-classpath.txt")
        if (this.pluginClasspathResource == null) {
            throw new IllegalStateException("Did not find plugin classpath resource, run `testClasses` build task.")
        }
    }

    public List get() {
        this.pluginClasspathResource.readLines().collect { new File(it) }
    }

}
