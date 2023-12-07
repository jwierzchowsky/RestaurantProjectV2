package com.example.RestaurantProjectV2;

import com.example.RestaurantProjectV2.domain.OrderEntity;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

@SpringBootTest
class RestaurantProjectV2ApplicationTests {
    private ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private Validator validator = validatorFactory.getValidator();
    Date futureDate = new Date(System.currentTimeMillis() + 60000);
    Date pastDate = new Date(System.currentTimeMillis() - 60000);

    @Test
    public void shouldHaveNoViolationsForValidOrder() {
        OrderEntity order = new OrderEntity("Pomidorowa", 5, futureDate);

        var violations = validator.validate(order);

        assertTrue(violations.isEmpty());
    }

    @Test
    public void shouldDetectMissingName() {
        OrderEntity order = new OrderEntity(null, 5, futureDate);

        var violations = validator.validate(order);

        assertFalse(violations.isEmpty());
        System.out.println(violations);
        assertEquals(1, violations.size());
        assertEquals("name", violations.iterator().next().getPropertyPath().toString());

    }

    @Test
    public void shouldDetectInvalidName() {
        OrderEntity order = new OrderEntity("InvalidDish", 5, futureDate);
        var violations = validator.validate(order);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("name", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void shouldDetectNegativeQuantity() {
        OrderEntity order = new OrderEntity("Grzybowa", -5, futureDate);

        var violations = validator.validate(order);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("quantity", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void shouldDetectPastOrderDate() {

        OrderEntity order = new OrderEntity("Grzybowa", 2, pastDate);

        var violations = validator.validate(order);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("orderDate", violations.iterator().next().getPropertyPath().toString());
    }

}
