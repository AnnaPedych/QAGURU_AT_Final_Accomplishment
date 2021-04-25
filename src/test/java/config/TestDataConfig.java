package config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:config/testdata.properties"
})
public interface TestDataConfig extends Config{
    @Key("test.username")
    String testUsername();

    @Key("test.password")
    String testPassword();

    @Key("web.url")
    String webUrl();

    @Key("api.url")
    String apiUrl();
}