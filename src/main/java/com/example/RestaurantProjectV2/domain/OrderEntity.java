package com.example.RestaurantProjectV2.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    @Pattern(regexp = "^(Schabowy|Schabowy z ziemniakami|Pizza|Pomidorowa|Grzybowa|Makaron|Sałatka|Burger)$",
            message = "Invalid name. Allowed values: Schabowy, Schabowy z ziemniakami, Pizza, Pomidorowa, Grzybowa, Makaron, Sałatka, Burger")
    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    @Min(value = 1, message = "Quantity must be greater than or equal to 1")
    private int quantity;
    @NotNull
    @Future(message = "Order date must be in the present or future")
    private Date orderDate;

    public OrderEntity() {

    }

    public OrderEntity(String name, int quantity, Date orderDate) {
        this.name = name;
        this.quantity = quantity;
        this.orderDate = orderDate;
    }
}
