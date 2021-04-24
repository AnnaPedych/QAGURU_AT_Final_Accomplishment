package config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:config/testmode.properties"
})
public interface TestModeConfig extends Config{
    @Key("browser.name")
    String browserName();

    @Key("browser.version")
    String browserVersion();

    @Key("is.remote")
    boolean isRemote();
}
