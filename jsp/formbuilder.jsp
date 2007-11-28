<?xml version="1.0"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:fb="http://xmlns.idega.com/com.idega.formbuilder"
	xmlns:ws="http://xmlns.idega.com/com.idega.workspace" version="1.2"
	xmlns:t="http://myfaces.apache.org/tomahawk">
	<jsp:directive.page contentType="text/html" />
	<f:view>
		<ws:page id="formbuilder" 
				 showFunctionMenu="false"
				 javascripturls="/idegaweb/bundles/com.idega.block.web2.0.bundle/resources/javascript/mootools/1.11/mootools-all.js,
				 				/idegaweb/bundles/com.idega.block.web2.0.bundle/resources/javascript/codepress/codepress.js,
								/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/categories-tabs.js,
								/idegaweb/bundles/com.idega.block.web2.0.bundle/resources/javascript/moodalbox/1.2.1/js/moodalbox.js,
								/dwr/engine.js,
								/dwr/util.js,
								/dwr/interface/FormComponent.js,
								/dwr/interface/FormDocument.js,
								/dwr/interface/FormPage.js,
								/dwr/interface/JbpmBusiness.js,
								/dwr/interface/ProcessPalette.js,
								/dwr/interface/ProcessData.js,
								/dwr/interface/Workspace.js,
								/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/formbuilder.js"
				stylesheeturls="/idegaweb/bundles/com.idega.formbuilder.bundle/resources/style/formbuilder.css,
								/idegaweb/bundles/com.idega.block.web2.0.bundle/resources/javascript/moodalbox/1.2.1/css/moodalbox.css">
			<h:form id="workspaceform1" onsubmit="return false;">
				<f:verbatim>
                	<script type="text/javascript">
                		window.addEvent('domready', function() {
							var errorHanlder = function(errorString, exception) {
								reloadPage();
							}
							DWREngine.setErrorHandler(errorHanlder);
						});
						window.addEvent('domready', initializeInlineEdits);
						window.addEvent('domready', initMenu);
						window.addEvent('domready', setupPagesPanel);
						window.addEvent('domready', setupDesignView);
						window.addEvent('domready', registerFormbuilderActions);
						window.addEvent('domready', setupVariableViewer);
                	</script>
                </f:verbatim>
				<t:div id="mainApplication" forceId="true">
					<fb:workspace id="mainWorkspace"/>
				</t:div>
				<t:div id="newFormDialog" forceId="true" styleClass="newFormDialogStyle" style="display: none;">
					<t:htmlTag styleClass="accordionHeading" value="span">
						<t:outputText value="New form" styleClass="title"> </t:outputText>
					</t:htmlTag>
					<t:inputText id="newFormDialogInput" forceId="true"></t:inputText>
					<t:commandLink id="createFormBtn" forceId="true"  value="Create"></t:commandLink>
				</t:div>
				<t:div id="formListDialog" forceId="true" styleClass="formListDialogStyle" style="display: none;">
					<t:htmlTag styleClass="accordionHeading" value="span">
						<t:outputText value="Save form" styleClass="title"> </t:outputText>
					</t:htmlTag>
					<t:commandLink id="yesSaveFormBtn" forceId="true"  value="Yes"></t:commandLink>
					<t:commandLink id="noSaveFormBtn" forceId="true"  value="No"></t:commandLink>
				</t:div>
				<t:div id="selectVariableDialog" forceId="true" styleClass="selectVariableDialogStyle" style="visibility: hidden;">
					<t:htmlTag styleClass="accordionHeading" value="span">
						<t:outputText value="Select variable" styleClass="title"> </t:outputText>
					</t:htmlTag>
					<t:htmlTag styleClass="variablePopupList" value="ul" id="variablePopupList">
					</t:htmlTag>
					<t:commandLink id="noVariableBtn" forceId="true"  value="None"></t:commandLink>
					<t:commandLink id="cancelVariableBtn" forceId="true"  value="Cancel"></t:commandLink>
				</t:div>
				<t:div styleClass="fbBottomButtonsContainer">
					<t:commandLink id="designButton" styleClass="leftButton" forceId="true" onclick="switchView('design', this.id);return false" value="Design"></t:commandLink>
					<t:commandLink id="previewButton" styleClass="leftButton" forceId="true" onclick="switchView('preview', this.id);return false" value="Preview"></t:commandLink>
					<t:commandLink id="sourceCodeButton" styleClass="leftButton" forceId="true" onclick="switchView('source', this.id);return false" value="Source"></t:commandLink>
					<t:commandLink id="newFormButton" styleClass="rightButton" forceId="true" value="#{localizedStrings['com.idega.formbuilder']['toolbar_new']}"></t:commandLink>
					<t:commandLink id="saveFormButton" styleClass="rightButton" forceId="true" onclick="fbsave();return false" value="#{localizedStrings['com.idega.formbuilder']['toolbar_save']}"></t:commandLink>
					<t:commandLink id="homeButton" styleClass="rightButton" forceId="true" value="#{localizedStrings['com.idega.formbuilder']['toolbar_home']}"></t:commandLink>
				</t:div>
			</h:form>
		</ws:page>
	</f:view>
</jsp:root>