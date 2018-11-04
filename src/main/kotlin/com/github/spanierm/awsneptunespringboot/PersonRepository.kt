package com.github.spanierm.awsneptunespringboot

import com.microsoft.spring.data.gremlin.annotation.Edge
import com.microsoft.spring.data.gremlin.annotation.EdgeFrom
import com.microsoft.spring.data.gremlin.annotation.EdgeSet
import com.microsoft.spring.data.gremlin.annotation.EdgeTo
import com.microsoft.spring.data.gremlin.annotation.Graph
import com.microsoft.spring.data.gremlin.annotation.Vertex
import com.microsoft.spring.data.gremlin.annotation.VertexSet
import com.microsoft.spring.data.gremlin.common.GremlinConfig
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
    override fun getGremlinConfig(): GremlinConfig {
        return GremlinConfig.defaultBuilder()
                .endpoint(gremlinProperties.endpoint)
                .port(gremlinProperties.port!!)
                .username(gremlinProperties.username)
                .password(gremlinProperties.password)
                .sslEnabled(gremlinProperties.sslEnabled)
                .build()
    }
}

@Component
@ConfigurationProperties("gremlin")
class GremlinProperties {
    var endpoint: String? = null
    var port: Int? = null
    var username: String? = null
    var password: String? = null
    var sslEnabled: Boolean = false
}

@Repository
interface PersonRepository : GremlinRepository<Person, String>

@Vertex
data class Person(
        @Id
        var id: String,
        var name: String,
        var age: String
)

@Edge
data class Relation(
        @Id
        var id: String,
        var name: String,
        @EdgeFrom
        var personFrom: Person,
        @EdgeTo
        var personTo: Person
)

@Graph
class Network(
        @Id
        private var id: String
) {
    @VertexSet
    private var vertices = mutableListOf<Person>()
    @EdgeSet
    private var edges = mutableListOf<Relation>()
}
