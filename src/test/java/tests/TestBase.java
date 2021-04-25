package tests;

import com.codeborne.selenide.Configuration;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import static com.codeborne.selenide.Selenide.*;
import static helpers.AttachmentsHelper.*;
import static helpers.AttachmentsHelper.attachVideo;
import static helpers.DriverHelper.*;
import static tests.TestData.getApiUrl;
import static tests.TestData.getWebUrl;


public class TestBase {
    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = getApiUrl();
        Configuration.baseUrl = getWebUrl();
        configureDriver();
    }

    public String getInitialWishlistCount() {
        open("");
        return $("a[href='/wishlist'] .wishlist-qty").getText();
    }

    public String getInitialCartCount() {
        open("");
        return $("a[href='/cart'] .cart-qty").getText();
    }

    @AfterEach
    public void addAttachments(){
        String sessionId = getSessionId();

        attachScreenshot("Last screenshot");
        attachPageSource();
        attachAsText("Browser console logs", getConsoleLogs());

        closeWebDriver();

        if (isVideoOn()) attachVideo(sessionId);

    }
}
