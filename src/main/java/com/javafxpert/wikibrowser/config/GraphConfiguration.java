/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.javafxpert.wikibrowser.config;

import com.javafxpert.wikibrowser.model.conceptmap.ItemRepository;
import org.neo4j.ogm.session.SessionFactory;
import org.neo4j.server.security.auth.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.server.Neo4jServer;
import org.springframework.data.neo4j.server.RemoteServer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Manages the configuration for a Neo4j graph database server
 *
 * @author Kenny Bastani
 */
@EnableNeo4jRepositories(basePackageClasses = {ItemRepository.class})
@EnableTransactionManagement
@Configuration
public class GraphConfiguration extends Neo4jConfiguration {

    @Value("${spring.neo4j.host}")
    private String host;

    @Value("${spring.neo4j.port}")
    private String port;

    @Bean
    public Neo4jServer neo4jServer() {
      return new RemoteServer(String.format("http://%s:%s", host, port));
    }

    @Bean
    public SessionFactory getSessionFactory() {
      return new SessionFactory(ItemRepository.class.getPackage().getName());
    }
}

