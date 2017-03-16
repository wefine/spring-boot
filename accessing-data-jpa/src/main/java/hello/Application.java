package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.time.LocalDateTime;

@EntityScan(
        basePackageClasses = { Application.class, Jsr310JpaConverters.class }
)
@SpringBootApplication
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Bean
    public CommandLineRunner demo(CustomerRepository repository) {
        return (args) -> {
            repository.deleteAll();
            LocalDateTime now = LocalDateTime.now();
            // save a couple of customers
            repository.save(new Customer("Jack", "Bauer", now.minusDays(5)));
            repository.save(new Customer("Chloe", "O'Brian", now.minusMonths(2)));
            repository.save(new Customer("Kim", "Bauer", now.minusMonths(3)));
            repository.save(new Customer("David", "Palmer", now.minusYears(1)));
            repository.save(new Customer("Michelle", "Dessler", now.minusDays(2)));

            // fetch all customers
            log.info("Customers found with findAll():");
            log.info("-------------------------------");
            for (Customer customer : repository.findAll()) {
                log.info(customer.toString());
            }
            log.info("");

            // fetch customers by last name
            log.info("Customer found with findByLastName('Bauer'):");
            log.info("--------------------------------------------");
            for (Customer bauer : repository.findByLastName("Bauer")) {
                log.info(bauer.toString());
            }
            log.info("");
        };
    }

}
