package org.eni.encheres.bll;

import org.eni.encheres.bll.exception.BLLException;
import org.eni.encheres.bo.User;
import org.eni.encheres.dal.DaoFactory;


import at.favre.lib.crypto.bcrypt.BCrypt;

import lombok.Getter;


public class UserManager {

	@Getter private static UserManager instance = new UserManager();
	private UserManager() {}
		
	public User selectOneUser(Integer noUser) {
		return DaoFactory.getUserDao().selectOneUser(noUser);
	}

	public void addUser(User user) throws BLLException {
		user.setPassword(BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray()));
		checkUser(user);
		DaoFactory.getUserDao().insertUser(user);
	}

	public User getUser(int id) {
		return DaoFactory.getUserDao().selectOneUser(id);
	}

	/**
	 * Vérifie tous les champs de user en utilisant la méthode checkField
	 * 
	 * @param user
	 * @throws BLLException
	 */
	private void checkUser(User user) throws BLLException {
		BLLException bll = new BLLException();
		checkField(user.getUsername(),"pseudo",bll);
		checkField(user.getLastName(),"nom",bll);
		checkField(user.getFirstName(),"prénom",bll);
		checkField(user.getEmail(),"email",bll);
		checkField(user.getPhoneNumber(),"numéro de téléphone",bll);
		checkField(user.getStreet(),"rue",bll);
		checkField(user.getPostCode(),"code postal",bll);
		checkField(user.getCity(),"ville",bll);
		if (bll.getErreurs().size()>0) {
			throw bll;
		}
	}


	/**
	 * Vérifie les champs en fonction du type de nom (pseudo,email,tel,etc..).<br/>
	 * Si un problème est trouvé, un message est ajouté à la liste d'erreur qui est remontée à la fin du traitement.
	 * 
	 * @param field
	 * @param name
	 * @param bll
	 */
	private void checkField(String field, String name, BLLException bll) {
		if (field.isBlank()) {
			bll.addError("Le champs %s ne peut pas être vide".formatted(name));
		}else {
		
			if (name.equals("pseudo")) {
				if (DaoFactory.getUserDao().selectByUserName(field)!=null) {
					bll.addError("Le nom d'utilisateur est déjà utilisé");
				}
				if (!field.matches("[A-Za-z0-9]{3,30}")) {
					bll.addError("Le nom d'utilisateur doit être constitué uniquement de caractères alphanumériques");
				}
				if(field.length()<3) {
					bll.addError("Le pseudo n'est pas assez long");
				}
			}
			
			if (name.equals("nom")
					|| name.equals("prénom")
					|| name.equals("rue")
					|| name.equals("ville")) {
				
				if (field.length()>30) {
					bll.addError("Le champs %s ne doit pas dépasser 30 charactères".formatted(name));
				}	
			}
			
			if (name.equals("email")) {
				if (DaoFactory.getUserDao().selectByEmail(field)!=null) {
					bll.addError("Le mail est déjà utilisé");
				}
				if(!field.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
					bll.addError("Le champs %s ne respect pas le format attendu".formatted(name));	
				}
				if(field.length()>20) {
					bll.addError("Le champs %s ne doit pas dépasser 20 charactères".formatted(name));	
				}
			}
			
			if (name.equals("numéro de téléphone")) {
				if(!field.matches("[0-9]{10}")) {
					bll.addError("Le numéro de téléphone ne respect pas le format attendu".formatted(name));	
				}
				if(field.length()>15) {
					bll.addError("Le numéro de téléphone ne doit pas dépasser 15 charactères".formatted(name));	
				}
			}
			
			if (name.equals("code postal")) {
				if(!field.matches("^(([0-8][0-9])|(9[0-5])|(2[ab]))[0-9]{3}$")) {
					bll.addError("Le code postal ne respect pas le format attendu".formatted(name));	
				}
				if(field.length()>10) {
					bll.addError("Le code postal ne doit pas dépasser 10 charactères".formatted(name));	
				}
			}
		}
	}

	
	/**
	 *  1. Vérifie si le compte existe(si existe pas, retourne une erreur)<br/>
	 *  2. Vérifie si le mot de passe est valide ( si invalide, retourn une erreur <br/>
	 *  3. Retourne user si tout est bon
	 *
	 * @param username
	 * @param password
	 * @return user for session
	 * @throws BLLException
	 */
	public User login(String login, String password) throws BLLException {
		BLLException bll = new BLLException();
		User user = DaoFactory.getUserDao().selectByLogin(login);
		if(user!=null) {
			//on chèque le mot de passe, si c'est bon
			BCrypt.Result result = BCrypt.verifyer()
					.verify(password.toCharArray(), user.getPassword());
			if (!result.verified) {
				bll.addError("Le mot de passe est erroné");
				throw bll;
			} else {
				return user;
			}
		}else {
			bll.addError("Pas d'utilisateur trouvé");
			throw bll;
		}
	}

	/**
	 * 
	 * @param user
	 * @throws BLLException 
	 */
	public void updateUser(User user, Integer noUser) throws BLLException {
		user.setPassword(BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray()));
		checkUpdate(user,noUser);
		DaoFactory.getUserDao().updateUser(user,noUser);
		
	}

	private void checkUpdate(User user, Integer noUser) throws BLLException {
		BLLException bll = new BLLException();
		//vérification si username utilisé par un autre utilisateur
		if (DaoFactory.getUserDao().checkUsernameIsNotUsed(user.getUsername(),noUser)!=null) {
			bll.addError("Le nom d'utilisateur est déjà utilisé par un autre utilisateur");
		}else {//vérification des champs de saisie
			if (!user.getUsername().matches("[A-Za-z0-9]{3,30}")) {
				bll.addError("Le nom d'utilisateur doit être constitué uniquement de caractères alphanumériques");
			}
			if(user.getUsername().length()<3) {
				bll.addError("Le pseudo n'est pas assez long");
			}
		}
		//vérification si email utilisé par un autre utilisateur
		if (DaoFactory.getUserDao().checkEmailIsNotUsed(user.getEmail(),noUser)!=null) {
			bll.addError("Le mail est déjà utilisé par un autre utilisateur");
		}else {//vérification des champs de saisie
			if(!user.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
				bll.addError("Le champs email ne respect pas le format attendu");	
			}
			if(user.getEmail().length()>20) {
				bll.addError("Le champs email ne doit pas dépasser 20 charactères");	
			}
		}
		checkField(user.getLastName(),"nom",bll);
		checkField(user.getFirstName(),"prénom",bll);
		checkField(user.getPhoneNumber(),"numéro de téléphone",bll);
		checkField(user.getStreet(),"rue",bll);
		checkField(user.getPostCode(),"code postal",bll);
		checkField(user.getCity(),"ville",bll);
		if (bll.getErreurs().size()>0) {
			throw bll;
		}
	}
	
	
	
	
	
}
