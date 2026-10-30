package org.fp.shop_e.repository;

import org.fp.shop_e.entity.Order;
import org.fp.shop_e.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = {"customer", "items", "items.product"})
    Optional<Order> findWithDetailsById(Long id);

    @EntityGraph(attributePaths = {"customer", "items", "items.product"})
    List<Order> findByCustomerIdOrderByCreatedAtDesc(Long customerId);

    @EntityGraph(attributePaths = {"customer", "items", "items.product"})
    @Query("""
            SELECT o FROM Order o
            WHERE (:status IS NULL OR o.status = :status)
              AND (:customerId IS NULL OR o.customer.id = :customerId)
            ORDER BY o.createdAt DESC
            """)
    Page<Order> searchOrders(
            @Param("status") OrderStatus status,
            @Param("customerId") Long customerId,
            Pageable pageable
    );
}

