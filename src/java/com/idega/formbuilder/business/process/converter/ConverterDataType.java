package com.idega.formbuilder.business.process.converter;


/**
 * 
 *  Last modified: $Date: 2007/09/21 11:30:29 $ by $Author: civilis $
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 */
public enum ConverterDataType {
	
	DATE {
		public DataConverter getConverter() { 
			return new DateConverter();
		}
	},	
	STRING {
		public DataConverter getConverter() { 
			return new StringConverter();
		}
	},	
	LIST {
		public DataConverter getConverter() { 
			return new CollectionConverter();
		}
	};
	
	public abstract DataConverter getConverter();
}