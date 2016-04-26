package za.co.discovery.assignment.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class ResourceConfig {

    @Bean
    public File getFileResource() {
        ClassLoader classLoader = this.getClass().getClassLoader();
        return new File(classLoader.getResource("worksheet.xlsx").getFile());
    }

}
