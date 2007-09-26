package com.idega.formbuilder.business.egov;

import javax.ejb.FinderException;

/**
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 *
 */
public interface ApplicationBusiness {

	public Application getApplication(Object primaryKey) throws FinderException;
}