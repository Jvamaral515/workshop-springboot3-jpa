package com.educandoweb.course.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class OrderCreateDTO {

    @NotNull
    private Long userId;
    @NotEmpty
    private final List<ItemDTO> items = new ArrayList<>();
    @NotNull
    private PaymentDTO payment;

    public Long getUserId() {return userId;}
    public void setUserId(Long userId) {this.userId = userId;}

    public List<ItemDTO> getItems() {return items;}

    public PaymentDTO getPayment() {return payment;}
    public void setPayment(PaymentDTO payment) {this.payment = payment;}

    public static class ItemDTO {
        @NotNull
        private Long productId;
        @PositiveOrZero
        private Integer quantity;
        @Positive
        private BigDecimal price;

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }
    }

    public static class PaymentDTO {
        @NotEmpty
        private String type;
        @NotEmpty
        private Instant moment;
        @NotNull
        private int installments;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Instant getMoment() {
            return moment;
        }

        public void setMoment(Instant moment) {
            this.moment = moment;
        }

        public int getInstallments() {
            return installments;
        }

        public void setInstallments(int installments) {
            this.installments = installments;
        }
    }
}
