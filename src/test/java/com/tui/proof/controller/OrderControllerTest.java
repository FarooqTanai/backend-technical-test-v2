package com.tui.proof.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tui.proof.errors.ProjectExceptionHandler;
import com.tui.proof.model.Address;
import com.tui.proof.model.Client;
import com.tui.proof.model.Entity;
import com.tui.proof.model.Order;
import com.tui.proof.model.response.CommonResponse;
import com.tui.proof.repository.ClientRepository;
import com.tui.proof.repository.OrderRepository;
import com.tui.proof.service.ClientService;
import com.tui.proof.service.OrderService;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@WebMvcTest(OrderController.class)
@ContextConfiguration(classes = {OrderController.class, 
								OrderService.class, 
								ProjectExceptionHandler.class,
								ClientService.class
								})
public class OrderControllerTest {

	private static long FIVE_MINUTES = Duration.ofMinutes(5).toMillis();
	private MockMvc mvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Autowired
	private ObjectMapper mapper;
	
	@MockBean
	private OrderRepository orderRepository;
	
	@MockBean
	private ClientRepository clientRepository;
	
	private Client client1;
	private Client client2;
	private Client client3;
	
	private Order order1;
	private Order order2;
	private Order order3;
	private Order order4;
	
	@Before
	public void setUp() {
		this.mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.build();
		
		client1 = new Client(1, "tom", "jerry", "+393880000000", "test@mail.com", "male");
		client2 = new Client(2, "pecos", "joan", "+393880000002", "test2@mail.com", "female");
		client3 = new Client(3, "george", "little", "+393880000003", "test3@mail.com", "male");
		
		
		Address deliveryAddress1 = new Address("street 1", "21100", "pavia", "italy");
		Address deliveryAddress2 = new Address("street 2", "32200", "milan", "italy");
		Address deliveryAddress3 = new Address("street 3", "13300", "rome", "italy");
		//Order1 and Order2 were placed five minutes ago
		Date fiveMinutesAgo = new Date(System.currentTimeMillis()-FIVE_MINUTES);
		order1 = new Order("abc123", deliveryAddress1, 10, 100, 1, fiveMinutesAgo, fiveMinutesAgo);
		order2 = new Order("def321", deliveryAddress2, 15, 150, 2, fiveMinutesAgo, fiveMinutesAgo);
		order3 = new Order("ghi5l2", deliveryAddress1, 5, 50, 1, new Date(), new Date());
		order4 = new Order("jkl4r5", deliveryAddress3, 5, 50, 3, new Date(), new Date());
		
		List<Order> orders = new ArrayList<>();
		orders.add(order1);
		orders.add(order2);
		orders.add(order3);
		orders.add(order4);
		when(orderRepository.findAll()).thenReturn(orders);
	}
	
	
	@Test
	@WithMockUser
	public void getClients_OK() throws Exception {
		MvcResult result = mvc.perform(get("/orders")).andExpect(status().isOk()).andReturn();
		String expected = stringFromResource("orders/allOrdersResponse.json");
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	@WithMockUser
	public void getClients_ContainsFirstNameAndLastName() throws Exception {
		
		List<Client> clients = new ArrayList<>();
		clients.add(client1);
		when(clientRepository.getClientsContainsFirstNameAndLastName("to", "rry")).thenReturn(clients);
		
		Set<Integer> clientIds = new HashSet<>();
		clientIds.add(client1.getId());
		
		List<Order> orders = new ArrayList<>();
		orders.add(order1);
		orders.add(order3);
		when(orderRepository.findOrdersByClientIds(clientIds)).thenReturn(orders);
	
		MvcResult result = mvc.perform(get("/orders?firstName=to&lastName=rry")).andExpect(status().isOk()).andReturn();
		String expected = stringFromResource("orders/ordersByFNameLName.json");
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	@WithMockUser
	public void getClients_ContainsLastName() throws Exception {
		
		List<Client> clients = new ArrayList<>();
		clients.add(client3);
		when(clientRepository.getClientsContainsFirstName("litt")).thenReturn(clients);
		
		Set<Integer> clientIds = new HashSet<>();
		clientIds.add(client3.getId());
		
		List<Order> orders = new ArrayList<>();
		orders.add(order4);
		when(orderRepository.findOrdersByClientIds(clientIds)).thenReturn(orders);
	
		MvcResult result = mvc.perform(get("/orders?firstName=litt")).andExpect(status().isOk()).andReturn();
		String expected = stringFromResource("orders/ordersByLName.json");
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	@WithMockUser
	public void getClients_ContainsFirstName() throws Exception {
		
		List<Client> clients = new ArrayList<>();
		clients.add(client2);
		when(clientRepository.getClientsContainsLastName("peco")).thenReturn(clients);
		
		Set<Integer> clientIds = new HashSet<>();
		clientIds.add(client2.getId());
		
		List<Order> orders = new ArrayList<>();
		orders.add(order2);
		when(orderRepository.findOrdersByClientIds(clientIds)).thenReturn(orders);
	
		MvcResult result = mvc.perform(get("/orders?lastName=peco")).andExpect(status().isOk()).andReturn();
		String expected = stringFromResource("orders/ordersByFName.json");
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	@WithMockUser
	public void getClients_ContainsFirstName_NotFound() throws Exception {
		
		List<Client> clients = new ArrayList<>();
		when(clientRepository.getClientsContainsLastName("peco")).thenReturn(clients);
		MvcResult result = mvc.perform(get("/orders?lastName=peco")).andExpect(status().isOk()).andReturn();
		assertTrue(result.getResponse().getContentAsString().equals("{\"entity\":\"ORDER\",\"data\":[]}"));
	}
	
	
	@Test
	@WithMockUser
	public void saveOrder() throws Exception {
		
		when(clientRepository.findById(3)).thenReturn(Optional.of(client3));
		Address deliveryAddress = new Address("street 1", "21100", "pavia", "italy");
		Order order = new Order("jkl4r5", deliveryAddress, 5, 50, 3, null, null);
		String body = mapper.writeValueAsString(order);
		MvcResult result = mvc.perform(post("/orders").contentType(MediaType.APPLICATION_JSON_VALUE).content(body))
		.andExpect(status().isOk()).andReturn();
		CommonResponse<Map<String, String>> apiResponse = mapper.readValue(result.getResponse().getContentAsString(), 
				new TypeReference<CommonResponse<Map<String, String>>>(){});
		
		assertTrue(apiResponse.getEntity().equals(Entity.ORDER));
		assertTrue(apiResponse.getData() != null && !apiResponse.getData().isEmpty());
		assertTrue(apiResponse.getData().size() == 1);
		assertTrue(apiResponse.getData().get("orderNumber") != null);
		
	}
	
	@Test
	@WithMockUser
	public void saveOrder_clientNotFound() throws Exception {
		when(clientRepository.findById(3)).thenReturn(Optional.empty());
		Address deliveryAddress = new Address("street 1", "21100", "pavia", "italy");
		Order order = new Order("jkl4r5", deliveryAddress, 5, 50, 3, null, null);
		String body = mapper.writeValueAsString(order);
		MvcResult result = mvc.perform(post("/orders").contentType(MediaType.APPLICATION_JSON_VALUE).content(body))
		.andExpect(status().isNotFound()).andReturn();
		String expected = stringFromResource("orders/orderClientNotFound.json");
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	@WithMockUser
	public void saveOrder_PilotesNotAllowedException() throws Exception {
		when(clientRepository.findById(3)).thenReturn(Optional.of(client3));
		Address deliveryAddress = new Address("street 1", "21100", "pavia", "italy");
		Order order = new Order("jkl4r5", deliveryAddress, 55, 50, 3, null, null);
		String body = mapper.writeValueAsString(order);
		MvcResult result = mvc.perform(post("/orders").contentType(MediaType.APPLICATION_JSON_VALUE).content(body))
		.andExpect(status().isForbidden()).andReturn();
		String expected = stringFromResource("orders/orderPilotesNotAllowed.json");
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	@WithMockUser
	public void saveOrder_DeliveryAddressNull() throws Exception {
		when(clientRepository.findById(3)).thenReturn(Optional.of(client3));
		Order order = new Order("jkl4r5", null, 15, 50, 3, null, null);
		String body = mapper.writeValueAsString(order);
		MvcResult result = mvc.perform(post("/orders").contentType(MediaType.APPLICATION_JSON_VALUE).content(body))
		.andExpect(status().isBadRequest()).andReturn();
		String expected = stringFromResource("orders/ordersDeliveryAddressRequired.json");
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	@WithMockUser
	public void saveOrder_StreetNull() throws Exception {
		when(clientRepository.findById(3)).thenReturn(Optional.of(client3));
		Address deliveryAddress = new Address(null, "21100", "pavia", "italy");
		Order order = new Order("jkl4r5", deliveryAddress, 15, 50, 3, null, null);
		String body = mapper.writeValueAsString(order);
		MvcResult result = mvc.perform(post("/orders").contentType(MediaType.APPLICATION_JSON_VALUE).content(body))
		.andExpect(status().isBadRequest()).andReturn();
		String expected = stringFromResource("orders/ordersStreetRequired.json");
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	@WithMockUser
	public void saveOrder_PostcodeNull() throws Exception {
		when(clientRepository.findById(3)).thenReturn(Optional.of(client3));
		Address deliveryAddress = new Address("street 1", null, "pavia", "italy");
		Order order = new Order("jkl4r5", deliveryAddress, 15, 50, 3, null, null);
		String body = mapper.writeValueAsString(order);
		MvcResult result = mvc.perform(post("/orders").contentType(MediaType.APPLICATION_JSON_VALUE).content(body))
		.andExpect(status().isBadRequest()).andReturn();
		String expected = stringFromResource("orders/ordersPostcodeRequired.json");
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	@WithMockUser
	public void saveOrder_CityNull() throws Exception {
		when(clientRepository.findById(3)).thenReturn(Optional.of(client3));
		Address deliveryAddress = new Address("street 1", "21100", null, "italy");
		Order order = new Order("jkl4r5", deliveryAddress, 15, 50, 3, null, null);
		String body = mapper.writeValueAsString(order);
		MvcResult result = mvc.perform(post("/orders").contentType(MediaType.APPLICATION_JSON_VALUE).content(body))
		.andExpect(status().isBadRequest()).andReturn();
		String expected = stringFromResource("orders/ordersCityRequired.json");
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	@WithMockUser
	public void saveOrder_CountryNull() throws Exception {
		when(clientRepository.findById(3)).thenReturn(Optional.of(client3));
		Address deliveryAddress = new Address("street 1", "21100", "pavia", null);
		Order order = new Order("jkl4r5", deliveryAddress, 15, 50, 3, null, null);
		String body = mapper.writeValueAsString(order);
		MvcResult result = mvc.perform(post("/orders").contentType(MediaType.APPLICATION_JSON_VALUE).content(body))
		.andExpect(status().isBadRequest()).andReturn();
		String expected = stringFromResource("orders/ordersCountryRequired.json");
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	@WithMockUser
	public void saveOrder_OrderTotalNotValid() throws Exception {
		when(clientRepository.findById(3)).thenReturn(Optional.of(client3));
		Address deliveryAddress = new Address("street 1", "21100", "pavia", "italy");
		Order order = new Order("jkl4r5", deliveryAddress, 15, -5, 3, null, null);
		String body = mapper.writeValueAsString(order);
		MvcResult result = mvc.perform(post("/orders").contentType(MediaType.APPLICATION_JSON_VALUE).content(body))
		.andExpect(status().isBadRequest()).andReturn();
		String expected = stringFromResource("orders/orderTotalNotValid.json");
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	@WithMockUser
	public void updateOrder_OK() throws Exception {
		when(clientRepository.findById(3)).thenReturn(Optional.of(client3));
		when(orderRepository.findById("jkl4r5")).thenReturn(Optional.of(order4));
		Address deliveryAddress = new Address("street 1", "21100", "pavia", "italy");
		Order orderUpdate = new Order("jkl4r5", deliveryAddress, 15, 150, 3, null, null);
		String body = mapper.writeValueAsString(orderUpdate);
		MvcResult result = mvc.perform(put("/orders/jkl4r5").contentType(MediaType.APPLICATION_JSON_VALUE).content(body))
		.andExpect(status().isOk()).andReturn();
		String expected = stringFromResource("orders/ordersUpdateResponse.json");
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	@WithMockUser
	public void updateOrder_OrderNotFound() throws Exception {
		when(clientRepository.findById(3)).thenReturn(Optional.of(client3));
		when(orderRepository.findById("jkl4r5")).thenReturn(Optional.empty());
		Address deliveryAddress = new Address("street 1", "21100", "pavia", "italy");
		Order orderUpdate = new Order("jkl4r5", deliveryAddress, 15, 150, 3, null, null);
		String body = mapper.writeValueAsString(orderUpdate);
		MvcResult result = mvc.perform(put("/orders/jkl4r5").contentType(MediaType.APPLICATION_JSON_VALUE).content(body))
		.andExpect(status().isNotFound()).andReturn();
		String expected = stringFromResource("orders/ordersNotFound.json");
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	@WithMockUser
	public void updateOrder_UpdateNotAllowed() throws Exception {
		when(clientRepository.findById(2)).thenReturn(Optional.of(client2));
		when(orderRepository.findById("def321")).thenReturn(Optional.of(order2));
		Address deliveryAddress = new Address("street 1", "21100", "pavia", "italy");
		Order orderUpdate = new Order("def321", deliveryAddress, 15, 150, 2, null, null);
		String body = mapper.writeValueAsString(orderUpdate);
		MvcResult result = mvc.perform(put("/orders/def321").contentType(MediaType.APPLICATION_JSON_VALUE).content(body))
		.andExpect(status().isForbidden()).andReturn();
		String expected = stringFromResource("orders/ordersCannotBeUpdated.json");
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	@WithMockUser
	public void updateOrder_PilotesNotAllowedException() throws Exception {
		when(clientRepository.findById(3)).thenReturn(Optional.of(client3));
		when(orderRepository.findById("jkl4r5")).thenReturn(Optional.of(order4));
		Address deliveryAddress = new Address("street 1", "21100", "pavia", "italy");
		Order orderUpdate = new Order("jkl4r5", deliveryAddress, 13, 150, 3, null, null);
		String body = mapper.writeValueAsString(orderUpdate);
		MvcResult result = mvc.perform(put("/orders/jkl4r5").contentType(MediaType.APPLICATION_JSON_VALUE).content(body))
		.andExpect(status().isForbidden()).andReturn();
		String expected = stringFromResource("orders/orderPilotesNotAllowed.json");
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	@WithMockUser
	public void updateOrder_DeliveryAddressNull() throws Exception {
		when(clientRepository.findById(3)).thenReturn(Optional.of(client3));
		when(orderRepository.findById("jkl4r5")).thenReturn(Optional.of(order4));
		Order orderUpdate = new Order("jkl4r5", null, 15, 150, 3, null, null);
		String body = mapper.writeValueAsString(orderUpdate);
		MvcResult result = mvc.perform(put("/orders/jkl4r5").contentType(MediaType.APPLICATION_JSON_VALUE).content(body))
		.andExpect(status().isBadRequest()).andReturn();
		String expected = stringFromResource("orders/ordersDeliveryAddressRequired.json");
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	@WithMockUser
	public void updateOrder_StreetEmpty() throws Exception {
		when(clientRepository.findById(3)).thenReturn(Optional.of(client3));
		when(orderRepository.findById("jkl4r5")).thenReturn(Optional.of(order4));
		Address deliveryAddress = new Address(" ", "21100", "pavia", "italy");
		Order orderUpdate = new Order("jkl4r5", deliveryAddress, 15, 150, 3, null, null);
		String body = mapper.writeValueAsString(orderUpdate);
		MvcResult result = mvc.perform(put("/orders/jkl4r5").contentType(MediaType.APPLICATION_JSON_VALUE).content(body))
		.andExpect(status().isBadRequest()).andReturn();
		String expected = stringFromResource("orders/ordersStreetRequired.json");
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	@WithMockUser
	public void updateOrder_PostcodeNull() throws Exception {
		when(clientRepository.findById(3)).thenReturn(Optional.of(client3));
		when(orderRepository.findById("jkl4r5")).thenReturn(Optional.of(order4));
		Address deliveryAddress = new Address("street 1", null, "pavia", "italy");
		Order orderUpdate = new Order("jkl4r5", deliveryAddress, 15, 150, 3, null, null);
		String body = mapper.writeValueAsString(orderUpdate);
		MvcResult result = mvc.perform(put("/orders/jkl4r5").contentType(MediaType.APPLICATION_JSON_VALUE).content(body))
		.andExpect(status().isBadRequest()).andReturn();
		String expected = stringFromResource("orders/ordersPostcodeRequired.json");
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	@WithMockUser
	public void updateOrder_CityNull() throws Exception {
		when(clientRepository.findById(3)).thenReturn(Optional.of(client3));
		when(orderRepository.findById("jkl4r5")).thenReturn(Optional.of(order4));
		Address deliveryAddress = new Address("street 1", "21100", null, "italy");
		Order orderUpdate = new Order("jkl4r5", deliveryAddress, 15, 150, 3, null, null);
		String body = mapper.writeValueAsString(orderUpdate);
		MvcResult result = mvc.perform(put("/orders/jkl4r5").contentType(MediaType.APPLICATION_JSON_VALUE).content(body))
		.andExpect(status().isBadRequest()).andReturn();
		String expected = stringFromResource("orders/ordersCityRequired.json");
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	@WithMockUser
	public void updateOrder_CountryEmpty() throws Exception {
		when(clientRepository.findById(3)).thenReturn(Optional.of(client3));
		when(orderRepository.findById("jkl4r5")).thenReturn(Optional.of(order4));
		Address deliveryAddress = new Address("street 1", "21100", "pavia", "");
		Order orderUpdate = new Order("jkl4r5", deliveryAddress, 15, 150, 3, null, null);
		String body = mapper.writeValueAsString(orderUpdate);
		MvcResult result = mvc.perform(put("/orders/jkl4r5").contentType(MediaType.APPLICATION_JSON_VALUE).content(body))
		.andExpect(status().isBadRequest()).andReturn();
		String expected = stringFromResource("orders/ordersCountryRequired.json");
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	@WithMockUser
	public void updateOrder_OrderTotalNotValid() throws Exception {
		when(clientRepository.findById(3)).thenReturn(Optional.of(client3));
		when(orderRepository.findById("jkl4r5")).thenReturn(Optional.of(order4));
		Address deliveryAddress = new Address("street 1", "21100", "pavia", "italy");
		Order orderUpdate = new Order("jkl4r5", deliveryAddress, 15, -150, 3, null, null);
		String body = mapper.writeValueAsString(orderUpdate);
		MvcResult result = mvc.perform(put("/orders/jkl4r5").contentType(MediaType.APPLICATION_JSON_VALUE).content(body))
		.andExpect(status().isBadRequest()).andReturn();
		String expected = stringFromResource("orders/orderTotalNotValid.json");
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	
	
	private String stringFromResource(String path) throws IOException, URISyntaxException {
		return new String(Files.readAllBytes(
				Paths.get(ClientControllerTest.class.getClassLoader().getResource(path).toURI())));
	}
}
