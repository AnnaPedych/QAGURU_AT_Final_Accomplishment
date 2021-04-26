package tests.UITests;

import org.junit.jupiter.api.*;
import tests.TestBase;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;
import static tests.TestData.getTestPassword;
import static tests.TestData.getTestUsername;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("ui")
public class LoginUITests extends TestBase {

    @Test
    @Order(2)
    public void enterValidCredentialsTest() {
        step("Login with test account credentials", () -> {
            open("");
            $("a[href='/login']").click();
            $("html").shouldHave(text("Welcome, Please Sign In!"));
            $("#Email").setValue(getTestUsername());
            $("#Password").setValue(getTestPassword()).pressEnter();
        });
        step("Verify correct user is logged in", () -> {
            $(".account").shouldHave(text(getTestUsername()));
        });
    }

    @Test
    @Order(1)
    public void enterInvalidCredentialsTest() {
        step("Log in with empty email and password", () -> {
            open("");
            $("a[href='/login']").click();
            $("html").shouldHave(text("Welcome, Please Sign In!"));
            $("#Email").setValue(" ");
            $("#Password").setValue(" ").pressEnter();
        });
        step("Verify validation message of unsuccessful login", () -> {
            $(".validation-summary-errors span").shouldHave(text("Login was unsuccessful. Please correct the errors and try again."));
        });
    }
}
