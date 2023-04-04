package com.github.ryanreymorris.orderescortbot.repository;

import com.github.ryanreymorris.orderescortbot.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * {@link Customer} repository.
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query(value = "select *  from (SELECT c.*,  RANK() OVER (ORDER BY COUNT(*)) rnk FROM customer c LEFT JOIN service_call s ON s.performer_id = c.id Group By c.id) joined where joined.is_service_call=true order by joined.rnk limit 1",
            nativeQuery = true)
    List<Customer> findLessBusyTecSupport();
}
