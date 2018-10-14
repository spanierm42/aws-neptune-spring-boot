package com.github.spanierm.awsneptunespringboot

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AwsNeptuneController {
    @PostMapping
    fun executeGremlinQuery(@RequestBody query: String): String {
        return query
    }
}
