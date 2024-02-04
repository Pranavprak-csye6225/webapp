package com.csye6225.webapp.serviceImpl;

import com.csye6225.webapp.service.HealthCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class HealthCheckServiceImpl implements HealthCheckService {

    DataSource dataSource;

    @Autowired
    public HealthCheckServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean isDatabaseConnected() {
        try {
            dataSource.getConnection();
            return true;
        } catch (Exception e) {
            System.out.println("Database not connected: " + e);
            return false;
        }
    }
}
