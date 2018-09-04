package com.intergamma.inventory.timers;

import com.intergamma.inventory.access.ReservationRepository;
import com.intergamma.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    @Autowired
    private ReservationRepository reservationRepository;

    /**
     * Checks for outdated reservations every 5s.
     */
    @Scheduled(fixedRate = 5000)
    public void cleanOutdatedReservation(){
        reservationRepository.cleanExpiredReservations();
    }
}
