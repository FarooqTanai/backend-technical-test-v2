package com.tui.proof.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import com.tui.proof.model.Entity;
import com.tui.proof.model.Order;
import com.tui.proof.model.response.CommonResponse;
import com.tui.proof.service.OrderService;
import java.util.List;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@Log4j2
@RestController
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	

	@RequestMapping(value = "/orders", method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<CommonResponse<List<Order>>> getOrders(@RequestParam(name="firstName", required = false) String firstName
			, @RequestParam(name="lastName", required = false) String lastName) {
		
		log.info("Request for getOrders, firstName: {}, lastName: {}", firstName, lastName);
		List<Order> clients = orderService.getOrders(firstName, lastName);
		log.debug("Response for getOrders: {}", clients);
		return new ResponseEntity<>(new CommonResponse<>(Entity.ORDER, clients), HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/orders", method = POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<CommonResponse<Map<String, String>>> saveOrder(@RequestBody Order order) {
		
		log.debug("Request for saveOrder: {}", order);
		Map<String, String> response = orderService.saveOrder(order);
		log.debug("Response for saveOrder: {}", response);
		return new ResponseEntity<>(new CommonResponse<>(Entity.ORDER, response), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/orders/{orderNumber}", method = PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<CommonResponse<Map<String, String>>> updateOrder(@PathVariable String orderNumber, @RequestBody Order order) {
		
		log.debug("Request for updateOrder: {}", order);
		Map<String, String> response = orderService.updateOrder(orderNumber, order);
		log.debug("Response for updateOrder: {}", response);
		return new ResponseEntity<>(new CommonResponse<>(Entity.ORDER, response), HttpStatus.OK);
	}
}
