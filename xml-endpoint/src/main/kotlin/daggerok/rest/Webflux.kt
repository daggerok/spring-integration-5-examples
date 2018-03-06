package daggerok.rest

import daggerok.App
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ImportResource
import org.springframework.http.MediaType
import org.springframework.integration.channel.DirectChannel
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerResponse.accepted
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono

@Service("transformer")
class MyTransformer {
  fun transform(message: Message<*>): Message<String> {

    println("original headers:")
    println(message.headers)
    println("original payload:")
    println(message.payload)

    return MessageBuilder
        .withPayload("response of ${message.payload}")
        //.setHeader("id", message.headers["id"]) // id header is read-only
        .setHeader("correlationId", message.headers["id"])
        .build()
  }
}

@Service
class MyReceiver(val responseMessageChannel: DirectChannel) : ApplicationRunner {
  override fun run(args: ApplicationArguments?) {
    responseMessageChannel.subscribe {
      println("transformed headers:")
      println(it.headers)
      println("transformed payload:")
      println(it.payload)
    }
  }
}

@Configuration
@ComponentScan(basePackageClasses = [App::class])
@ImportResource("classpath:/config/spring-integration.xml")
class WebfluxRoutesConfig(val requestMessageChannel: DirectChannel) {
  @Bean
  fun routes() = router {

    ("/").nest {

      contentType(MediaType.APPLICATION_JSON_UTF8)

      GET("/**") {
        val map = mapOf("hello" to "world")
        ok().body(
            Mono.just(map), map.javaClass
        )
      }

      POST("/**") {
        accepted().body(
            it.bodyToMono(String::class.java)
                .map {
                  MessageBuilder
                      .withPayload(it)
                      .build()
                }
                .map { requestMessageChannel.send(it) }
                .map { "done" }, String::class.java
        )
      }
    }
  }
}
