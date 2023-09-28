package com.mk.BackendQuiz.config;

import java.util.concurrent.Executor;
import javax.sql.DataSource;

import liquibase.integration.spring.SpringLiquibase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseDataSource;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import tech.jhipster.config.liquibase.SpringLiquibaseUtil;

@Configuration
public class LiquibaseConfig {

    private final Logger log = LoggerFactory.getLogger(LiquibaseConfig.class);

    private final Environment env;

    public LiquibaseConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public LiquibaseProperties liquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Bean
    public SpringLiquibase liquibase(
            @Qualifier("taskExecutor") Executor executor,
            @LiquibaseDataSource ObjectProvider<DataSource> liquibaseDataSource,
            LiquibaseProperties liquibaseProperties,
            ObjectProvider<DataSource> dataSource,
            DataSourceProperties dataSourceProperties
    ) {
        SpringLiquibase liquibase = SpringLiquibaseUtil.createAsyncSpringLiquibase(
                this.env,
                executor,
                liquibaseDataSource.getIfAvailable(),
                liquibaseProperties,
                dataSource.getIfUnique(),
                dataSourceProperties
        );
        liquibase.setChangeLog("classpath:config/liquibase/master.xml");
        liquibase.setContexts(liquibaseProperties.getContexts());
        liquibase.setDefaultSchema(liquibaseProperties.getDefaultSchema());
        liquibase.setLiquibaseSchema(liquibaseProperties.getLiquibaseSchema());
        liquibase.setLiquibaseTablespace(liquibaseProperties.getLiquibaseTablespace());
        liquibase.setDatabaseChangeLogLockTable(liquibaseProperties.getDatabaseChangeLogLockTable());
        liquibase.setDatabaseChangeLogTable(liquibaseProperties.getDatabaseChangeLogTable());
        liquibase.setDropFirst(liquibaseProperties.isDropFirst());
        liquibase.setLabels(liquibaseProperties.getLabels());
        liquibase.setChangeLogParameters(liquibaseProperties.getParameters());
        liquibase.setRollbackFile(liquibaseProperties.getRollbackFile());
        liquibase.setTestRollbackOnUpdate(liquibaseProperties.isTestRollbackOnUpdate());
        liquibase.setShouldRun(liquibaseProperties.isEnabled());
        log.debug("Configuring Liquibase");
        return liquibase;
    }
}
