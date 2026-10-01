package vn.iotstar.dao;

import vn.iotstar.dto.CheckoutForm;
import vn.iotstar.entity.CustomerOrder;
import vn.iotstar.entity.OrderStatus;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface OrderDAO {
    Long placeCod(String username, Map<String, Integer> quantities, CheckoutForm form,
                  String token, Map<String, BigDecimal> expectedPrices);
    List<CustomerOrder> findByUser(String username, OrderStatus status, int page, int size);
    long countByUser(String username, OrderStatus status);
    Map<String, Long> countStatuses(String username);
    CustomerOrder findOwned(Long id, String username);
}
