import spock.lang.Specification

public class Spec extends Specification {
    def 'spec'() {
        expect:
        System.getProperty('allure.results.directory') != null
    }
}
