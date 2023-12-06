package com.example.RestaurantProjectV2;

import com.example.RestaurantProjectV2.domain.OrderEntity;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;




import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import java.util.Date;
import java.util.Set;

@SpringBootTest
class RestaurantProjectV2ApplicationTests {
	private ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
	private Validator validator = validatorFactory.getValidator();

	@Test
	public void shouldHaveNoViolationsForValidOrder() {
		OrderEntity order = new OrderEntity("Valid Order", 5, new Date());

		var violations = validator.validate(order);

		assertTrue(violations.isEmpty());
	}

	@Test
	public void shouldDetectMissingName() {
		OrderEntity order = new OrderEntity(null, 5, new Date());

		var violations = validator.validate(order);

		assertFalse(violations.isEmpty());
		System.out.println(violations);
		assertEquals(1, violations.size());
		assertEquals("name", violations.iterator().next().getPropertyPath().toString());

	}

	@Test
	public void shouldDetectNegativeQuantity() {
		OrderEntity order = new OrderEntity("Negative Quantity", -5, new Date());

		var violations = validator.validate(order);

		assertFalse(violations.isEmpty());
		assertEquals(1, violations.size());
		assertEquals("quantity", violations.iterator().next().getPropertyPath().toString());
	}

}
