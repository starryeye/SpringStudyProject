package dev.starryeye.globaltransaction.config.properties;

import dev.starryeye.globaltransaction.config.properties.sub.MyAtomikosDataSourceBean;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@Getter
@ConfigurationProperties("spring.datasource.xa")
public class MyXaDataSources {

    private final MyAtomikosDataSourceBean memo;
    private final MyAtomikosDataSourceBean todo;

    @ConstructorBinding
    public MyXaDataSources(MyAtomikosDataSourceBean memo, MyAtomikosDataSourceBean todo) {
        this.memo = memo;
        this.todo = todo;

        initialize();
    }

    private void initialize() {
        memo.mysqlInitialize();
        todo.mysqlInitialize();
    }
}
