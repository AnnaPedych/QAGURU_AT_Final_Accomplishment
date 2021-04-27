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
@Tags({@Tag("combined"), @Tag("wishlist")})
@Owner("Anna Pedych")
@Feature("Wishlist development")
public class AddToWishListTests extends TestBase {

    @Test
    @Order(1)
    @JiraIssue("QC3-27")
    @Story("Anonymous wishlist usage")
    @DisplayName("Anonymous user adds product to wishlist")
    void addToWishListAnonymousTest() {
        Map<String, String> cookies = new Auth().getAnonymousCookies();
        step("Get cookies", () -> {
                    open("http://demowebshop.tricentis.com/Themes/DefaultClean/Content/images/logo.png");
                    getWebDriver().manage().addCookie(new Cookie("Nop.customer", cookies.get("Nop.customer")));
                    getWebDriver().manage().addCookie(new Cookie("ARRAffinity", cookies.get("ARRAffinity")));
                });
        step("Verify that wishlist is empty", () -> {
                    open("");
                    $("a[href='/wishlist'] .wishlist-qty").shouldHave(text("(0)"));
                });
        step("Add product to wishlist", () -> {
                    given()
                            .filter(filters().customTemplates())
                            .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                            .cookies(cookies)
                            .body("product_attribute_34_7_12=32&addtocart_34.EnteredQuantity=1")
                            .when()
                            .post("/addproducttocart/details/34/2")
                            .then()
                            .statusCode(200)
                            .log().body()
                            .body("success", is(true))
                            .body("updatetopwishlistsectionhtml", equalTo("(1)"))
                            .extract().response();
                });
        step("Check that 1 product is present in the wishlist", () -> {
            open("");
            $("a[href='/wishlist'] .wishlist-qty").shouldHave(text("(1)"));
        });
    }

    @Test
    @Order(2)
    @JiraIssue("QC3-27")
    @Story("Authorized wishlist usage")
    @DisplayName("User adds product to wishlist after log in")
    void addToWishListLoggedInTest() {
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

        String initialWishListValue = new TestBase().getInitialWishlistCount();
        int initialWishListCount = Integer.parseInt(initialWishListValue.substring(1, initialWishListValue.length() - 1));

        step("Add product to the wishlist and verify it is summed to wishlist products count", () -> {
            String response =
                    given()
                            .filter(filters().customTemplates())
                            .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                            .cookies(cookies)
                            .body("product_attribute_34_7_12=32&addtocart_34.EnteredQuantity=1")
                            .when()
                            .post("/addproducttocart/details/34/2")
                            .then()
                            .statusCode(200)
                            .log().body()
                            .body("success", is(true))
                            .body("updatetopwishlistsectionhtml", is(("(" + (initialWishListCount + 1) + ")")))
                            .extract().response().asString();

            JSONParser parser = new JSONParser();
            JSONObject JSONResponse = (JSONObject) parser.parse(response);
            String wishListCount = (String) JSONResponse.get("updatetopwishlistsectionhtml");

            open("");
            $("a[href='/wishlist'] .wishlist-qty").shouldHave(text((wishListCount)));
        });
    }
}
