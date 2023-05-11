package org.eni.encheres.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class User {
	
	private Integer noUser;
	@NonNull private String username;
	@NonNull private String firstName;
	@NonNull private String lastName;
	@NonNull private String email;
	@NonNull private String phoneNumber;
	@NonNull private String street;
	@NonNull private String postCode;
	@NonNull private String city;
	@NonNull private String password;
	@NonNull private Integer credit;
	private boolean isAdmin;
	
	
	public User(@NonNull String username, @NonNull String password) {
		super();
		this.username = username;
		this.password = password;
	}


	public User(Integer noUser) {
		super();
		this.noUser = noUser;
	}


	
	
	public User(String username, String firstname, String lastname, String email, String phoneNumber,
			String street, String postCode, String city, String password) {	
		this.username = username;
		this.firstName = firstname;
		this.lastName = lastname;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.street = street;
		this.postCode = postCode;
		this.city = city;
		this.password = password;
		
	}

	/**
	 * This constructor is used when an object ItemAllInformation is created by a search result.
	 * @param noUser
	 * @param username
	 */
	public User(Integer noUser, @NonNull String username) {
		super();
		this.noUser = noUser;
		this.username = username;
	}
	
	/**
	 * This constructor is used when an object ItemAllInformation is created by a search result.
	 * @param noUser
	 * @param username
	 */
	public User(Integer noUser, @NonNull String username, String phoneNumber) {
		super();
		this.noUser = noUser;
		this.username = username;
		this.phoneNumber = phoneNumber;
	}
	
	
}
