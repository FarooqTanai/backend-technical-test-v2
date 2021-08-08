package com.tui.proof.repository;

import com.tui.proof.model.Client;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClientRepository extends JpaRepository<Client, Integer>{


	@Query("select u from Client u where u.firstName like %?1% and u.lastName like %?2%")
	List<Client> getClientsContainsFirstNameAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);
	
	@Query("select u from Client u where u.firstName like %?1%")
	List<Client> getClientsContainsFirstName(@Param("firstName") String firstName);
	
	@Query("select u from Client u where u.lastName like %?1%")
	List<Client> getClientsContainsLastName(@Param("lastName") String lastName);
}
