import org.testng.Assert;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Attachment;

public class TestNgTest {

    @Test
    public void testWithAttachment() {
        attachment();
        Assert.assertTrue(true);
    }

    @Attachment(value = "hello.html", type = "text/html")
    public String attachment() {
        return "<p>HELLO</p>";
    }

}
