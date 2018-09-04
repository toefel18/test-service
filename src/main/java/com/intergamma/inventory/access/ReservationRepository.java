package com.intergamma.inventory.access;

import com.intergamma.inventory.entity.Reservation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface ReservationRepository extends CrudRepository<Reservation, Long> {

    // 1800 = 30 minutes in seconds
    @Query(value = "DELETE FROM reservation WHERE reservation_timestamp < ?1", nativeQuery = true)
    void cleanExpiredReservations(long maxAge);

    @Query("SELECT SUM(r.amount) FROM Reservation r" +
            " WHERE r.store.name = ?1" +
            " AND r.product.productCode = ?2")
    Optional<Long> calculateTotalAmountReserved(String storeName, String productCode);

    void deleteByStore_NameAndProduct_ProductCodeAndClientName(String storeName, String productCode, String clientName);

    Optional<Reservation> findByStore_NameAndProduct_ProductCodeAndClientName(String storeName, String productCode, String clientName);
}
