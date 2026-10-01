package vn.iotstar.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class OtpUtilTest {
    @Test
    void generatedOtpAlwaysHasSixDigits() {
        for (int i = 0; i < 100; i++) {
            assertTrue(OtpUtil.generateOTP().matches("\\d{6}"));
        }
    }
}
