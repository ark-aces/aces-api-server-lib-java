package com.arkaces.aces_server.aces_service.notification;

import org.springframework.mail.MailSender;

public class NotificationServiceFactory {

    public EmailNotificationService createEmailNotificationService(
            String serviceName,
            String fromName,
            String recipientEmailAddress,
            MailSender mailSender
    ) {
        return new EmailNotificationService(serviceName, fromName, recipientEmailAddress, mailSender);
    }

    public NotificationService createNoOpNotificationService() {
        return new NoOpNotificationService();
    }

}
