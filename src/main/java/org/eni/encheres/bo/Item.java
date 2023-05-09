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
	@NonNull private LocalDateTime startDate;
	@NonNull private LocalDateTime endDate;
	@NonNull private Integer initialPrice;
	private Integer sellingPrice;
	@NonNull private User user;
	@NonNull private Category category;
	private String state;
	
	// this constructor is used for the Items created by a user (noItem will be generated, and sellingPrice will be initialized at null)
	public Item(@NonNull String itemTitle, @NonNull String description, @NonNull LocalDateTime startDate,
			@NonNull LocalDateTime endDate, @NonNull Integer initialPrice, @NonNull User user, @NonNull Category category,
			String state) {
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

	/**
	 * This constructor is used when an object ItemAllInformation is created by a search result.
	 * @param noItem
	 * @param itemTitle
	 * @param description
	 * @param startDate
	 * @param endDate
	 * @param initialPrice
	 * @param user
	 */
	public Item(Integer noItem, @NonNull String itemTitle, @NonNull String description,
			@NonNull LocalDateTime startDate, @NonNull LocalDateTime endDate, @NonNull Integer initialPrice) {
		super();
		this.noItem = noItem;
		this.itemTitle = itemTitle;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.initialPrice = initialPrice;
	}


	
}
