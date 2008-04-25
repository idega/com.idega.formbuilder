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
				 				/idegaweb/bundles/com.idega.block.web2.0.bundle/resources/javascript/smoothbox/smoothbox.js,
				 				/idegaweb/bundles/com.idega.block.web2.0.bundle/resources/javascript/moodalbox/1.2.1/js/moodalbox_full.js,
				 				/idegaweb/bundles/com.idega.block.web2.0.bundle/resources/javascript/inlineEdit/1.1/inlineEdit.js,
				 				/idegaweb/bundles/com.idega.block.web2.0.bundle/resources/javascript/mootabs/1.2/mootabs.js,
				 				
				 				/idegaweb/bundles/org.chiba.web.bundle/resources/javascript/PresentationContext.js,
				 				
				 				/dwr/engine.js,
				 				/dwr/util.js,
								/dwr/interface/FormComponent.js,
								/dwr/interface/PropertyManager.js,
								/dwr/interface/FormDocument.js,
								/dwr/interface/FormPage.js,
								/dwr/interface/JbpmBusiness.js,
								/dwr/interface/ProcessPalette.js,
								/dwr/interface/ProcessData.js,
								/dwr/interface/Workspace.js,
								/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/formbuilder.js"
								
				stylesheeturls="/idegaweb/bundles/com.idega.formbuilder.bundle/resources/style/formbuilder.css,
								/idegaweb/bundles/com.idega.block.web2.0.bundle/resources/javascript/smoothbox/smoothbox.css,
								/idegaweb/bundles/com.idega.block.web2.0.bundle/resources/javascript/moodalbox/1.2.1/css/moodalbox.css,
								/idegaweb/bundles/com.idega.block.web2.0.bundle/resources/javascript/mootabs/1.2/mootabs.css">
			<h:form id="workspaceform1" onsubmit="return false;">
				<f:verbatim>
                	<script type="text/javascript">
                		window.addEvent('domready', function() {
							var errorHanlder = function(errorString, exception) {
								//reloadPage();
								alert(errorString +  ' ' +  exception);
							}
							DWREngine.setErrorHandler(errorHanlder);
						});
						window.addEvent('domready', initializeAccordions);
						window.addEvent('domready', initializePalette);
						window.addEvent('domready', initializePagesPanel);
						window.addEvent('domready', initializeDesign);
						window.addEvent('domready', registerFormbuilderActions);
						window.addEvent('domready', initializeVariableViewer);
						window.addEvent('domready', initializeBottomToolbar);
						window.addEvent('resize', controlFormbuilderAppWindow);
                	</script>
                </f:verbatim>
				<t:div id="mainApplication" forceId="true">
					<fb:workspace id="mainWorkspace"/>
				</t:div>
				<t:div id="newFormDialog" forceId="true" style="display: none;">
					<t:div styleClass="simplePopupLayer">
						<t:outputLabel styleClass="simplePopupLabel" for="formNameInput" value="Form name" />
						<t:inputText styleClass="simplePopupInput" id="formNameInput" onkeydown="if(isEnterEvent(event)) {createNewForm(modalFormName, modalGoToDesigner);}" onkeyup="modalFormName = this.value;" onblur="modalFormName = this.value;" forceId="true" />
					</t:div>
					<t:htmlTag value="br" />
					<t:commandLink id="createFormBtn" onclick="createNewForm(modalFormName, modalGoToDesigner);" forceId="true" value="Create" />
				</t:div>
				<t:div id="formListDialog" forceId="true" styleClass="formListDialogStyle" style="display: none;">
					<t:commandLink id="yesSaveFormBtn" forceId="true" onclick="fbsave();window.location.href = '/workspace/forms/';" value="Yes" />
					<t:commandLink id="noSaveFormBtn" forceId="true" onclick="window.location.href = '/workspace/forms/';" value="No" />
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
				<t:div id="bottomButtonsContainer" forceId="true" styleClass="fbBottomButtonsContainer">
					<t:div styleClass="states">
						<t:commandLink id="previewButton" styleClass="leftButton viewSwitchBtn activeViewButton" forceId="true">
							<t:htmlTag styleClass="outerSpan" value="span">
								<t:outputText value="Preview" styleClass="innerSpan"/>
							</t:htmlTag>
						</t:commandLink>
						<t:commandLink id="sourceCodeButton" styleClass="leftButton viewSwitchBtn" forceId="true">
							<t:htmlTag styleClass="outerSpan" value="span">
								<t:outputText value="Source" styleClass="innerSpan"/>
							</t:htmlTag>
						</t:commandLink>
						<t:commandLink id="designButton" styleClass="leftButton viewSwitchBtn" forceId="true">
							<t:htmlTag styleClass="outerSpan" value="span">
								<t:outputText value="Design" styleClass="innerSpan"/>
							</t:htmlTag>
						</t:commandLink>
					</t:div>
					<t:commandLink id="newFormButton" styleClass="rightButton smoothbox" forceId="true" value="#{localizedStrings['com.idega.formbuilder']['toolbar_new']}"></t:commandLink>
					<t:commandLink id="saveFormButton" styleClass="rightButton" forceId="true" value="#{localizedStrings['com.idega.formbuilder']['toolbar_save']}"></t:commandLink>
					<t:commandLink id="homeButton" styleClass="rightButton smoothbox" forceId="true" value="#{localizedStrings['com.idega.formbuilder']['toolbar_home']}"></t:commandLink>
				</t:div>
			</h:form>
		</ws:page>
	</f:view>
</jsp:root>