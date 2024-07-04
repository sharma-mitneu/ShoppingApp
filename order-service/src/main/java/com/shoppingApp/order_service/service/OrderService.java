package com.shoppingApp.order_service.service;

import com.shoppingApp.order_service.repository.OrderRepository;
import com.shoppingApp.order_service.dto.OrderLineItemsDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import com.shoppingApp.order_service.model.Order;
import com.shoppingApp.order_service.dto.OrderRequest;
import com.shoppingApp.order_service.model.OrderLineItems;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItemsList(orderLineItemsList);

        //call inventory service and place order if product is in stock !

        orderRepository.save(order);

    }
        private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemDto){
            OrderLineItems orderLineItems = new OrderLineItems();
            orderLineItems.setPrice(orderLineItemDto.getPrice());
            orderLineItems.setQuantity(orderLineItemDto.getQuantity());
            orderLineItems.setSkuCode(orderLineItemDto.getSkuCode());
            return orderLineItems;
        }


}

