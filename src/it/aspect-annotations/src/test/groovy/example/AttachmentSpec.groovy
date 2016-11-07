package example

import spock.lang.Specification
import ru.yandex.qatools.allure.annotations.Attachment

class AttachmentSpec extends Specification {

    def 'attachment'(){
        expect:
        makeAttachment() == "content"
    }

    @Attachment
    String makeAttachment() {
        "content"
    }

}
