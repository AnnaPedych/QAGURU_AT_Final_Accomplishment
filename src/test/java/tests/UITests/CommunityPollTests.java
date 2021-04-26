package tests.UITests;

import api.Auth;
import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Cookie;
import tests.TestBase;
import tests.TestData;

import java.util.Map;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("ui")
public class CommunityPollTests extends TestBase {
    @Test
    @Disabled
    @Order(2)
    public void voteAsLoggedInUserTest() {
        Map<String, String> cookies = new Auth().getAuthorizedCookies("qaguru@qa.guru", "qaguru@qa.guru1");

        open("http://demowebshop.tricentis.com/Themes/DefaultClean/Content/images/logo.png");
        getWebDriver().manage().addCookie(new Cookie("Nop.customer", cookies.get("Nop.customer")));
        getWebDriver().manage().addCookie(new Cookie("NOPCOMMERCE.AUTH", cookies.get("NOPCOMMERCE.AUTH")));
        getWebDriver().manage().addCookie(new Cookie("ARRAffinity", cookies.get("ARRAffinity")));

        open("");
        $$(".poll-options li input").get(TestData.voteOption).click();
        $(".vote-poll-button").click();
        $(".poll-results").shouldBe(Condition.visible);
    }

    @Test
    @Order(1)
    public void voteAsAnonymousUserTest() {
        Map<String, String> cookies = new Auth().getAnonymousCookies();

        open("http://demowebshop.tricentis.com/Themes/DefaultClean/Content/images/logo.png");
        getWebDriver().manage().addCookie(new Cookie("Nop.customer", cookies.get("Nop.customer")));
        getWebDriver().manage().addCookie(new Cookie("ARRAffinity", cookies.get("ARRAffinity")));

        open("");
        $$(".poll-options li input").get(TestData.voteOption).click();
        $(".vote-poll-button").click();
        $(".poll-vote-error").should(Condition.matchText("Only registered users can vote."));
    }
}
