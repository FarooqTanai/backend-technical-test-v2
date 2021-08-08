package com.tui.proof.service;

import static com.tui.proof.utils.Util.FIELD_FIRST_NAME;
import static com.tui.proof.utils.Util.FIELD_LAST_NAME;

import com.tui.proof.errors.ClientNotFoundException;
import com.tui.proof.errors.ClientsNotFoundException;
import com.tui.proof.model.Client;
import com.tui.proof.model.Entity;
import com.tui.proof.repository.ClientRepository;
import com.tui.proof.utils.Validator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class ClientService {

	@Autowired
	private ClientRepository clientRepository;

	public List<Client> getAllClients() {
		List<Client> clients = clientRepository.findAll();
		if (clients.isEmpty()) {
			log.info("Clients not found");
			throw new ClientsNotFoundException();
		}
		return clients;
	}

	public Map<String, Integer> saveClient(Client client) {
		validateClient(client);
		Client saved = clientRepository.save(client);
		Map<String, Integer> response = new HashMap<>();
		response.put("ClientId", saved.getId());
		return response;
	}

	public Map<String, Integer> updateClient(int id, Client client) {

		Optional<Client> optional = clientRepository.findById(id);
		if (!optional.isPresent()) {
			log.info("Client {} not found", id);
			throw new ClientNotFoundException(String.valueOf(id));
		}
		client.setId(id);
		saveClient(client);
		Map<String, Integer> response = new HashMap<>();
		response.put("ClientId", id);
		return response;
	}

	private void validateClient(Client client) {
		Validator.validateField(FIELD_FIRST_NAME, client.getFirstName(), Entity.CLIENT);
		Validator.validateField(FIELD_LAST_NAME, client.getLastName(), Entity.CLIENT);
		Validator.validateTelephone(client.getTelephone(), Entity.CLIENT);
		Validator.validateEmail(client.getEmail(), Entity.CLIENT);
		client.setSex(Validator.validateSex(client.getSex(), Entity.CLIENT));
	}

	
	public List<Client> getClientsContainsFirstNameAndLastName(String clientFname, String clientLname) {
		return clientRepository.getClientsContainsFirstNameAndLastName(clientFname, clientLname);
	}

	public List<Client> getClientsContainsFirstName(String clientFname) {
		return clientRepository.getClientsContainsFirstName(clientFname);
	}

	public List<Client> getClientsContainsLastName(String clientLname) {
		return clientRepository.getClientsContainsLastName(clientLname);
	}

	public boolean clientExist(int clientId) {
		Optional<Client> client = clientRepository.findById(clientId);
		if(client.isPresent()) {
			return true;
		}
		return false;
	}

}
