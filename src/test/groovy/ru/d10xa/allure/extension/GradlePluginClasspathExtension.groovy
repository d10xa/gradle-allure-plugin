package ru.d10xa.allure.extension

import org.spockframework.runtime.extension.AbstractAnnotationDrivenExtension
import org.spockframework.runtime.extension.AbstractMethodInterceptor
import org.spockframework.runtime.extension.IMethodInvocation
import org.spockframework.runtime.model.FieldInfo

class GradlePluginClasspathExtension extends AbstractAnnotationDrivenExtension<GradlePluginClasspath> {

    @Override
    void visitFieldAnnotation(GradlePluginClasspath annotation, FieldInfo field) {
        field.parent.topSpec.addSetupInterceptor(new AbstractMethodInterceptor() {
            @Override
            void interceptSetupMethod(IMethodInvocation invocation) throws Throwable {
                invocation.instance."$field.name" = pluginClasspathResource.readLines().collect { new File(it) }
            }
        })
    }

    private URL getPluginClasspathResource() {
        def resource = getClass().classLoader.getResource("plugin-classpath.txt")
        if (resource == null) {
            throw new IllegalStateException("Did not find plugin classpath resource, run `testClasses` build task.")
        }
        resource
    }

}
