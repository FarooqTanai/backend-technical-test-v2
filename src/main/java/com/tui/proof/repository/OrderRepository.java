package com.tui.proof.repository;

import com.tui.proof.model.Order;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, String>{

	
	@Query("select u from Order u where u.clientId in ?1")
	List<Order> findOrdersByClientIds(Set<Integer> clientIds);

}
