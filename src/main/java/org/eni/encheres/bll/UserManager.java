package org.eni.encheres.bll;

import org.eni.encheres.bll.exception.BLLException;
import org.eni.encheres.bo.User;
import org.eni.encheres.dal.DaoFactory;


import at.favre.lib.crypto.bcrypt.BCrypt;

import lombok.Getter;


public class UserManager {
	//singleton getInstance();
	@Getter private static UserManager instance = new UserManager();
	private UserManager() {}
	
	//******************** CRUD
	//CREATE ------------
	public void addUser(User user) throws BLLException {
		user.setPassword(BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray()));
		checkCreate(user);
		DaoFactory.getUserDao().insertUser(user);
	}
	
	//READ ------------
	public User selectOneUser(Integer noUser) {
		return DaoFactory.getUserDao().selectOneUser(noUser);
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
	
	// UPDATE ------------
	/**
	 * 1.cripte le mot de passe, vérifie si c'est le bon <br/>
	 * 2.fait les checks field et checks is used <br/>
	 * 3.si les checks sont bon on lance l'update de user <br/>
	 * 4.après l'update on récupère user pour la session directement dans la BDD <br/>
	 * @param user
	 * @throws BLLException 
	 */
	public void updateUser(User userUpdate) throws BLLException {
		userUpdate.setPassword(BCrypt.withDefaults().hashToString(12, userUpdate.getPassword().toCharArray()));
		checkUpdate(userUpdate);
		DaoFactory.getUserDao().updateUser(userUpdate);
	}
	
	//DELETE ------------
	
	public void deleteUser(User user) {
		DaoFactory.getUserDao().deleteUser(user);
	}
	
	
	//CHECKS ------------
	
	/**
	 * Méthode de check pour la création d'utilisateur
	 * Vérifie tous les champs de user en utilisant les méthodes:
	 * 1. createUsernameIsNotUsed()<br/>
	 * 2. createEmailIsNotUsed()<br/>
	 * 3. checkField()
	 * @param user
	 * @throws BLLException
	 */
	private void checkCreate(User user) throws BLLException {
		BLLException bll = new BLLException();
		createUsernameIsNotUsed(user.getUsername(),bll);
		createEmailIsNotUsed(user.getEmail(),bll);
		if (bll.getErreurs().size()>0) {
			throw bll;
		}else {
			checkField(user.getUsername(),"pseudo",bll);
			checkField(user.getLastName(),"nom",bll);
			checkField(user.getFirstName(),"prénom",bll);
			checkField(user.getEmail(),"email",bll);
			checkField(user.getPhoneNumber(),"numéro de téléphone",bll);
			checkField(user.getStreet(),"rue",bll);
			checkField(user.getPostCode(),"code postal",bll);
			checkField(user.getCity(),"ville",bll);
			checkField(user.getCity(),"mot de passe",bll);
			if (bll.getErreurs().size()>0) {
				throw bll;
			}
		}
	}

	/**
	 * Méthode de check pour l'update d'utilisateur
	 * Vérifie tous les champs de user en utilisant les méthodes:<br/>
	 * 1. UpdateUsernameIsNotUsed()<br/>
	 * 2. UpdateEmailIsNotUsed()<br/>
	 * 3. checkField()
	 * @param userUpdate
	 * @throws BLLException
	 */
	private void checkUpdate(User userUpdate) throws BLLException {
		BLLException bll = new BLLException();
		//vérification si username utilisé par un autre utilisateur
		//et si email utilisé par un autre utilisateur
		updateUsernameIsNotUsed(userUpdate,bll);
		updateEmailIsNotUsed(userUpdate,bll);
		if (bll.getErreurs().size()>0) {
			throw bll;
		}else {//si username et email valide : vérification des champs de saisie
			checkField(userUpdate.getUsername(),"pseudo",bll);
			checkField(userUpdate.getLastName(),"nom",bll);
			checkField(userUpdate.getFirstName(),"prénom",bll);
			checkField(userUpdate.getEmail(),"email",bll);
			checkField(userUpdate.getPhoneNumber(),"numéro de téléphone",bll);
			checkField(userUpdate.getStreet(),"rue",bll);
			checkField(userUpdate.getPostCode(),"code postal",bll);
			checkField(userUpdate.getCity(),"ville",bll);
			checkField(userUpdate.getCity(),"mot de passe",bll);
			if (bll.getErreurs().size()>0) {
				throw bll;
			}
		}	
	}
	
	/**
	 * Vérifie les champs en fonction du type de nom (pseudo,email,tel,etc..).<br/>
	 * Si un problème est trouvé, un message est ajouté à la liste d'erreur qui est remontée à la fin du traitement.<br/>
	 * Ne check que les champs pas la BDD
	 * @param field
	 * @param name
	 * @param bll
	 */
	private void checkField(String field, String name, BLLException bll) {
		if (field.isBlank()) {
			bll.addError("Le champ %s ne peut pas être vide".formatted(name));
		}else {
			if (name.equals("pseudo")) {
				if (!field.matches("[A-Za-z0-9]{3,30}")) {
					bll.addError("Le champ %s doit être constitué uniquement de caractères alphanumériques".formatted(name));
				}
				if(field.length()<3) {
					bll.addError("Le champ %s n'est pas assez long".formatted(name));
				}
			}
			
			if (name.equals("nom")
					|| name.equals("prénom")
					|| name.equals("rue")
					|| name.equals("ville")) {
				
				if (field.length()>30) {
					bll.addError("Le champ %s ne doit pas dépasser 30 charactères".formatted(name));
				}	
			}
			
			if (name.equals("email")) {
				if(!field.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
					bll.addError("Le champ %s ne respect pas le format attendu".formatted(name));	
				}
				if(field.length()>20) {
					bll.addError("Le champ %s ne doit pas dépasser 20 charactères".formatted(name));	
				}
			}
			
			if (name.equals("numéro de téléphone")) {
				if(!field.matches("[0-9]{10}")) {
					bll.addError("Le champ %s ne respect pas le format attendu".formatted(name));	
				}
				if(field.length()>15) {
					bll.addError("Le champ %s ne doit pas dépasser 15 charactères".formatted(name));	
				}
			}
			
			if (name.equals("code postal")) {
				if(!field.matches("^(([0-8][0-9])|(9[0-5])|(2[ab]))[0-9]{3}$")) {
					bll.addError("Le champ %s ne respect pas le format attendu".formatted(name));	
				}
				if(field.length()>10) {
					bll.addError("Le champ %s ne doit pas dépasser 10 charactères".formatted(name));	
				}
			}
			
			if (name.equals("mot de passe")) {
				if(field.length()>255) {
					bll.addError("Le champ %s est trop long".formatted(name));
				}
			}
		}
	}
	
	/**
	 * vérifie si le username est utilisé par un autre utilisateur dans la BDD <br/>
	 * ne prend pas en compte celui de l'utilisateur connecté<br/>
	 * si c'est la cas une erreur est ajoutée à bll
	 * @param userUpdate
	 * @return boolean
	 */
	private void updateUsernameIsNotUsed(User userUpdate, BLLException bll) {
		if(DaoFactory.getUserDao().checkUsernameIsNotUsed(userUpdate.getUsername(),userUpdate.getNoUser())!=null) {
			bll.addError("Le pseudo est déjà utilisé par un autre utilisateur");
		}
	}
	
	/**
	 * vérifie si l'email est utilisé par un autre utilisateur dans la BDD<br/>
	 * ne prend pas en compte celui de l'utilisateur connecté<br/>
	 * si c'est la cas une erreur est ajoutée à bll
	 * @param userUpdate
	 * @return boolean
	 */
	private void updateEmailIsNotUsed(User userUpdate, BLLException bll) {
		if(DaoFactory.getUserDao().checkEmailIsNotUsed(userUpdate.getEmail(),userUpdate.getNoUser())!=null) {
			bll.addError("L'adresse email est déjà utilisée par un autre utilisateur");
		}
	}
	
	/**
	 * vérifie si le username est déjà utilisé dans la BDD<br/>
	 * si c'est la cas une erreur est ajoutée à bll
	 * @param UserName
	 * @param bll
	 */
	private void createUsernameIsNotUsed(String UserName, BLLException bll) {
		if (DaoFactory.getUserDao().selectByUserName(UserName)!=null) {
			bll.addError("Le pseudo est déjà utilisé");
		}
	}
	
	
	/**
	 * vérifie si l'email est déjà utilisé dans la BDD<br/>
	 * si c'est la cas une erreur est ajoutée à bll
	 * @param UserName
	 * @param bll
	 */
	private void createEmailIsNotUsed(String email, BLLException bll) {
		if (DaoFactory.getUserDao().selectByEmail(email)!=null) {
			bll.addError("L'adresse email est déjà utilisée");
		}
	}

	
	
	
	
	
	
}
