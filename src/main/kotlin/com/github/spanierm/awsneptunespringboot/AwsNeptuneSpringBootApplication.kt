package com.github.spanierm.awsneptunespringboot

import org.apache.tinkerpop.gremlin.driver.Cluster
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection
import org.apache.tinkerpop.gremlin.structure.T
import org.apache.tinkerpop.gremlin.structure.util.empty.EmptyGraph
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component


@SpringBootApplication
class AwsNeptuneSpringBootApplication

fun main(args: Array<String>) {
    runApplication<AwsNeptuneSpringBootApplication>(*args)
}

//@Component
class GremlinDirectConnection(
        @Value("\${gremlin.endpoint}") private val endpoint: String,
        @Value("\${gremlin.port}") private val port: String
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        val cluster = Cluster.build()
                .addContactPoint(endpoint).port(port.toInt())
                .create()

        val graph = EmptyGraph.instance()
                .traversal()
                .withRemote(DriverRemoteConnection.using(cluster))

        graph.V().drop().iterate()
        graph.E().drop().iterate()

        graph.addV("Person")
                .property("Name", "Justin")
                .next()

        graph.addV("Custom Label")
                .property(T.id, "CustomId1")
                .property("name", "Custom id vertex 1")
                .next()
        graph.addV("Custom Label")
                .property(T.id, "CustomId2")
                .property("name", "Custom id vertex 2")
                .next()

        graph.addE("Edge Label")
                .from(graph.V("CustomId1"))
                .to(graph.V("CustomId2"))
                .next()

        graph.V()
                .limit(3)
                .valueMap<String>()
                .forEachRemaining { e -> System.out.println(e) }

        cluster.close()
    }
}

@Component
class SpringDataGremlinConnection(
        @Autowired
        val personRepository: PersonRepository
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        val testUser = Person("PERSON_ID", "PERSON_NAME", "PERSON_AGE")
        personRepository.deleteAll()
        personRepository.save(testUser)
    }
}
