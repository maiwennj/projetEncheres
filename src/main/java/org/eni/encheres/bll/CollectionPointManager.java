package org.eni.encheres.bll;

import java.time.LocalDateTime;

import org.eni.encheres.bll.exception.BLLException;
import org.eni.encheres.bo.CollectionPoint;
import org.eni.encheres.dal.DaoFactory;

import lombok.Getter;

public class CollectionPointManager {
	
	//singleton getInstance();
		@Getter public static CollectionPointManager instance = new CollectionPointManager(); //lazy singleton
		private CollectionPointManager() {}
		
		public void addCollectionPoint(CollectionPoint collectionPoint) throws BLLException {
			System.out.println(collectionPoint);
			checkFields(collectionPoint);
			DaoFactory.getCollectionPoint().insertCollectionPoint(collectionPoint);
		}

		private void checkFields(CollectionPoint collectionPoint) throws BLLException {
			BLLException bll = new BLLException();
			if (collectionPoint.getCityCP().isBlank() || collectionPoint.getCityCP().isEmpty()) {
				bll.addError("Le champ \"ville\" est obligatoire.");
			}
			if (collectionPoint.getCityCP().length()>30) {
				bll.addError("Le champ \"ville\" ne peut pas contenir plus de 30 caractères.");
			}
			if (collectionPoint.getPostCodeCP().isBlank() || collectionPoint.getPostCodeCP().isEmpty()) {
				bll.addError("Le champ \"code postal\" est obligatoire.");
			}
			if (collectionPoint.getPostCodeCP().length()>15) {
				bll.addError("Le champ \"code postal\" ne peut pas contenir plus de 15 caractères.");
			}
			if (collectionPoint.getStreetCP().isBlank() || collectionPoint.getStreetCP().isEmpty()) {
				bll.addError("Le champ \"rue\" est obligatoire.");
			}
			if (collectionPoint.getStreetCP().length()>30) {
				bll.addError("Le champ \"rue\" ne peut pas contenir plus de 30 caractères.");
			}
			
			if (bll.getErreurs().size()>0) {
				throw bll;
			}
			
		}
		
		
}
