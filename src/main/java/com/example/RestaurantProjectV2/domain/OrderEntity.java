package com.example.RestaurantProjectV2.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.Date;

@Entity
@Table(name = "'order'")
@Data
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    @Min(2)
    private int quantity;
    @NotNull
    private Date orderDate;
    public OrderEntity(){

    }
    public OrderEntity(String name, int quantity, Date orderDate){
        this.name = name;
        this.quantity = quantity;
        this.orderDate = orderDate;
    }
}
