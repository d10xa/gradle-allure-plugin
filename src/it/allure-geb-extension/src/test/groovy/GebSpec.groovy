import geb.spock.GebReportingSpec
import spock.lang.Stepwise

@Stepwise
public class GebSpec extends GebReportingSpec {

    def 'every step ends with report method execution'() {
        go 'file://' + new File('page-with-title.html').absolutePath

        expect:
        title == 'Geb - Very Groovy Browser Automation'
        Class.forName('ru.d10xa.allure.GlobalAllureExtension') != null
    }

}
