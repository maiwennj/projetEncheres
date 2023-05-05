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
	@NonNull private Integer initialePrice;
	private Integer sellingPrice;
	@NonNull private User user;
	@NonNull private Category category;
	private String state;
	
	public Item(Integer noItem, @NonNull String itemTitle, @NonNull String description,
			@NonNull LocalDateTime startDate, @NonNull LocalDateTime endDate, @NonNull Integer initialePrice,
			Integer sellingPrice, @NonNull User user, @NonNull Category category) {
		super();
		this.noItem = noItem;
		this.itemTitle = itemTitle;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.initialePrice = initialePrice;
		this.sellingPrice = sellingPrice;
		this.user = user;
		this.category = category;
	}

	public Item(int int1, String string, String string2, LocalDate localDate, LocalDate localDate2, int int2, int int3,
			User user2, Category category2, String string3) {
		// TODO Auto-generated constructor stub
	}


	
	
	
	
	
	
}
