import spock.lang.Stepwise
import geb.spock.GebReportingSpec

@Stepwise
public class GebSpec extends GebReportingSpec {

    def 'every step ends with report method execution'() {
        go 'http://www.gebish.org/'

        expect:
        title == 'Geb - Very Groovy Browser Automation'
        Class.forName('ru.d10xa.allure.GlobalAllureExtension') != null
    }

}
