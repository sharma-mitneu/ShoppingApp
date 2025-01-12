package com.shopping_app.microservices.order_service.service;

import com.shopping_app.microservices.order_service.client.InventoryClient;
import com.shopping_app.microservices.order_service.dto.OrderRequest;
import com.shopping_app.microservices.order_service.event.OrderPlacedEvent;
import com.shopping_app.microservices.order_service.model.Order;
import com.shopping_app.microservices.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public void placeOrder(OrderRequest orderRequest){

        var isProductInStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());
        log.info("Product with skuCode {} is in stock: {}", orderRequest.skuCode(), isProductInStock);
        if(isProductInStock){
            // map order request to order object
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());
            order.setPrice(orderRequest.price());
            order.setSkuCode(orderRequest.skuCode());
            order.setQuantity(orderRequest.quantity());
            orderRepository.save(order);

            // Send the message to kafka topic
            // order number and email for sending email notification

            OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent(order.getOrderNumber(), "test1@123.com");
            log.info("Start - Sending OrderPlacedEvent {} to Kafka topic order-placed", orderPlacedEvent);
            kafkaTemplate.send("order-placed", orderPlacedEvent);
        }
        else{
            throw new RuntimeException("Product with skuCode " + orderRequest.skuCode() + " is out of stock");
        }

//        // map order request to order object
//        Order order = new Order();
//        order.setOrderNumber(UUID.randomUUID().toString());
//        order.setPrice(orderRequest.price());
//        order.setSkuCode(orderRequest.skuCode());
//        order.setQuantity(orderRequest.quantity());
//
//        // save order to orderRepository
//        orderRepository.save(order);

    }
}
