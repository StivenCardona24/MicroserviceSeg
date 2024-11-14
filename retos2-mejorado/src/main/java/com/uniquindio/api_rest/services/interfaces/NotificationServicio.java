package com.uniquindio.api_rest.services.interfaces;

import com.uniquindio.api_rest.dto.NotificationDTO;

public interface NotificationServicio {
    void sendNotification(NotificationDTO notificationDTO) throws Exception;
}
