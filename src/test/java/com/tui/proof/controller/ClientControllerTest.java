package com.tui.proof.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tui.proof.errors.ProjectExceptionHandler;
import com.tui.proof.model.Client;
import com.tui.proof.repository.ClientRepository;
import com.tui.proof.service.ClientService;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
@WebMvcTest(ClientController.class)
@ContextConfiguration(classes = {ClientController.class, 
								ClientService.class, 
								ProjectExceptionHandler.class
								})
public class ClientControllerTest {

	
	private MockMvc mvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Autowired
	private ObjectMapper mapper;
	
	@MockBean
	private ClientRepository repository;
	
	private Client client1;
	
	private Client client2;
	
	private Client client3;
	
	
	@Before
	public void setUp() {
		this.mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.build();
		
		client1 = new Client(1, "tom", "jerry", "+393880000000", "test@mail.com", "male");
		client2 = new Client(2, "pecos", "joan", "+393880000002", "test2@mail.com", "female");
		client3 = new Client(3, "george", "little", "+393880000003", "test3@mail.com", "male");
		List<Client> clients = new ArrayList<>();
		clients.add(client1);
		clients.add(client2);
		clients.add(client3);
		when(repository.findAll()).thenReturn(clients);
	}
	
	
	@Test
	@WithMockUser
	public void getClients_OK() throws Exception {
		MvcResult result = mvc.perform(get("/clients")).andExpect(status().isOk()).andReturn();
		String expected = stringFromResource("clients/allClientsResponse.json");
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), true);
	}
	
	@Test
	@WithMockUser
	public void getClients_NotFound() throws Exception {
		when(repository.findAll()).thenReturn(new ArrayList<>());
		MvcResult result = mvc.perform(get("/clients")).andExpect(status().isNotFound()).andReturn();
		String expected = stringFromResource("clients/clientsNotFound.json");
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}

	@Test
	@WithMockUser
	public void saveClient() throws Exception {
		when(repository.save(client1)).thenReturn(client1);
		String body = mapper.writeValueAsString(client1);
		MvcResult result = mvc.perform(post("/clients").contentType(MediaType.APPLICATION_JSON_VALUE).content(body))
		.andExpect(status().isOk()).andReturn();
		String expected = stringFromResource("clients/clientSaveResponse.json");
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), true);
	}
	
	@Test
	@WithMockUser
	public void saveClient_firstNameNull() throws Exception {
		String body = "{\"id\":1,\"lastName\":\"jerry\",\"telephone\":\"+393880000000\",\"email\":\"test@mail.com\",\"sex\":\"male\"}";
		MvcResult result = mvc.perform(post("/clients").contentType(MediaType.APPLICATION_JSON_VALUE).content(body))
		.andExpect(status().isBadRequest()).andReturn();
		String expected = stringFromResource("clients/clientFirstNameRequired.json");
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	@WithMockUser
	public void saveClient_firstNameEmpty() throws Exception {
		String body = "{\"id\":1,\"firstName\":\"   \",\"lastName\":\"jerry\",\"telephone\":\"+393880000000\",\"email\":\"test@mail.com\",\"sex\":\"male\"}";
		MvcResult result = mvc.perform(post("/clients").contentType(MediaType.APPLICATION_JSON_VALUE).content(body))
		.andExpect(status().isBadRequest()).andReturn();
		String expected = stringFromResource("clients/clientFirstNameRequired.json");
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	@WithMockUser
	public void saveClient_lastNameNull() throws Exception {
		String body = "{\"id\":1,\"firstName\":\"tom\",\"telephone\":\"+393880000000\",\"email\":\"test@mail.com\",\"sex\":\"male\"}";
		MvcResult result = mvc.perform(post("/clients").contentType(MediaType.APPLICATION_JSON_VALUE).content(body))
		.andExpect(status().isBadRequest()).andReturn();
		String expected = stringFromResource("clients/clientlastNameRequired.json");
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	@WithMockUser
	public void saveClient_telephoneNull() throws Exception {
		String body = "{\"id\":1,\"firstName\":\"tom\",\"lastName\":\"jerry\",\"email\":\"test@mail.com\",\"sex\":\"male\"}";
		MvcResult result = mvc.perform(post("/clients").contentType(MediaType.APPLICATION_JSON_VALUE).content(body))
		.andExpect(status().isBadRequest()).andReturn();
		String expected = stringFromResource("clients/clientTelephoneRequired.json");
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	@WithMockUser
	public void saveClient_emailNull() throws Exception {
		String body = "{\"id\":1,\"firstName\":\"tom\",\"lastName\":\"jerry\",\"telephone\":\"+393880000000\",\"sex\":\"male\"}";
		MvcResult result = mvc.perform(post("/clients").contentType(MediaType.APPLICATION_JSON_VALUE).content(body))
		.andExpect(status().isBadRequest()).andReturn();
		String expected = stringFromResource("clients/clientEmailRequired.json");
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	@WithMockUser
	public void saveClient_sexNull() throws Exception {
		String body = "{\"id\":1,\"firstName\":\"tom\",\"lastName\":\"jerry\",\"telephone\":\"+393880000000\",\"email\":\"test@mail.com\"}";
		MvcResult result = mvc.perform(post("/clients").contentType(MediaType.APPLICATION_JSON_VALUE).content(body))
		.andExpect(status().isBadRequest()).andReturn();
		String expected = stringFromResource("clients/clientSexRequired.json");
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	@WithMockUser
	public void saveClient_telephoneNotValid() throws Exception {
		String body = "{\"id\":1,\"firstName\":\"tom\",\"lastName\":\"jerry\",\"telephone\":\"3880000000\",\"email\":\"test@mail.com\",\"sex\":\"male\"}";
		MvcResult result = mvc.perform(post("/clients").contentType(MediaType.APPLICATION_JSON_VALUE).content(body))
		.andExpect(status().isBadRequest()).andReturn();
		String expected = stringFromResource("clients/clientTelephoneNotValid.json");
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	@WithMockUser
	public void saveClient_emailNotValid() throws Exception {
		String body = "{\"id\":1,\"firstName\":\"tom\",\"lastName\":\"jerry\",\"telephone\":\"+393880000000\",\"email\":\"hiHowAre.You\",\"sex\":\"male\"}";
		MvcResult result = mvc.perform(post("/clients").contentType(MediaType.APPLICATION_JSON_VALUE).content(body))
		.andExpect(status().isBadRequest()).andReturn();
		String expected = stringFromResource("clients/clientEmailNotValid.json");
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	@WithMockUser
	public void saveClient_sexNotValid() throws Exception {
		String body = "{\"id\":1,\"firstName\":\"tom\",\"lastName\":\"jerry\",\"telephone\":\"+393880000000\",\"email\":\"test@mail.com\",\"sex\":\"child\"}";
		MvcResult result = mvc.perform(post("/clients").contentType(MediaType.APPLICATION_JSON_VALUE).content(body))
		.andExpect(status().isBadRequest()).andReturn();
		String expected = stringFromResource("clients/clientSexNotValid.json");
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	@WithMockUser
	public void updateClient() throws Exception {
		Client updatedClient = new Client(2, "newName", "little", "+393880000003", "test3@mail.com", "other");
		when(repository.save(updatedClient)).thenReturn(updatedClient);
		when(repository.findById(2)).thenReturn(Optional.of(client2));
		String body = mapper.writeValueAsString(updatedClient);
		MvcResult result = mvc.perform(put("/clients/2").contentType(MediaType.APPLICATION_JSON_VALUE).content(body))
		.andExpect(status().isOk()).andReturn();
		String expected = stringFromResource("clients/clientUpdateResponse.json");
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), true);
	}
	
	@Test
	@WithMockUser
	public void updateClient_notFound() throws Exception {
		Client updatedClient = new Client(2, "newName", "little", "+393880000003", "test3@mail.com", "other");
		when(repository.save(updatedClient)).thenReturn(updatedClient);
		when(repository.findById(2)).thenReturn(Optional.empty());
		String body = mapper.writeValueAsString(updatedClient);
		MvcResult result = mvc.perform(put("/clients/2").contentType(MediaType.APPLICATION_JSON_VALUE).content(body))
		.andExpect(status().isNotFound()).andReturn();
		String expected = stringFromResource("clients/clientUpdateNotFound.json");
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	@WithMockUser
	public void updateClient_fNameNull() throws Exception {
		Client updatedClient = new Client(2, null, "little", "+393880000003", "test3@mail.com", "other");
		when(repository.save(updatedClient)).thenReturn(updatedClient);
		when(repository.findById(2)).thenReturn(Optional.of(client2));
		String body = mapper.writeValueAsString(updatedClient);
		MvcResult result = mvc.perform(put("/clients/2").contentType(MediaType.APPLICATION_JSON_VALUE).content(body))
		.andExpect(status().isBadRequest()).andReturn();
		String expected = stringFromResource("clients/clientFirstNameRequired.json");
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	@WithMockUser
	public void updateClient_lNameNull() throws Exception {
		Client updatedClient = new Client(2, "newName", null, "+393880000003", "test3@mail.com", "other");
		when(repository.save(updatedClient)).thenReturn(updatedClient);
		when(repository.findById(2)).thenReturn(Optional.of(client2));
		String body = mapper.writeValueAsString(updatedClient);
		MvcResult result = mvc.perform(put("/clients/2").contentType(MediaType.APPLICATION_JSON_VALUE).content(body))
		.andExpect(status().isBadRequest()).andReturn();
		String expected = stringFromResource("clients/clientlastNameRequired.json");
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	@WithMockUser
	public void updateClient_telephoneNull() throws Exception {
		Client updatedClient = new Client(2, "newName", "little", null, "test3@mail.com", "other");
		when(repository.save(updatedClient)).thenReturn(updatedClient);
		when(repository.findById(2)).thenReturn(Optional.of(client2));
		String body = mapper.writeValueAsString(updatedClient);
		MvcResult result = mvc.perform(put("/clients/2").contentType(MediaType.APPLICATION_JSON_VALUE).content(body))
		.andExpect(status().isBadRequest()).andReturn();
		String expected = stringFromResource("clients/clientTelephoneRequired.json");
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	@WithMockUser
	public void updateClient_EmailNull() throws Exception {
		Client updatedClient = new Client(2, "newName", "little", "+393880000003", null, "other");
		when(repository.save(updatedClient)).thenReturn(updatedClient);
		when(repository.findById(2)).thenReturn(Optional.of(client2));
		String body = mapper.writeValueAsString(updatedClient);
		MvcResult result = mvc.perform(put("/clients/2").contentType(MediaType.APPLICATION_JSON_VALUE).content(body))
		.andExpect(status().isBadRequest()).andReturn();
		String expected = stringFromResource("clients/clientEmailRequired.json");
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	@WithMockUser
	public void updateClient_SexNull() throws Exception {
		Client updatedClient = new Client(2, "newName", "little", "+393880000003", "test3@mail.com", null);
		when(repository.save(updatedClient)).thenReturn(updatedClient);
		when(repository.findById(2)).thenReturn(Optional.of(client2));
		String body = mapper.writeValueAsString(updatedClient);
		MvcResult result = mvc.perform(put("/clients/2").contentType(MediaType.APPLICATION_JSON_VALUE).content(body))
		.andExpect(status().isBadRequest()).andReturn();
		String expected = stringFromResource("clients/clientSexRequired.json");
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	
	
	private String stringFromResource(String path) throws IOException, URISyntaxException {
		return new String(Files.readAllBytes(
				Paths.get(ClientControllerTest.class.getClassLoader().getResource(path).toURI())));
	}
	
}
