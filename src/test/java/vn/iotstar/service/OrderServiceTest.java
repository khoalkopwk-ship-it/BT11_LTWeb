package vn.iotstar.service;

import org.junit.jupiter.api.Test;
import vn.iotstar.dto.CheckoutForm;
import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {
    private final OrderService service = new OrderService(null);
    private CheckoutForm validForm() {
        CheckoutForm form = new CheckoutForm();
        form.setReceiverName("Người dùng mẫu");
        form.setPhone("0901234567");
        form.setAddress("01 Võ Văn Ngân, Thủ Đức, TP. Hồ Chí Minh");
        return form;
    }

    @Test void validReceiverInformationIsAccepted() { assertDoesNotThrow(() -> service.validateForm(validForm())); }
    @Test void rejectShortName() {
        CheckoutForm form = validForm(); form.setReceiverName("A");
        assertThrows(IllegalArgumentException.class, () -> service.validateForm(form));
    }
    @Test void rejectInvalidPhone() {
        CheckoutForm form = validForm(); form.setPhone("123");
        assertThrows(IllegalArgumentException.class, () -> service.validateForm(form));
    }
    @Test void rejectShortAddress() {
        CheckoutForm form = validForm(); form.setAddress("abc");
        assertThrows(IllegalArgumentException.class, () -> service.validateForm(form));
    }
    @Test void rejectOverlongNote() {
        CheckoutForm form = validForm(); form.setNote("x".repeat(501));
        assertThrows(IllegalArgumentException.class, () -> service.validateForm(form));
    }
}
