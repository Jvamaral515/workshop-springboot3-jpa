package com.educandoweb.course.builder;

import com.educandoweb.course.DTO.OrderCreateDTO;

import java.math.BigDecimal;
import java.time.Instant;

public final class OrderRequestBuilder {

    public interface UserStep{
        ItemsStep addItem(Long productId, Integer quantity, BigDecimal price);
    }

    public interface ItemsStep {
        ItemsStep addItem(Long productId, Integer quantity, BigDecimal price);
        PaymentStep withPaymentType(String type);
    }

    public interface PaymentStep {
        PaymentStep installments(int installments);
        BuildStep paymentMoment(Instant moment);
    }

    public interface BuildStep {
        OrderCreateDTO build();
    }

    public static UserStep forUser (Long userId) {
        return new UserStepImpl(userId);
    }

    private static class UserStepImpl implements UserStep {
        private final OrderCreateDTO dto = new OrderCreateDTO();

        private UserStepImpl(Long userId) { dto.setUserId(userId); }

        @Override
        public ItemsStep addItem(Long productId, Integer qty, BigDecimal price) {
            return new ItemsStepImpl(dto).addItem(productId, qty, price);
        }
    }

    private static class ItemsStepImpl implements ItemsStep {
        private final OrderCreateDTO dto;

        private ItemsStepImpl(OrderCreateDTO dto) { this.dto = dto; }

        @Override
        public ItemsStep addItem(Long productId, Integer qty, BigDecimal price) {
            var item = new OrderCreateDTO.ItemDTO();
            item.setProductId(productId);
            item.setQuantity(qty);
            item.setPrice(price);
            dto.getItems().add(item);
            return this;                     // permanece em ItemsStep
        }

        @Override
        public PaymentStep withPaymentType(String type) {
            var pay = new OrderCreateDTO.PaymentDTO();
            pay.setType(type);
            dto.setPayment(pay);
            return new PaymentStepImpl(dto);
        }
    }

    private static class PaymentStepImpl implements PaymentStep, BuildStep {
        private final OrderCreateDTO dto;

        private PaymentStepImpl(OrderCreateDTO dto) { this.dto = dto; }

        private OrderCreateDTO.PaymentDTO payment() { return dto.getPayment(); }

        @Override
        public PaymentStep installments(int n) {
            payment().setInstallments(n);
            return this;
        }

        @Override
        public BuildStep paymentMoment(Instant moment) {
            payment().setMoment(moment);
            return this;
        }

        @Override
        public OrderCreateDTO build() {
            if (dto.getItems().isEmpty())
                throw new IllegalStateException("Pedido precisa de ao menos 1 item.");
            if (dto.getPayment() == null || dto.getPayment().getMoment() == null)
                throw new IllegalStateException("Pagamento incompleto.");
            return dto;
        }
    }

    private OrderRequestBuilder() { }
}