package org.eni.encheres.bll.exception;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BLLException extends Exception{

	private List<String> erreurs = new ArrayList<>();
	
	
	public BLLException(String message) {
		super(message);
	}
	
	public void addError(String erreur) {
		erreurs.add(erreur);
	}
	
	@Override
	public String getMessage() {
		return "BLL : "+super.getMessage();
	}


	
}