package com.example


import io.gatling.javaapi.core.*
import org.apache.kafka.clients.producer.*
import io.gatling.javaapi.core.CoreDsl.*
import java.time.Duration
import java.util.*
import java.util.concurrent.TimeUnit

class KafkaLoadSimulation : Simulation(){
    private val kafkaProducer = Producer.create(
        mapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to "org.apache.kafka.common.serialization.StringSerializer",
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to "org.apache.kafka.common.serialization.StringSerializer",
            ProducerConfig.ACKS_CONFIG to "1",
            ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG to "5000",
            ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG to "10000",
            ProducerConfig.MAX_BLOCK_MS_CONFIG to "10000"
        )
    )




    private fun randomInn(): String{
        return (1..12).map {(0..9).random()}.joinToString("")
    }



    val scn = scenario("Kafka Load Test")
        .exec { session ->
            val msgId = UUID.randomUUID().toString()
            val fullName = FullName().randomFullName()
            val inn = randomInn()
            val message = """{"msg_id":"$msgId","full_name":"$fullName","inn":"$inn"}"""
            val record = ProducerRecord("test-topic", msgId, message)

//            println("Отправка сообщения $msgId")

            val resultSession = try {
                kafkaProducer.send(record).get(5, TimeUnit.SECONDS)
//                println("Сообщение $msgId отправлено")
                session.markAsSucceeded()
            } catch (e: Exception){
                System.err.println("Ошибка отправки в Кафка: ${e.message}")
                e.printStackTrace()
                session.markAsFailed()
            }
            resultSession
        }

    init {
        setUp(
            scn.injectOpen(
                constantUsersPerSec(10.0).during(Duration.ofSeconds(5)),
                constantUsersPerSec(15.0).during(Duration.ofSeconds(5)),
                constantUsersPerSec(25.0).during(Duration.ofSeconds(5)),
                constantUsersPerSec(50.0).during(Duration.ofSeconds(5))
            )
        ).protocols()
    }

    object Producer{
        fun create(config: Map<String,String>): KafkaProducer<String, String> {

            val props = Properties()
            props.putAll(config)
            return KafkaProducer(props)
        }
    }

















}