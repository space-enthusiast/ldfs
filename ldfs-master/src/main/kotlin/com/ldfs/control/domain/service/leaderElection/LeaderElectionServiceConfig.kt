package com.ldfs.control.domain.service.leaderElection

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class LeaderElectionServiceConfig {
    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }
}
