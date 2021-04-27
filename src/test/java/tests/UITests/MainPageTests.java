package tests.UITests;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBase;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static helpers.DriverHelper.getConsoleLogs;
import static io.qameta.allure.Allure.step;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

@Tag("ui")
public class MainPageTests extends TestBase {

    @Test
    public void checkAllBlocksLoadedTest() {
        step("Verify all blocks are visible", () -> {
            open("");
            $("img[alt='Tricentis Demo Web Shop']").shouldBe(Condition.visible);
            $(".header-links-wrapper").shouldBe(Condition.visible);
            $(".search-box").shouldBe(Condition.visible);
            $(".header-menu").shouldBe(Condition.visible);
            $(".leftside-3").shouldBe(Condition.visible);
            $(".rightside-3").shouldBe(Condition.visible);
            $(".center-3").shouldBe(Condition.visible);
            $(".footer").shouldBe(Condition.visible);
        });
    }

    @Test
    @Tag("flaky")
    @DisplayName("Console log should not contain errors")
    void checkConsoleLogErrorsTest() {
        step("Open test Url", () -> open(""));
        step("Verify there are no errors in console log", () -> {
            String consoleLogs = getConsoleLogs();
            assertThat(consoleLogs, not(containsString("SEVERE")));
        });
    }
}
