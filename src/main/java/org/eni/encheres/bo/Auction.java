package org.eni.encheres.bo;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Auction {
	
	private User user;
	private Item item;
	private LocalDateTime auctionDate;
	private Integer bid;
	
	/**
	 * This constructor is used when an object ItemAllInformation is created by a search result.
	 * @param user
	 * @param item
	 * @param bid
	 */
	public Auction(User user, Item item, Integer bid) {
		super();
		this.user = user;
		this.item = item;
		this.bid = bid;
	}
	
	
	
}