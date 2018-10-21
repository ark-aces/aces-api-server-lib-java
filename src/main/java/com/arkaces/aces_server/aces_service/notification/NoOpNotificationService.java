package com.arkaces.aces_server.aces_service.notification;

import java.math.BigDecimal;

public class NoOpNotificationService implements NotificationService {

    @Override
    public void notifyLowCapacity(BigDecimal currentCapacity, String capacityUnits) {
        // do nothing
    }

    @Override
    public void notifyFailedTransfer(String contractId, String transferId, String reasonMessage) {
        // do nothing
    }

    @Override
    public void notifySuccessfulTransfer(String contractId, String transferId) {
        // do nothing
    }

}
