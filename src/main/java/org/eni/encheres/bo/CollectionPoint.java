package org.eni.encheres.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectionPoint {

	private Item item;
	private String streetCP;
	private String postCodeCP;
	private String cityCP;
	
}
