package vn.iotstar.config;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class JpaLifecycleListener implements ServletContextListener {
    @Override
    public void contextDestroyed(ServletContextEvent event) { JpaConfig.close(); }
}
