package com.intergamma.inventory.access;

import com.intergamma.inventory.entity.InventoryHistoryType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ReservationRepository extends CrudRepository<ReservationRepository, Long> {

    long cleanExpiredReservations()

}
