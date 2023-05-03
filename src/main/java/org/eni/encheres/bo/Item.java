package org.eni.encheres.bo;

import java.time.LocalDate;

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
	@NonNull private Integer initialePrice;
	@NonNull private Integer sellingPrice;
	@NonNull private User user;
	@NonNull private Category category;
//	private String state;
	
}
