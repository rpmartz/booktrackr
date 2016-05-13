package com.ryanpmartz.booktrackr.config;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableMetrics
@Profile(value = {"dev", "prod"})
public class MetricsConfig extends MetricsConfigurerAdapter {

    @Value("${graphite.host}")
    private String graphiteHost;

    @Value("${graphite.port}")
    private int graphitePort;

    @Value("${graphite.apiKey}")
    private String graphiteApiKey;

    @Override
    public void configureReporters(MetricRegistry metricRegistry) {

        registerReporter(Slf4jReporter.forRegistry(metricRegistry)
                .outputTo(LoggerFactory.getLogger("metrics"))
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build()).start(1, TimeUnit.MINUTES);

        // set DNS ttl to 60 per Hosted Graphite documentation
        java.security.Security.setProperty("networkaddress.cache.ttl", "60");
        Graphite graphite = new Graphite(new InetSocketAddress(graphiteHost, graphitePort));

        registerReporter(GraphiteReporter.forRegistry(metricRegistry)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .prefixedWith(graphiteApiKey)
                .build(graphite)).start(1, TimeUnit.MINUTES);
    }
}
