<?xml version="1.0"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:fb="http://xmlns.idega.com/com.idega.formbuilder"
	xmlns:ws="http://xmlns.idega.com/com.idega.workspace" version="1.2"
	xmlns:t="http://myfaces.apache.org/tomahawk">
	<jsp:directive.page contentType="text/html" />
	<f:view>
		<ws:page 	id="formbuilder" 
					showFunctionMenu="false"
					javascripturls="/dwr/interface/FormDocument.js,
									/dwr/interface/Workspace.js,
									/dwr/engine.js,
												
									/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/formshome.js"
					stylesheeturls="/idegaweb/bundles/com.idega.formbuilder.bundle/resources/style/formshome.css">
			<h:form id="workspaceform1" onsubmit="return false;">
				<f:verbatim>
                	<script type="text/javascript">
                		window.addEvent('domready', function() {
							var errorHanlder = function() {
								reloadPage();
							}
							DWREngine.setErrorHandler(errorHanlder);
						});
						window.addEvent('domready', registerFormsHomeActions);
                	</script>
                </f:verbatim>
				<fb:homePage />
				<t:div id="newTaskFormDialog" forceId="true" style="display: none;">
					<t:div styleClass="simplePopupLayer">
						<t:outputLabel styleClass="simplePopupLabel" for="taskFormNameInput" value="Form name" />
						<t:inputText styleClass="simplePopupInput" id="taskFormNameInput" onkeyup="modalFormName = this.value;" onblur="modalFormName = this.value;" forceId="true" />
					</t:div>
					<t:htmlTag value="br" />
					<t:selectBooleanCheckbox onchange="modalGoToDesigner = this.checked;" id="designerCheckbox" forceId="true" value="true" />
					<t:outputLabel for="designerCheckbox" value="Go to designer" />
					<t:htmlTag value="br" />
					<t:commandLink id="createTaskFormBtn" onclick="createNewTaskForm(modalFormName, modalGoToDesigner);" forceId="true" value="Create" />
				</t:div>
				<t:div id="attachTaskFormDialog" forceId="true" style="display: none;">
					<t:outputLabel for="formSelector" value="Choose form" />
					<t:selectOneMenu id="formSelector" onchange="var selFormVal = this.options[this.selectedIndex].getProperty('value');if(selFormVal != ''){modalSelectedForm = selFormVal;}" forceId="true" />
					<t:htmlTag value="br" />
					<t:selectBooleanCheckbox onchange="modalGoToDesigner = this.checked;" id="designerCheckbox" forceId="true" value="true" />
					<t:outputLabel for="designerCheckbox" value="Go to designer" />
					<t:htmlTag value="br" />
					<t:commandLink id="attachTaskFormBtn" onclick="attachTaskForm(modalFormName, modalGoToDesigner);" forceId="true" value="Attach" />
				</t:div>
				<t:div id="newFormDialog" forceId="true" style="display: none;">
					<t:div styleClass="simplePopupLayer">
						<t:outputLabel styleClass="simplePopupLabel" for="formNameInput" value="Form name" />
						<t:inputText styleClass="simplePopupInput" id="formNameInput" onkeydown="if(isEnterEvent(event)) {createNewForm(modalFormName, modalGoToDesigner);}" onkeyup="modalFormName = this.value;" onblur="modalFormName = this.value;" forceId="true" />
					</t:div>
					<t:htmlTag value="br" />
					<t:selectBooleanCheckbox onchange="modalGoToDesigner = this.checked;" id="designerCheckbox2" forceId="true" value="true" />
					<t:outputLabel for="designerCheckbox2" value="Go to designer" />
					<t:htmlTag value="br" />
					<t:commandLink id="createFormBtn" onclick="createNewForm(modalFormName, modalGoToDesigner);" forceId="true" value="Create" />
				</t:div>
				<t:div styleClass="fbBottomButtonsContainer">
					<t:commandLink id="newFormButton" styleClass="rightButton smoothbox" forceId="true" value="#{localizedStrings['com.idega.formbuilder']['toolbar_new']}" />
				</t:div>
			</h:form>
		</ws:page>
	</f:view>
</jsp:root>