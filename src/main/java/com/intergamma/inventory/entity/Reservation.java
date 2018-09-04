package com.intergamma.inventory.entity;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Column(nullable = false, updatable = false)
    private String clientName;

    @Column(nullable = false, updatable = false)
    private ZonedDateTime reservationTimestamp;

    @Column(nullable = false)
    private long amount;

    public Reservation() {
    }

    public Reservation(Store store, Product product, String clientName, long amount) {
        this.store = store;
        this.product = product;
        this.clientName = clientName;
        this.amount = amount;
        this.reservationTimestamp = ZonedDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public ZonedDateTime getReservationTimestamp() {
        return reservationTimestamp;
    }

    public void setReservationTimestamp(ZonedDateTime reservationTimestamp) {
        this.reservationTimestamp = reservationTimestamp;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
