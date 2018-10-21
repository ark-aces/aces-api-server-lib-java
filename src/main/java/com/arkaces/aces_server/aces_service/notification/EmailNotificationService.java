package com.arkaces.aces_server.aces_service.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class EmailNotificationService implements NotificationService {

    private final String serviceName;
    private final String fromEmailAddress;
    private final String recipientEmailAddress;
    private final MailSender mailSender;

    @Override
    public void notifyLowCapacity(BigDecimal currentCapacity, String capacityUnits) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(recipientEmailAddress);
        msg.setFrom(fromEmailAddress);
        msg.setSubject("Service " + serviceName + " at low capacity");
        msg.setText("Service " + serviceName + " is at low capacity:\n\n" +
                "Current Capacity: " + currentCapacity.toPlainString() + " " + capacityUnits);
        send(msg);
    }

    @Override
    public void notifyFailedTransfer(String contractId, String transferId, String reasonMessage) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(recipientEmailAddress);
        msg.setFrom(fromEmailAddress);
        msg.setSubject("Service " + serviceName + " failed executing transfer " + transferId);
        msg.setText("Service " + serviceName + " failed executing transfer " + transferId
                + " for contract " + contractId + " with reason:\n\n" + reasonMessage);
        send(msg);
    }

    @Override
    public void notifySuccessfulTransfer(String contractId, String transferId) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(recipientEmailAddress);
        msg.setFrom(fromEmailAddress);
        msg.setSubject("Service " + serviceName + " successfully executed transfer " + transferId);
        msg.setText("Service " + serviceName + " successfully executed transfer " + transferId + " for contract " + contractId);
        send(msg);
    }

    private void send(SimpleMailMessage msg) {
        try {
            this.mailSender.send(msg);
        } catch (Exception e) {
            throw new NotificationServiceException("Failed to send notification email", e);
        }
    }

}
