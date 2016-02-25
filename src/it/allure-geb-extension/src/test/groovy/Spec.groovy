@spock.lang.Stepwise
public class Spec extends geb.spock.GebReportingSpec {

    def 'every step ends with report method execution'() {
        go 'http://www.gebish.org/'

        expect:
        title == 'Geb - Very Groovy Browser Automation'
    }

    def 'extension dependency resolved'() {
        expect:
        Class.forName('ru.d10xa.allure.GlobalAllureExtension') != null
    }

    def 'html file found'() {
        String html = new File('build/allure-results')
                .listFiles()
                .find { it.name.endsWith 'html' }
                .text

        expect:
        html.contains('Geb - Very Groovy Browser Automation')
    }

}
