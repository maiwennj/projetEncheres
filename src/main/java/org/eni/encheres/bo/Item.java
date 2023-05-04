package org.eni.encheres.bo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class Item {
	
	private Integer noItem;
	@NonNull private String itemTitle;
	@NonNull private String description;
	@NonNull private LocalDate startDate;
	@NonNull private LocalDate endDate;
	@NonNull private Integer initialPrice;
	private Integer sellingPrice;
	@NonNull private User user;
	@NonNull private Category category;
	private Character state;
	
	// this constructor is used for the Items created by an user (noItem will be generated, and sellingPrice will be initialized at null)
	public Item(@NonNull String itemTitle, @NonNull String description, @NonNull LocalDate startDate,
			@NonNull LocalDate endDate, @NonNull Integer initialPrice, @NonNull User user, @NonNull Category category,
			Character state) {
		super();
		this.itemTitle = itemTitle;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.initialPrice = initialPrice;
		this.user = user;
		this.category = category;
		this.state = state;
	}

	
	
	// TEST ENCHERES A SUPPRIMER
	
	public Item(Integer noItem) {
		super();
		this.noItem = noItem;
	}
}
