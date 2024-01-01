package com.shoppingApp.inventory_service.repository;

import com.shoppingApp.inventory_service.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findBySkuCode();

    List<Inventory> findBySkuCodeIn(List<String> skuCode);
}
