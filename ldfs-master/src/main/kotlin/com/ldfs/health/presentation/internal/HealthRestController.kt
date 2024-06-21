package com.ldfs.health.presentation.internal

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping(path = ["/health"], produces = [MediaType.TEXT_PLAIN_VALUE])
@RestController
class HealthRestController {
    @GetMapping("ok")
    fun hello() = "ok"
}
