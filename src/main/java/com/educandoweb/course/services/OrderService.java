package com.educandoweb.course.services;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import com.educandoweb.course.dto.OrderCreateDTO;
import com.educandoweb.course.entities.*;
import com.educandoweb.course.entities.enums.OrderStatus;
import com.educandoweb.course.repositories.ProductRepository;
import com.educandoweb.course.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.stereotype.Service;

import com.educandoweb.course.repositories.OrderRepository;

@Service
public class OrderService {
	
	@Autowired
	private OrderRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private GenericHttpMessageConverter genericHttpMessageConverter;
    @Autowired
    private OrderRepository orderRepository;

	public List<Order> findAll(){
		return repository.findAll();
	}
	
	public Order findById(Long id) {
		Optional<Order> obj = repository.findById(id);
		return obj.get();
	}

	@Transactional
	public Order insert(OrderCreateDTO dto){

		User user = userRepository.getReferenceById(dto.getUserId());

		Order order = new Order(null, Instant.now(), OrderStatus.WAITING_PAYMENT, user);

		dto.getItems().forEach(item -> {
			Product product = productRepository.getReferenceById(item.getProductId());
			OrderItem itemOrderItem = new OrderItem(order, product, item.getQuantity(), item.getPrice());
			order.getItems().add(itemOrderItem);
		});

		OrderCreateDTO.PaymentDTO paymentDTO = dto.getPayment();
		Payment pay = new Payment(null, paymentDTO.getMoment(), order, paymentDTO.getType(), paymentDTO.getInstallments());
		order.setPayment(pay);

		orderRepository.save(order);
		return order;
	}
}
