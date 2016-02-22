package ru.d10xa.allure.extension

import groovy.transform.InheritConstructors
import org.apache.commons.io.FileUtils
import org.spockframework.runtime.extension.AbstractAnnotationDrivenExtension
import org.spockframework.runtime.extension.AbstractMethodInterceptor
import org.spockframework.runtime.extension.IMethodInvocation
import org.spockframework.runtime.model.FieldInfo
import org.spockframework.runtime.model.SpecInfo
import spock.lang.Specification

class TestProjectDirExtension extends AbstractAnnotationDrivenExtension<TestProjectDir> {

    private TestProjectDir annotation
    private FieldInfo field

    @Override
    void visitFieldAnnotation(TestProjectDir annotation, FieldInfo field) {
        this.field = field
        this.annotation = annotation
        AbstractTestProjectDirInterceptor interceptor
        if (field.shared) {
            interceptor = new SharedTestProjectDirInterceptor(annotation, field)
        } else {
            interceptor = new TestProjectDirInterceptor(annotation, field)
        }
        interceptor.install(field.parent.getTopSpec())
    }

}

abstract class AbstractTestProjectDirInterceptor extends AbstractMethodInterceptor {
    protected TestProjectDir annotation
    protected FieldInfo field

    AbstractTestProjectDirInterceptor(TestProjectDir annotation, FieldInfo field) {
        this.annotation = annotation
        this.field = field
    }

    void cloneDir(IMethodInvocation invocation) {
        def to = new File("build/gradle-testkit", annotation.dir())
        def from = new File("src/it", annotation.dir())
        assert from.isDirectory()
        if(to.exists()){
            FileUtils.cleanDirectory(to)
        }
        FileUtils.copyDirectory(from, to)
        getSpecification(invocation)."$field.name" = to
        invocation.proceed()
    }

    private static final Specification getSpecification(IMethodInvocation invocation) {
        invocation.instance ?: invocation.sharedInstance
    }

    abstract void install(SpecInfo specInfo)

}

@InheritConstructors
class TestProjectDirInterceptor extends AbstractTestProjectDirInterceptor {

    @Override
    void interceptSetupMethod(IMethodInvocation invocation) {
        cloneDir invocation
    }

    void install(SpecInfo spec) {
        spec.setupInterceptors.add this
    }

}

@InheritConstructors
class SharedTestProjectDirInterceptor extends AbstractTestProjectDirInterceptor {

    @Override
    void interceptSetupSpecMethod(IMethodInvocation invocation) {
        cloneDir invocation
    }

    void install(SpecInfo spec) {
        spec.setupSpecInterceptors.add this
    }

}
