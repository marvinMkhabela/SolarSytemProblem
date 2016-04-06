package za.co.discovery.assignment.Configuration;

import org.apache.derby.jdbc.BasicEmbeddedDataSource40;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

@Configuration
public class DataSourceConfig {

    @Bean
    @Autowired
    @Profile("default")
    public DataSource dataSource() {

        BasicEmbeddedDataSource40 dataSource = new BasicEmbeddedDataSource40();
        dataSource.setConnectionAttributes("create=true");
        dataSource.setDatabaseName("DiscoverySolarSystem");
        dataSource.setUser("marvin");
        dataSource.setPassword("password");

        try{
            dataSource.getConnection();
        }
        catch (SQLException e) {
            System.out.println("Could not acquire connection to production DB");
        }

        return dataSource;
    }

    @Bean
    @Qualifier("hibernateProperties")
    public Properties properties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.DerbyTenSevenDialect");
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.hbm2ddl.auto", "create");

        return properties;
    }

}
