package za.co.discovery.assignment.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class TestResourceConfig {

    @Bean
    public File getResource() {
        ClassLoader classLoader = this.getClass().getClassLoader();
        File file = new File(classLoader.getResource("worksheetTest.xlsx").getFile());

        return file;
    }
}
