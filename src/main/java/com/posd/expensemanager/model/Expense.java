package com.posd.expensemanager.model;

import javax.persistence.*;

@Entity
@Table(name = "expense")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String docs;
    private String status;
    private String message;

    @OneToOne
    @JoinColumn(name = "travelorder_id")
    private TravelOrder travelOrder;

    public Expense(String docs, String status, String message, TravelOrder travelOrder) {
        this.docs = docs;
        this.status = status;
        this.message = message;
        this.travelOrder = travelOrder;
    }

    public Expense() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDocs() {
        return docs;
    }

    public void setDocs(String docs) {
        this.docs = docs;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TravelOrder getTravelOrder() {
        return travelOrder;
    }

    public void setTravelOrder(TravelOrder travelOrder) {
        this.travelOrder = travelOrder;
    }

    public String toString() {
        return id + " | " + travelOrder.getId() + " | " + status;
    }

    public String toFormattedString() {
        return id + " | " + travelOrder.getId() + " | " + docs;
    }
}
