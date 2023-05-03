package org.eni.encheres.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {

	
	private Integer noCategory;
	private String libelle;
	
	
	public Category(Integer noCategory) {
		this.noCategory = noCategory;
	}
	
	
}
