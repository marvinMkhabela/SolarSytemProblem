package za.co.discovery.assignment.configurations;

import org.apache.derby.jdbc.BasicEmbeddedDataSource40;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

@Configuration
public class DataSourceConfig {

    @Bean
    @Autowired
    public DataSource dataSource() {

        BasicEmbeddedDataSource40 dataSource = new BasicEmbeddedDataSource40();
        dataSource.setConnectionAttributes("create=true");
        dataSource.setDatabaseName("DiscoveryTravel");
        dataSource.setUser("marvin");
        dataSource.setPassword("password");

        try{
            dataSource.getConnection();
        }
        catch (SQLException e) {
            System.out.println("Could not acquire connection to production DB");
            System.exit(-1);
        }

        return dataSource;
    }

    @Bean
    @Qualifier("hibernateProperties")
    public Properties properties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.DerbyTenSevenDialect");
        properties.setProperty("hibernate.show_sql", "false");
        properties.setProperty("hibernate.hbm2ddl.auto", "create");

        return properties;
    }

}
