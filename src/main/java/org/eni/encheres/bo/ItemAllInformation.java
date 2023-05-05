package org.eni.encheres.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemAllInformation {
	
	private Item item;
	private User user;
	private Auction auction;
	private Category category;
	private CollectionPoint collectionPoint;
	
	public ItemAllInformation(User user,Item item, Auction auction) {
		super();
		this.item = item;
		this.user = user;
		this.auction = auction;
	}
	
}
