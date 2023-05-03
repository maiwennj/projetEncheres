package org.eni.encheres.bo;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Auction {
	
	private User user;
	private Item item;
	private LocalDate auctionDate;
	private Integer bid;
	
}
