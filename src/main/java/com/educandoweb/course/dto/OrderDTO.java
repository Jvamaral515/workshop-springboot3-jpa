package com.educandoweb.course.dto;

import com.educandoweb.course.entities.Order;
import com.educandoweb.course.entities.OrderItem;
import com.educandoweb.course.entities.User;
import com.educandoweb.course.entities.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.web.JsonPath;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDTO {

    private Long id;
    private Instant moment;
    private OrderStatus status;

    @JsonProperty("userId")
    private Long userId;
    private String userName;

    private List<ItemDTO> items = new ArrayList<>();

    private Instant paymentMoment;

    public OrderDTO() {}

    public OrderDTO(Order entity) {
        this.id     = entity.getId();
        this.moment = entity.getMoment();
        this.status = entity.getOrderStatus();

        User user = entity.getClient();
        this.userId   = user.getId();
        this.userName = user.getName();

        this.items = entity.getItems().stream()
                .map(ItemDTO::new)
                .collect(Collectors.toList());

        if (entity.getPayment() != null) {
            this.paymentMoment = entity.getPayment().getMoment();
        }
    }

    public Long getId()                 { return id; }
    public Instant getMoment()          { return moment; }
    public OrderStatus getStatus()      { return status; }
    public Long getUserId()           { return userId; }
    public String getUserName()       { return userName; }
    public List<ItemDTO> getItems()     { return items; }
    public Instant getPaymentMoment()   { return paymentMoment; }

    public BigDecimal getTotal() {
        return items.stream()
                .map(ItemDTO::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static class ItemDTO {
        private Long productId;
        private String productName;
        private Integer quantity;
        private BigDecimal price;
        private BigDecimal subTotal;

        public ItemDTO() {}

        public ItemDTO(OrderItem oi) {
            this.productId   = oi.getProduct().getId();
            this.productName = oi.getProduct().getName();
            this.quantity    = oi.getQuantity();
            this.price       = oi.getPrice();
            this.subTotal    = oi.getPrice().multiply(BigDecimal.valueOf(oi.getQuantity()));
        }

        public Long        getProductId()   { return productId; }
        public String      getProductName() { return productName; }
        public Integer     getQuantity()    { return quantity; }
        public BigDecimal  getPrice()       { return price; }
        public BigDecimal  getSubTotal()    { return subTotal; }
    }
}
