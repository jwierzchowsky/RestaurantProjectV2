package com.example.RestaurantProjectV2.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Table(name = "'order'")
@Data
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int quantity;
    private Date orderDate;
    public OrderEntity(){

    }
    public OrderEntity(String name, int quantity, Date orderDate){
        this.name = name;
        this.quantity = quantity;
        this.orderDate = orderDate;
    }
}
