package daggerok

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ImportResource

@SpringBootApplication
@ImportResource("classpath:/config/spring-integration.xml")
class App

fun main(args: Array<String>) {
  runApplication<App>(*args)
}
