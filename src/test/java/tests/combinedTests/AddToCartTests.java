package tests.combinedTests;

import api.Auth;
import customAnnotations.JiraIssue;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Cookie;
import tests.TestBase;

import java.util.Map;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static templates.ReportTemplate.filters;
import static tests.TestData.getTestPassword;
import static tests.TestData.getTestUsername;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tags({@Tag("combined"), @Tag("cart")})
@Owner("Anna Pedych")
@Feature("Cart development")
public class AddToCartTests extends TestBase {

    @Test
    @Order(1)
    @JiraIssue("QC3-27")
    @Story("Anonymous shopping")
    @DisplayName("Anonymous user adds product to cart")
    public void addToCartAnonymousTests() {
        Map<String, String> cookies = new Auth().getAnonymousCookies();
        step("Get cookies", () -> {
            open("http://demowebshop.tricentis.com/Themes/DefaultClean/Content/images/logo.png");
            getWebDriver().manage().addCookie(new Cookie("Nop.customer", cookies.get("Nop.customer")));
            getWebDriver().manage().addCookie(new Cookie("ARRAffinity", cookies.get("ARRAffinity")));
        });
        step("Verify that cart is empty", () -> {
            open("");
            $("a[href='/cart'] .cart-qty").shouldHave(text("(0)"));
        });
        step("Add product to cart", () -> {
            given()
                    .filter(filters().customTemplates())
                    .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                    .cookies(cookies)
                    .body("product_attribute_16_5_4=14&product_attribute_16_6_5=15&product_attribute_16_3_6=18&product_attribute_16_4_7=44&product_attribute_16_8_8=22&addtocart_16.EnteredQuantity=1")
                    .when()
                    .post("/addproducttocart/details/16/1")
                    .then()
                    .statusCode(200)
                    .log().body()
                    .body("success", is(true))
                    .body("updatetopcartsectionhtml", equalTo("(1)"))
                    .extract().response();
        });
        step("Check that 1 product is present in the cart", () -> {
            open("");
            $("a[href='/cart'] .cart-qty").shouldHave(text("(1)"));
        });
    }

    @Test
    @Order(2)
    @JiraIssue("QC3-27")
    @Story("Authorized shopping")
    @DisplayName("User adds product to cart after log in")
    public void addToCartLoggedInTests() {
        Map<String, String> cookies = new Auth().getAuthorizedCookies(getTestUsername(), getTestPassword());
        step("Get cookies", () -> {
            open("http://demowebshop.tricentis.com/Themes/DefaultClean/Content/images/logo.png");
            getWebDriver().manage().addCookie(new Cookie("Nop.customer", cookies.get("Nop.customer")));
            getWebDriver().manage().addCookie(new Cookie("NOPCOMMERCE.AUTH", cookies.get("NOPCOMMERCE.AUTH")));
            getWebDriver().manage().addCookie(new Cookie("ARRAffinity", cookies.get("ARRAffinity")));
        });
        step("Verify that correct user is logged in", () -> {
            open("");
            $(".account").shouldHave(text(getTestUsername()));
        });

        String initialCartValue = new TestBase().getInitialCartCount();
        int initialCartCount = Integer.parseInt(initialCartValue.substring(1, initialCartValue.length() - 1));

        step("Add product to the cart and verify it is summed to cart product count", () -> {
            String response =
                    given()
                            .filter(filters().customTemplates())
                            .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                            .cookies(cookies)
                            .body("product_attribute_16_5_4=14&product_attribute_16_6_5=15&product_attribute_16_3_6=18&product_attribute_16_4_7=44&product_attribute_16_8_8=22&addtocart_16.EnteredQuantity=1")
                            .when()
                            .post("/addproducttocart/details/16/1")
                            .then()
                            .statusCode(200)
                            .log().body()
                            .body("success", is(true))
                            .body("updatetopcartsectionhtml", is(("(" + (initialCartCount + 1) + ")")))
                            .extract().response().asString();

            JSONParser parser = new JSONParser();
            JSONObject JSONResponse = (JSONObject) parser.parse(response);
            String cartCount = (String) JSONResponse.get("updatetopcartsectionhtml");

            open("");
            $("a[href='/cart'] .cart-qty").shouldHave(text((cartCount)));
        });
    }
}