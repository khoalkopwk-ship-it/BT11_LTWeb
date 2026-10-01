package vn.iotstar.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Cart implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Map<String, Integer> quantities = new LinkedHashMap<>();

    public synchronized Map<String, Integer> getQuantities() {
        return Collections.unmodifiableMap(new LinkedHashMap<>(quantities));
    }
    public synchronized int getQuantity(String id) { return quantities.getOrDefault(id, 0); }
    public synchronized void put(String id, int quantity) { quantities.put(id, quantity); }
    public synchronized void remove(String id) { quantities.remove(id); }
    public synchronized void clear() { quantities.clear(); }
    public synchronized boolean isEmpty() { return quantities.isEmpty(); }
    public synchronized int getTotalQuantity() { return quantities.values().stream().mapToInt(Integer::intValue).sum(); }
}
