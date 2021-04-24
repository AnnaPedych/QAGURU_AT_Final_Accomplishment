package tests.combinedTests;

import api.Auth;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.Cookie;
import tests.TestBase;

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
public class AddToCartTests extends TestBase {

    @Test
    @Order(1)
    public void AddToCartAnonymousTests() {
        Map<String, String> cookies = new Auth().getAnonymousCookies();

        open("http://demowebshop.tricentis.com/Themes/DefaultClean/Content/images/logo.png");
        getWebDriver().manage().addCookie(new Cookie("Nop.customer", cookies.get("Nop.customer")));
        getWebDriver().manage().addCookie(new Cookie("ARRAffinity", cookies.get("ARRAffinity")));

        open("");
        $("a[href='/cart'] .cart-qty").shouldHave(text("(0)"));

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

        open("");
        $("a[href='/cart'] .cart-qty").shouldHave(text("(1)"));

    }

    @Test
    @Order(2)
    public void AddToCartLoggedInTests() throws ParseException {
        Map<String, String> cookies = new Auth().getAuthorizedCookies("qaguru@qa.guru", "qaguru@qa.guru1");

        open("http://demowebshop.tricentis.com/Themes/DefaultClean/Content/images/logo.png");
        getWebDriver().manage().addCookie(new Cookie("Nop.customer", cookies.get("Nop.customer")));
        getWebDriver().manage().addCookie(new Cookie("NOPCOMMERCE.AUTH", cookies.get("NOPCOMMERCE.AUTH")));
        getWebDriver().manage().addCookie(new Cookie("ARRAffinity", cookies.get("ARRAffinity")));

        open("");
        $(".account").shouldHave(text("qaguru@qa.guru"));

        String initialCartValue = new TestBase().getInitialCartCount();
        int initialCartCount = Integer.parseInt(initialCartValue.substring(1, initialCartValue.length() - 1));

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
    }
}