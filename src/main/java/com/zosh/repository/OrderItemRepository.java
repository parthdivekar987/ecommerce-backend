package com.zosh.repository;

import com.zosh.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Your existing methods can remain if you use them elsewhere
    List<OrderItem> findByProductId(Long productId);
    void deleteByProductId(Long productId);

    // ADDED: This is the new method for our safety check.
    // It efficiently counts how many order items are using a specific product.
    @Query("SELECT COUNT(o) FROM OrderItem o WHERE o.product.id = :productId")
    int countByProductId(@Param("productId") Long productId);
}