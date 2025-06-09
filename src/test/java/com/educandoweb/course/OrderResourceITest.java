package com.educandoweb.course;

import com.educandoweb.course.builder.OrderRequestBuilder;
import com.educandoweb.course.dto.OrderCreateDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderResourceITest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper mapper;

    @Test
    @DisplayName("Deve criar pedido completo e retornar HTTP 201")
    @Transactional
    @Rollback
    void createOrder() throws Exception {

        OrderCreateDTO dto = OrderRequestBuilder
                .forUser(1L)
                .addItem(3L, 2, new BigDecimal("120.00"))
                .addItem(5L, 1, new BigDecimal("45.90"))
                .withPaymentType("credit_card")
                .installments(3)
                .paymentMoment(Instant.now())
                .build();

        String json = mapper.writeValueAsString(dto);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.containsString("/orders/")))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.items.length()").value(2))
                .andExpect(jsonPath("$.items[0].productId").value(3L))
                .andExpect(jsonPath("$.items[1].quantity").value(1))
                .andExpect(jsonPath("$.status").value("WAITING_PAYMENT"));
    }
}
