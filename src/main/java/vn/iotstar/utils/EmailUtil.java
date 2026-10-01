package vn.iotstar.utils;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public final class EmailUtil {
    private EmailUtil() {
    }

    public static void sendOTP(String email, String otp) throws MessagingException {
        String username = requiredEnv("MAIL_USERNAME");
        String password = requiredEnv("MAIL_PASSWORD");
        String host = requiredEnv("MAIL_HOST");
        String port = requiredEnv("MAIL_PORT");
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");

        Session mailSession = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        MimeMessage message = new MimeMessage(mailSession);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
        message.setSubject("Mã OTP đăng ký EcoMart", "UTF-8");
        message.setText("Mã OTP của bạn là: " + otp + ". Mã có hiệu lực trong 5 phút.", "UTF-8");
        Transport.send(message);
    }
    private static String requiredEnv(String name) throws MessagingException {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new MessagingException("Thiếu biến môi trường: " + name);
        }
        return value;
    }
}
