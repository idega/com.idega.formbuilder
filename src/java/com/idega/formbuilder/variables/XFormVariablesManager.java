package com.idega.formbuilder.variables;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.chiba.ChibaUtils;
import com.idega.core.business.DefaultSpringBean;
import com.idega.data.SimpleQuerier;
import com.idega.jbpm.variables.VariablesManager;
import com.idega.util.ArrayUtil;
import com.idega.util.CoreConstants;
import com.idega.util.ListUtil;
import com.idega.util.StringUtil;
import com.idega.util.datastructures.map.MapUtil;

@Service
@Scope("request")
public class XFormVariablesManager extends DefaultSpringBean implements VariablesManager {

	public void doManageVariables(Long processInstanceId, Long taskInstanceId, Map<String, Object> variables) {
		List<String> currentSessions = ChibaUtils.getInstance().getKeysOfXFormSessions(ChibaUtils.getInstance().getCurrentHttpSessionId());
		if (ListUtil.isEmpty(currentSessions))
			return;
		
		String query = "update jbpm_variableinstance set stringvalue_ = null, LONGVALUE_ = null, DOUBLEVALUE_ = null, DATEVALUE_ = null, BYTEARRAYVALUE_ = null " +
						" where processinstance_ = " + processInstanceId + " and taskinstance_ ";
		String updateTask = query + "= " + taskInstanceId +	" and name_ = '";
		String updateToken = query + "is null and TOKENVARIABLEMAP_ is not null and name_ = '";
		for (String sessionKey: currentSessions) {
			Map<String, String> emptyValues = ChibaUtils.getInstance().getEmptyXFormValues(sessionKey);
			if (MapUtil.isEmpty(emptyValues))
				continue;
			
			for (String elementId: emptyValues.keySet()) {
				String variableName = ChibaUtils.getInstance().getVariableNameByXFormElementId(sessionKey, elementId);
				if (StringUtil.isEmpty(variableName)) {
					getLogger().warning("Variable name is unknown for XForm element " + elementId + " and session " + sessionKey);
					continue;
				}
				
				if (!hasTokenValue(variableName, processInstanceId))
					continue;
				
				if (doUpdate(updateTask.concat(variableName).concat(CoreConstants.QOUTE_SINGLE_MARK)))
					doUpdate(updateToken.concat(variableName).concat(CoreConstants.QOUTE_SINGLE_MARK));
			}
		}
	}
	
	private boolean doUpdate(String updateSQL) {
		try {
			SimpleQuerier.executeUpdate(updateSQL, false);
			return true;
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error while executing SQL update: " + updateSQL, e);
		}
		return false;
	}
	
	private boolean hasTokenValue(String name, Long piId) {
		List<Serializable[]> results = null;
		String query = "select stringvalue_ from jbpm_variableinstance where processinstance_ = " + piId + " and name_ = '" + name +
				"' and TOKENVARIABLEMAP_ is not null and taskinstance_ is null";
		try {
			results = SimpleQuerier.executeQuery(query, 1);
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error executing query " +  query, e);
		}
		if (ListUtil.isEmpty(results))
			return false;
		Serializable[] values = results.get(0);
		if (ArrayUtil.isEmpty(values))
			return false;
		Serializable value = values[0];
		if (!(value instanceof String))
			return false;
		String stringValue = (String) value;
		if (StringUtil.isEmpty(stringValue))
			return false;
		return true;
	}

}