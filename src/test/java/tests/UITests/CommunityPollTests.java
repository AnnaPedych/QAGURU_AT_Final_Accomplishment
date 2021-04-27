package tests.UITests;

import api.Auth;
import com.codeborne.selenide.Condition;
import customAnnotations.JiraIssue;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Cookie;
import tests.TestBase;
import tests.TestData;

import java.util.Map;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("ui")
@Owner("Anna Pedych")
@Feature("Customer interaction")
public class CommunityPollTests extends TestBase {

    @Test
    @Disabled
    @Order(2)
    @JiraIssue("QC3-27")
    @Story("User voting")
    @DisplayName("Vote as logged in user")
    public void voteAsLoggedInUserTest() {
        Map<String, String> cookies = new Auth().getAuthorizedCookies("qaguru@qa.guru", "qaguru@qa.guru1");
        step("Get cookies", () -> {
            open("http://demowebshop.tricentis.com/Themes/DefaultClean/Content/images/logo.png");
            getWebDriver().manage().addCookie(new Cookie("Nop.customer", cookies.get("Nop.customer")));
            getWebDriver().manage().addCookie(new Cookie("NOPCOMMERCE.AUTH", cookies.get("NOPCOMMERCE.AUTH")));
            getWebDriver().manage().addCookie(new Cookie("ARRAffinity", cookies.get("ARRAffinity")));
        });
        step("Vote", () -> {
            open("");
            $$(".poll-options li input").get(TestData.voteOption).click();
            $(".vote-poll-button").click();
        });
        step("Verify vote results are visible", () -> {
            $(".poll-results").shouldBe(Condition.visible);
        });
    }

    @Test
    @Order(1)
    @JiraIssue("QC3-27")
    @Story("User voting")
    @DisplayName("Validation on unauthorized voting")
    public void voteAsAnonymousUserTest() {
        Map<String, String> cookies = new Auth().getAnonymousCookies();
        step("Get cookies", () -> {
            open("http://demowebshop.tricentis.com/Themes/DefaultClean/Content/images/logo.png");
            getWebDriver().manage().addCookie(new Cookie("Nop.customer", cookies.get("Nop.customer")));
            getWebDriver().manage().addCookie(new Cookie("ARRAffinity", cookies.get("ARRAffinity")));
        });
        step("Vote", () -> {
            open("");
            $$(".poll-options li input").get(TestData.voteOption).click();
            $(".vote-poll-button").click();
        });
        step("Check validation message is shown", () -> {
            $(".poll-vote-error").should(Condition.matchText("Only registered users can vote."));
        });
    }
}
