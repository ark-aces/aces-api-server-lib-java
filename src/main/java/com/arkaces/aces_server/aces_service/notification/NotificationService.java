package com.arkaces.aces_server.aces_service.notification;

import java.math.BigDecimal;

public interface NotificationService {

    void notifyLowCapacity(BigDecimal currentCapacity, String capacityUnits);

    void notifyFailedTransfer(String contractId, String transferId, String reasonMessage);

    void notifySuccessfulTransfer(String contractId, String transferId);

}
