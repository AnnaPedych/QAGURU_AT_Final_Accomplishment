package templates;
import io.qameta.allure.restassured.AllureRestAssured;

public class ReportTemplate {
    private static final AllureRestAssured FILTER = new AllureRestAssured();

    private static class InitLogFilter {
        private static final ReportTemplate logFilter = new ReportTemplate();
    }

    private ReportTemplate() {}

    public static ReportTemplate filters() {
        return InitLogFilter.logFilter;
    }

    public AllureRestAssured customTemplates() {
        FILTER.setRequestTemplate("request.ftl");
        FILTER.setResponseTemplate("response.ftl");
        return FILTER;
    }
}