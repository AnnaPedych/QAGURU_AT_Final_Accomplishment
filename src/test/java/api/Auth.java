package api;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static templates.ReportTemplate.filters;

public class Auth {
    public Map<String, String> getAuthorizedCookies(String login, String password) {
        return
                given()
                        .filter(filters().customTemplates())
                        .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                        .formParam("Email", login)
                        .formParam("Password", password)
                        .when()
                        .post("/login")
                        .then()
                        .statusCode(302)
                        .log().body()
                        .extract().cookies();
    }

    public Map<String, String> getAnonymousCookies() {
        return
                given()
                        .filter(filters().customTemplates())
                        .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                        .when()
                        .get("/wishlist")
                        .then()
                        .statusCode(200)
                        .log().body()
                        .extract().cookies();
    }
}