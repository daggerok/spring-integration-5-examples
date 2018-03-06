package daggerok

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ImportResource
import org.springframework.stereotype.Service

// Abstract endpoint will be provided by spring-integration
interface MyGateway {
  fun process(message: String): Unit
}

// spring-integration will pass messaging to that registered endpoint service activator
@Service("receiver")
class MyGatewayImpl {
  fun handle(payload: String): Unit = println("handling: '$payload'")
}

@SpringBootApplication
@ImportResource("classpath:/config/spring-integration.xml")
class App(val gw: MyGateway) : ApplicationRunner {
  override fun run(args: ApplicationArguments?) = gw.process("Hello!")
}

fun main(args: Array<String>) {
  runApplication<App>(*args)
}
