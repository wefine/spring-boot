package hello;

import hello.storage.StorageProperties;
import hello.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
        // 在每次启动时都删除原来的文件，就当仅用于测试场景；生产环境就当入库。
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
        };
    }
}
