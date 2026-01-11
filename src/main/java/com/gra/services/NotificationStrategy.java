package com.gra.services;

import com.gra.model.Notifikime;

public interface NotificationStrategy {
    void sendMessage(Notifikime njoftim);
}