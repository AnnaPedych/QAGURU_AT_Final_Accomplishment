package config;

import org.aeonbits.owner.ConfigFactory;

public class ConfigHelper {

    public static String getTestUsername() {
        return getAuthorizationConfig().testUsername();
    }

    public static String getTestPassword() {
        return getAuthorizationConfig().testPassword();
    }

    public static String getWebRemoteDriver() {
        // https://%s:%s@selenoid.autotests.cloud/wd/hub/
        return String.format(System.getProperty("web.remote.driver"),
                getWebConfig().webRemoteDriverUser(),
                getWebConfig().webRemoteDriverPassword());
    }

    public static boolean isRemoteWebDriver() {
        return System.getProperty("web.remote.driver") != null;
    }

    public static String getWebVideoStorage() {
        return System.getProperty("video.storage");
    }

    public static boolean isVideoOn() {
        return getWebVideoStorage() != null;
    }

    private static WebConfig getWebConfig() {
        return ConfigFactory.newInstance().create(
                WebConfig.class, System.getProperties());
    }

    private static AuthorizationConfig getAuthorizationConfig() {
        return ConfigFactory.newInstance().create(
                    AuthorizationConfig.class, System.getProperties());
    }
}
