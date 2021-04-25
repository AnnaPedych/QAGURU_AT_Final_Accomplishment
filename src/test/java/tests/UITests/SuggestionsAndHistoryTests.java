package tests.UITests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBase;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("ui")
public class SuggestionsAndHistoryTests extends TestBase {

    @Test
    public void verifySearchSuggestionAndHistoryTest() {

        open("");
        $("#small-searchterms").val("jeans");
        $(".ui-autocomplete li a").shouldHave(Condition.text("Blue Jeans")).click();
        String currentUrl = WebDriverRunner.getWebDriver().getCurrentUrl();
        assertEquals("http://demowebshop.tricentis.com/blue-jeans", currentUrl);
        $(".product-name h1").shouldHave(Condition.text("Blue Jeans"));
        open("");
        $(".block-recently-viewed-products").$("a[href='/blue-jeans']").shouldBe(Condition.visible);
    }
}
