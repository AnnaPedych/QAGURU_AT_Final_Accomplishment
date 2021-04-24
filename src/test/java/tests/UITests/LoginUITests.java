package tests.UITests;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import tests.TestBase;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static config.ConfigHelper.getTestPassword;
import static config.ConfigHelper.getTestUsername;
import static io.qameta.allure.Allure.step;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginUITests extends TestBase {

    @Test
    @Order(2)
    public void enterValidCredentialsTest() {
        step(" ", () -> {
            open("");
            $("a[href='/login']").click();
            $("html").shouldHave(text("Welcome, Please Sign In!"));
            $("#Email").setValue(getTestUsername());
            $("#Password").setValue(getTestPassword()).pressEnter();
        });
        step(" ", () -> {
            $(".account").shouldHave(text(getTestUsername()));
        });
    }

    @Test
    @Order(1)
    public void enterInvalidCredentialsTest() {
        step(" ", () -> {
            open("");
            $("a[href='/login']").click();
            $("html").shouldHave(text("Welcome, Please Sign In!"));
            $("#Email").setValue(" ");
            $("#Password").setValue(" ").pressEnter();
        });
        step(" ", () -> {
            $(".validation-summary-errors span").shouldHave(text("Login was unsuccessful. Please correct the errors and try again."));
        });
    }
}
