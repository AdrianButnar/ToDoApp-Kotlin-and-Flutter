package src
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.stereotype.Component


@EnableResourceServer
@EnableJpaRepositories
@SpringBootApplication

class Application
private val log = LoggerFactory.getLogger(Application::class.java)

//@Bean
//@Autowired
//fun init(repository: ItemRepository) = CommandLineRunner {
//    repository.delete(ShoppingItem(1,"a","ha"))
//    repository.save(ShoppingItem(1,"Tomatoes", "1kg"))
//    repository.save(ShoppingItem(2,"Potatoes", "2kg"))
//    // fetch all customers
//    log.info("Customers found with findAll():")
//    log.info("-------------------------------")
//    repository.findAll().forEach { log.info(it.toString()) }
//    log.info("")
//
//}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}

