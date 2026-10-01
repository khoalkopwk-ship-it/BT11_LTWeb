package vn.iotstar.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class JpaConfig {
    private static EntityManagerFactory factory;

    public static synchronized EntityManager getEntityManager() {
        if (factory == null) {
            Map<String, Object> properties = new HashMap<>();
            override(properties, "jakarta.persistence.jdbc.url", "db.url", "DB_URL");
            override(properties, "jakarta.persistence.jdbc.user", "db.username", "DB_USERNAME");
            override(properties, "jakarta.persistence.jdbc.password", "db.password", "DB_PASSWORD");
            override(properties, "jakarta.persistence.jdbc.driver", "db.driver", "DB_DRIVER");
            override(properties, "hibernate.dialect", "db.dialect", "DB_DIALECT");
            override(properties, "hibernate.hbm2ddl.auto", "db.schema", "DB_SCHEMA");
            require(properties, "jakarta.persistence.jdbc.url", "DB_URL");
            require(properties, "jakarta.persistence.jdbc.user", "DB_USERNAME");
            require(properties, "jakarta.persistence.jdbc.password", "DB_PASSWORD");
            factory = Persistence.createEntityManagerFactory("DE03_LTW", properties);
        }
        return factory.createEntityManager();
    }

    private static void override(Map<String, Object> properties, String key, String systemKey, String envKey) {
        String value = System.getProperty(systemKey);
        if (value == null) value = System.getenv(envKey);
        if (value != null) properties.put(key, value);
    }

    private static void require(Map<String, Object> properties, String key, String envKey) {
        Object value = properties.get(key);
        if (value == null || (!key.equals("jakarta.persistence.jdbc.password") && value.toString().isBlank())) {
            throw new IllegalStateException("Thiếu cấu hình: " + envKey);
        }
    }

    public static synchronized void close() {
        if (factory != null && factory.isOpen()) factory.close();
        factory = null;
    }
}
