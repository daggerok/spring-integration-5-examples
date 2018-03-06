package daggerok.rest

import daggerok.App
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ImportResource
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerResponse.accepted
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono

// Abstract endpoint will be provided by spring-integration
interface MyGateway {
  fun process(message: String): Unit
}

// spring-integration will pass messaging to that registered endpoint service activator
@Service("receiver")
class MyReceiver {
  fun handle(payload: String): Unit = println("handling: '$payload'")
}

@Configuration
@ComponentScan(basePackageClasses = [App::class])
@ImportResource("classpath:/config/spring-integration.xml")
class WebfluxRoutesConfig(val gw: MyGateway) {

  fun run() = ApplicationRunner {
    gw.process("Hello!")
  }

  @Bean
  fun routes() = router {

    ("/**").nest {

      contentType(MediaType.APPLICATION_JSON_UTF8)

      GET("/") {
        val map = mapOf("hello" to "world")
        ok().body(
            Mono.just(map), map.javaClass
        )
      }

      POST("/") {
        accepted().body(
            it.bodyToMono(String::class.java)
                .map { gw.process(it) }
                .map { "done" }, String::class.java
        )
      }
    }
  }
}
