package com.tui.proof.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ClientOrder")
public class Order {
	@Id
	private String number;
	private Address deliveryAddress;
	private int pilotes;
	private double orderTotal;
	private int clientId;
	private Date orderCreateDate;
	private Date orderUpdateDate;

}
