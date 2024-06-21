package com.ldfs

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LdfsApplication

fun main(args: Array<String>) {
    runApplication<LdfsApplication>(*args)
}
