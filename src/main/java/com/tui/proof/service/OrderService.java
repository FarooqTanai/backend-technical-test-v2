package com.tui.proof.service;

import com.tui.proof.errors.ClientNotFoundException;
import com.tui.proof.errors.OrderNotFoundException;
import com.tui.proof.errors.OrderNotUpdatableException;
import com.tui.proof.errors.OrderTotalisNotValidException;
import com.tui.proof.errors.PilotesNotAllowedException;
import com.tui.proof.model.Client;
import com.tui.proof.model.Entity;
import com.tui.proof.model.Order;
import com.tui.proof.repository.OrderRepository;
import com.tui.proof.utils.Validator;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class OrderService {

	
	@Value("#{'${pilotes.allowed.values}'.split(',')}")
	private List<Integer> allowedPilotes;
	
	@Value("${order.updatable.minutes}")
	private long orderUpdatableMinutes;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private ClientService clientService;
	
	

	public List<Order> getOrders(String clientFname, String clientLname) {
		
		List<Client> clients = null;
		if(clientFname != null && clientLname != null) {
			clients = clientService.getClientsContainsFirstNameAndLastName(clientFname, clientLname);
		} else if (clientFname != null) {
			clients = clientService.getClientsContainsFirstName(clientFname);
		} else if (clientLname !=null) {
			clients = clientService.getClientsContainsLastName(clientLname);
		} else {
			return orderRepository.findAll();
		}
		
		if(clients != null && !clients.isEmpty()) {
			Set<Integer> clientIds = clients.stream().map(Client::getId).collect(Collectors.toSet());
			return orderRepository.findOrdersByClientIds(clientIds);
		}
		return new ArrayList<>();
	}

	public Map<String, String> saveOrder(Order order) {
		validateOrder(order);
		String orderNumber = UUID.randomUUID().toString();
		order.setNumber(orderNumber);
		order.setOrderCreateDate(new Date());
		order.setOrderUpdateDate(new Date());
		orderRepository.save(order);
		Map<String, String> response = new HashMap<String, String>();
		response.put("orderNumber", orderNumber);
		return response;
	}
	
	private void validateOrder(Order order) {
		if(!allowedPilotes.contains(order.getPilotes())) {
			log.error("Pilotes {} not allowed to save", order.getPilotes());
			throw new PilotesNotAllowedException(allowedPilotes);
		}
		Validator.validateDeliverAddress(order.getDeliveryAddress(), Entity.ORDER);
		double total = order.getOrderTotal();
		if(total <= 0) {
			throw new OrderTotalisNotValidException();
		}
		int clientId = order.getClientId();
		if(!clientService.clientExist(clientId)) {
			throw new ClientNotFoundException(String.valueOf(clientId));
		}
	}

	public Map<String, String> updateOrder(String orderNumber, Order order) {
		
		Optional<Order> existingOrder = orderRepository.findById(orderNumber);
		if(!existingOrder.isPresent()) {
			log.info("Order: {} not found", orderNumber);
			throw new OrderNotFoundException(orderNumber);
		}
		Date orderedDate = existingOrder.get().getOrderCreateDate();
		long currentTime = new Date().getTime();
		long updateAllowedTime = orderedDate.getTime() + Duration.ofMinutes(orderUpdatableMinutes).toMillis();
		if(updateAllowedTime < currentTime) {
			log.error("Order: {} not updatable", orderNumber);
			throw new OrderNotUpdatableException();
		}
		validateOrder(order);
		order.setNumber(orderNumber);
		order.setOrderCreateDate(orderedDate);
		order.setOrderUpdateDate(new Date());
		orderRepository.save(order);
		Map<String, String> response = new HashMap<String, String>();
		response.put("orderNumber", orderNumber);
		return response;
	}

}
