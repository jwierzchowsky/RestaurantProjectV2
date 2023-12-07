package com.example.RestaurantProjectV2;

import com.example.RestaurantProjectV2.domain.OrderEntity;
import com.example.RestaurantProjectV2.repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RestaurantProjectV2Application.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@AutoConfigureTestDatabase
public class RestaurantProjectIntegrationTests {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private OrderRepository repository;

    Date futureDate = new Date(System.currentTimeMillis() + 60000);
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void whenValidInput_thenCreateStudent() throws IOException, Exception {
        OrderEntity firstOrder = new OrderEntity("Schabowy", 1, futureDate);

        mvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(firstOrder)));

        List<OrderEntity> found = repository.findAll();
        assertThat(found).extracting(OrderEntity::getName).contains("Schabowy");

    }

    @Test
    public void givenOrders_whenGetOrder_thenStatus200() throws Exception {
        createTestOrder("Schabowy", 3, futureDate);
        createTestOrder("Pomidorowa", 2, futureDate);

        mvc.perform(get("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$[0].name", is("Schabowy")))
                .andExpect(jsonPath("$[1].name", is("Pomidorowa")));
    }

    private OrderEntity createTestOrder(String name, int quantity, Date orderDate) {
        OrderEntity order = new OrderEntity(name, quantity, orderDate);
        return repository.saveAndFlush(order);
    }

    @Test
    public void givenOrderId_whenGetOrderById_thenStatus200() throws Exception {
        OrderEntity order = createTestOrder("Schabowy", 3, futureDate);

        mvc.perform(get("/api/orders/{id}", order.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(3)));
    }

    @Test
    public void givenOrderId_whenUpdateOrder_thenStatus200() throws Exception {
        OrderEntity order = createTestOrder("Schabowy", 3, futureDate);
        OrderEntity updatedOrder = new OrderEntity("Schabowy z ziemniakami", 5, futureDate);

        mvc.perform(put("/api/orders/{id}", order.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedOrder)))
                .andExpect(status().isOk());

        OrderEntity updated = repository.findById(order.getId()).orElse(null);
        assertThat(updated).isNotNull();
        assertThat(updated.getName()).isEqualTo("Schabowy z ziemniakami");
        assertThat(updated.getQuantity()).isEqualTo(5);
    }

    @Test
    public void givenOrderId_whenDeleteOrder_thenStatus200() throws Exception {
        OrderEntity order = createTestOrder("Schabowy", 3, futureDate);

        mvc.perform(delete("/api/orders/{id}", order.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Optional<OrderEntity> deleted = repository.findById(order.getId());
        assertThat(deleted).isEmpty();
    }
}
