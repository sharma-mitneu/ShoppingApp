package service;

import dto.OrderLineItemsDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import model.Order;
import dto.OrderRequest;
import model.OrderLineItems;
import org.springframework.stereotype.Service;
import repository.OrderRepository;

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

