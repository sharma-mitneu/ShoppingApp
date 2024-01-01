package com.shoppingApp.order_service.service;

import com.shoppingApp.order_service.dto.InventoryResponse;
import com.shoppingApp.order_service.repository.OrderRepository;
import com.shoppingApp.order_service.dto.OrderLineItemsDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import com.shoppingApp.order_service.model.Order;
import com.shoppingApp.order_service.dto.OrderRequest;
import com.shoppingApp.order_service.model.OrderLineItems;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItemsList(orderLineItemsList);

        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSkuCode).toList();


        //call inventory service and place order if product is in stock !
        InventoryResponse[] inventoryResponses = webClient.get()
                .uri("http://localhost:8082/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve().bodyToMono(InventoryResponse[].class)
                .block();

        boolean AllProductsInStock = Arrays.stream(inventoryResponses)
                .allMatch(InventoryResponse::isInStock);

        if(AllProductsInStock) {
            orderRepository.save(order);
        }
        else {
            throw new IllegalArgumentException("Product is not in stock ! Try again later ! ");
        }
    }
        private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemDto){
            OrderLineItems orderLineItems = new OrderLineItems();
            orderLineItems.setPrice(orderLineItemDto.getPrice());
            orderLineItems.setQuantity(orderLineItemDto.getQuantity());
            orderLineItems.setSkuCode(orderLineItemDto.getSkuCode());
            return orderLineItems;
        }


}

