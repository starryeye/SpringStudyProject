package dev.starryeye.globaltransaction.config.properties.sub;

import lombok.Getter;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.util.Properties;

@Getter
public class XaProperties {

    private final String url;
    private final String username;
    private final String password;

    @ConstructorBinding
    public XaProperties(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    // Properties로 변환하는 메서드 (AtomikosDataSourceBean에서 사용하기 위함)
    public Properties toProperties() {
        Properties properties = new Properties();
        properties.setProperty("url", this.url);
        properties.setProperty("user", this.username);
        properties.setProperty("password", this.password);
        return properties;
    }
}
