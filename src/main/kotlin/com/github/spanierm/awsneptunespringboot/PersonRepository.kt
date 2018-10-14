package com.github.spanierm.awsneptunespringboot

import com.microsoft.spring.data.gremlin.annotation.Edge
import com.microsoft.spring.data.gremlin.annotation.EdgeFrom
import com.microsoft.spring.data.gremlin.annotation.EdgeSet
import com.microsoft.spring.data.gremlin.annotation.EdgeTo
import com.microsoft.spring.data.gremlin.annotation.Graph
import com.microsoft.spring.data.gremlin.annotation.Vertex
import com.microsoft.spring.data.gremlin.annotation.VertexSet
import com.microsoft.spring.data.gremlin.common.GremlinConfiguration
import com.microsoft.spring.data.gremlin.config.AbstractGremlinConfiguration
import com.microsoft.spring.data.gremlin.repository.GremlinRepository
import com.microsoft.spring.data.gremlin.repository.config.EnableGremlinRepositories
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.data.annotation.Id
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

@Configuration
@EnableGremlinRepositories(basePackages = ["com.github.spanierm.awsneptunespringboot"])
class GremlinConfiguration(
        @Autowired
        private val gremlinProperties: GremlinProperties
) : AbstractGremlinConfiguration() {
    override fun getGremlinConfiguration(): GremlinConfiguration {
        return GremlinConfiguration().apply {
            endpoint = gremlinProperties.endpoint
            port = gremlinProperties.port
            username = gremlinProperties.username
            password = gremlinProperties.password
        }
    }
}

@Component
@ConfigurationProperties("gremlin")
class GremlinProperties {
    var endpoint: String? = null
    var port: String? = null
    var username: String? = null
    var password: String? = null
}

@Repository
interface PersonRepository : GremlinRepository<Person, String> {
    fun findByName(name: String): List<Person>
}

@Vertex
data class Person(
        @Id
        val id: String,
        val name: String,
        val age: String
)

@Edge
data class Relation(
        @Id
        val id: String,
        val name: String,
        @EdgeFrom
        val personFrom: Person,
        @EdgeTo
        val personTo: Person
)

@Graph
class Network(
        @Id
        private val id: String
) {
    @VertexSet
    private val vertices = mutableListOf<Person>()
    @EdgeSet
    private val edges = mutableListOf<Relation>()
}