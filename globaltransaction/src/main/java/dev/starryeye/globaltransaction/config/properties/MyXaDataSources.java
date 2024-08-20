package dev.starryeye.globaltransaction.config.properties;

import com.atomikos.spring.AtomikosDataSourceBean;
import dev.starryeye.globaltransaction.config.properties.sub.MyAtomikosDataSourceBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties("spring.datasource.xa")
public class MyXaDataSources {

    private final MyAtomikosDataSourceBean memo;
    private final MyAtomikosDataSourceBean todo;

    @ConstructorBinding
    public MyXaDataSources(MyAtomikosDataSourceBean memo, MyAtomikosDataSourceBean todo) {
        this.memo = memo;
        this.todo = todo;

    }

    public AtomikosDataSourceBean getMemo() {
        return memo.createAtomikosDataSource();
    }

    public AtomikosDataSourceBean getTodo() {
        return todo.createAtomikosDataSource();
    }
}
