package tests;

import api.Auth;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.Cookie;

import java.util.Map;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static templates.ReportTemplate.filters;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AddToWishList extends TestBase {

    @Test
    @Order(1)
    void addToWishListAnonymousTest() {
        Map<String, String> cookies = new Auth().getAnonymousCookies();

        open("http://demowebshop.tricentis.com/Themes/DefaultClean/Content/images/logo.png");
        getWebDriver().manage().addCookie(new Cookie("Nop.customer", cookies.get("Nop.customer")));
        getWebDriver().manage().addCookie(new Cookie("ARRAffinity", cookies.get("ARRAffinity")));

        open("");
        $("a[href='/wishlist'] .wishlist-qty").shouldHave(text("(0)"));

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

        open("");
        $("a[href='/wishlist'] .wishlist-qty").shouldHave(text("(1)"));
    }

    @Test
    @Order(2)
    void addToWishListLoggedInTest() throws ParseException {
        Map<String, String> cookies = new Auth().getAuthorizedCookies("qaguru@qa.guru", "qaguru@qa.guru1");

        open("http://demowebshop.tricentis.com/Themes/DefaultClean/Content/images/logo.png");
        getWebDriver().manage().addCookie(new Cookie("Nop.customer", cookies.get("Nop.customer")));
        getWebDriver().manage().addCookie(new Cookie("NOPCOMMERCE.AUTH", cookies.get("NOPCOMMERCE.AUTH")));
        getWebDriver().manage().addCookie(new Cookie("ARRAffinity", cookies.get("ARRAffinity")));

        open("");
        $(".account").shouldHave(text("qaguru@qa.guru"));

        String initialWishListValue = new TestBase().getInitialWishlistCount();
        Integer initialWishListCount = Integer.parseInt(initialWishListValue.substring(1, initialWishListValue.length() - 1));

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
    }
}
