package com.API.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {
	@Autowired
    private JavaMailSender javaMailSender;


    @Async
    public void sendSimpleEmail(String to, String subject, String text) {
        try {
            // Tạo MimeMessage để gửi email
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

            // Đặt thông tin gửi mail
            messageHelper.setFrom("your-email@gmail.com");
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            
            // Gửi email dưới dạng HTML
            String htmlContent = "<html><body>" +
                                 "<div style='background-color: #4CAF50; color: white; padding: 10px; text-align: center; font-size: 24px;'>Mã xác thực</div>" +
                                 "<p style='font-size: 18px;'>Mã xác thực bắt buộc để truy cập tài liệu: <strong>" + text + "</strong></p>" +
                                 "<p style='font-size: 16px;'>Không chia sẻ email này. Để bảo mật thông tin, vui lòng không chia sẻ email cho người khác.</p>" +
                                 "<p style='font-size: 16px;'>Thắc mắc về tài liệu? Mọi thắc mắc sẽ được liên hệ trực tiếp với người gửi.</p>" +
                                 "</body></html>";
            
            messageHelper.setText(htmlContent, true); // true để email là dạng HTML
            
            // Gửi email
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    // Gửi email với file đính kèm
    public void sendEmailWithAttachment(String to, String subject, String text, String filePath) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            messageHelper.setFrom("your-email@gmail.com");
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(text);

            // Thêm file đính kèm
            messageHelper.addAttachment("Attachment", new java.io.File(filePath));

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
