package com.csye6225.webapp.serviceImpl;

import com.csye6225.webapp.service.HealthCheckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;

@Service
public class HealthCheckServiceImpl implements HealthCheckService {

    DataSource dataSource;

    @Autowired
    public HealthCheckServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    Logger logger = LoggerFactory.getLogger(HealthCheckServiceImpl.class);
    @Override
    public boolean isDatabaseConnected() {
        try(Connection conn = dataSource.getConnection()) {
            logger.info("Database connection success");
            return true;
        } catch (Exception e) {
            logger.error("Database connection error");
            return false;
        }
    }
}
