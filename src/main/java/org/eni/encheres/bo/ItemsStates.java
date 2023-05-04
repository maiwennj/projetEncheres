package org.eni.encheres.bo;

public enum ItemsStates {
	
	NEW("N"),
	UNDERWAY("E"),
	FINISHED("T"),
	ARCHIVED("A");
	
	private final String state;
	private ItemsStates(String state) {
		this.state=state;
	}
	
	public String getState() {return state;}
	
}
