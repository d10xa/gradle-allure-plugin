import org.junit.Assert;
import org.junit.Test;
import ru.yandex.qatools.allure.annotations.Attachment;

public class JunitTest {

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
