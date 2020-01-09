package com.juliuskrah.cdc;

import java.io.IOException;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.context.event.StartupEvent;
import io.micronaut.core.io.ResourceLoader;
import io.micronaut.core.io.ResourceResolver;
import io.micronaut.core.io.scan.ClassPathResourceLoader;
import io.micronaut.runtime.event.annotation.EventListener;
import io.reactiverse.reactivex.pgclient.PgPool;

/**
 * Initializes the database
 * 
 * @author Julius Krah
 */
@Singleton
public class Initialize {
    private final PgPool client;
    private static final Logger log = LoggerFactory.getLogger(Initialize.class);

    public Initialize(PgPool client) {
        this.client = client;
    }

    /**
     * Creates the database tables on application start
     */
    @EventListener
    public void onStartUp(StartupEvent event) {
        log.info("Initializing the database");
        ResourceLoader loader = new ResourceResolver().getLoader(ClassPathResourceLoader.class).get();
        var resourceStream = loader.getResourceAsStream("classpath:schema.sql");
        if (resourceStream.isPresent()) {
            try(var stream = resourceStream.get()) {
                var sql = new String(stream.readAllBytes());
                log.debug("SQL: {}", sql);
                client.query(sql, ar -> {
                    if(ar.succeeded()) {
                        var rows = ar.result();
                        log.info("{} rows updated", rows.rowCount());
                    } else {
                        log.error("Could not execute statement", ar.cause());
                    }
                });
            } catch (IOException e) {
                log.error("unable to read file", e);
            }
        }
    }
}