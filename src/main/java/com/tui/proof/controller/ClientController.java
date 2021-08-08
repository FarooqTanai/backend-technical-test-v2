package com.tui.proof.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import com.tui.proof.model.Client;
import com.tui.proof.model.Entity;
import com.tui.proof.model.response.CommonResponse;
import com.tui.proof.service.ClientService;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class ClientController {

	
	@Autowired
	private ClientService clientService;


	@RequestMapping(value = "/clients", method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<CommonResponse<List<Client>>> getClients() {
		
		log.info("Request for getAllClents");
		List<Client> clients = clientService.getAllClients();
		log.debug("Response for getAllClents: {}", clients);
		return new ResponseEntity<>(new CommonResponse<>(Entity.CLIENT, clients), HttpStatus.OK);
	}
	

	@RequestMapping(value = "/clients", method = POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<CommonResponse<Map<String, Integer>>> saveClient(@RequestBody Client client) {
		log.info("Request for save client: {}", client);
		Map<String, Integer> response = clientService.saveClient(client);
		log.info("Response for save client: {}", response);
		return new ResponseEntity<>(new CommonResponse<>(Entity.CLIENT, response), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/clients/{id}", method = PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<CommonResponse<Map<String, Integer>>> updateClient(@RequestBody Client client, @PathVariable int id) {
		log.info("Request for update client: {}", client);
		Map<String, Integer> response = clientService.updateClient(id, client);
		log.info("Response for update client: {}", response);
		return new ResponseEntity<>(new CommonResponse<>(Entity.CLIENT, response), HttpStatus.OK);
	}
	
	
}
