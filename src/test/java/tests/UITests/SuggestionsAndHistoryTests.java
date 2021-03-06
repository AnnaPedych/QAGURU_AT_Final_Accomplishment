package tests.UITests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.WebDriverRunner;
import customAnnotations.JiraIssue;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBase;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("ui")
@Owner("Anna Pedych")
@Feature("Search")
public class SuggestionsAndHistoryTests extends TestBase {

    @Test
    @JiraIssue("QC3-27")
    @Story("Search suggestions")
    @DisplayName("Search suggestion saves item to history")
    public void verifySearchSuggestionAndHistoryTest() {
        step("Type jeans into search field", () -> {
            open("");
            $("#small-searchterms").val("jeans");
        });
        step("Click on search suggestion", () -> $(".ui-autocomplete li").click());
        step("Verify that user was redirected to correct page", () -> {
            String currentUrl = WebDriverRunner.getWebDriver().getCurrentUrl();
            assertEquals("http://demowebshop.tricentis.com/blue-jeans", currentUrl);
            $(".product-name h1").shouldHave(Condition.text("Blue Jeans"));
        });
        step("Return to the main page", () -> open(""));
        step("Verify search history is extended with Jeans product", () -> $(".block-recently-viewed-products").$("a[href='/blue-jeans']").shouldBe(Condition.visible));
    }
}
