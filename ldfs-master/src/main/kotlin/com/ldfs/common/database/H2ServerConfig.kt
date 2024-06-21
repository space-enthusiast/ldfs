package com.ldfs.common.database

import org.h2.tools.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class H2ServerConfig {
    @Bean
    fun h2TcpServer(): Server {
        return Server.createTcpServer().start()
    }
}
