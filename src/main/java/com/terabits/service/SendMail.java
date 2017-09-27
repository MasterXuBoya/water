package com.terabits.service;

import java.util.Properties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class SendMail {

    private JavaMailSenderImpl email;

    private SimpleMailMessage message;

    public SendMail() {
        // TODO Auto-generated constructor stub
        email = new JavaMailSenderImpl();
        email.setHost("smtp.qq.com");
        email.setUsername("743042009@qq.com");//QQ邮箱账号
        email.setPassword("jsqtmmtliwqjbfib");//QQ邮箱授权码,在网站上设置申请
        email.setPort(465);
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.ssl.enable", true);
        properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.timeout", 5000);
        email.setJavaMailProperties(properties);
    }

    //发送邮件
    public void send(){
        message = new SimpleMailMessage();
        message.setFrom("743042009@qq.com");
        message.setTo("743042009@qq.com");
        message.setSubject("WOW 邮箱激活");
        message.setText("test");
        email.send(message);
    }
    public static void main(String args[]) {
        SendMail haha = new SendMail();
        haha.send();
        System.out.println("邮件已发送");
    }
}
