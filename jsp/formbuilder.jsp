<?xml version="1.0"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:fb="http://xmlns.idega.com/com.idega.formbuilder"
	xmlns:ws="http://xmlns.idega.com/com.idega.workspace" version="1.2"
	xmlns:t="http://myfaces.apache.org/tomahawk"
	xmlns:core="http://xmlns.idega.com/com.idega.core">
	<jsp:directive.page contentType="text/html" />
	<f:view>
		<ws:page id="formbuilder" javascripturls="#{workspace.javaScriptSources}" stylesheeturls="#{workspace.styleSheetSources}">
			<h:form id="workspaceform1" onsubmit="return false;">
				<f:verbatim>
                	<script type="text/javascript">
                		window.addEvent('domready', function() {
							var errorHanlder = function(errorString, exception) {
								reloadPage();
							}
							DWREngine.setErrorHandler(errorHanlder);
						});
						window.addEvent('domready', initializeAccordions);
						window.addEvent('domready', initializePalette);
						window.addEvent('domready', initializePagesPanel);
						window.addEvent('domready', initializeDesign);
						window.addEvent('domready', registerFormbuilderActions);
						window.addEvent('domready', initializeVariables);
						window.addEvent('domready', initializeBottomToolbar);
						window.addEvent('resize', controlFormbuilderAppWindow);
                	</script>
                </f:verbatim>
				<t:div forceId="true" id="mainApplication">
					<div>
						<fb:workspace id="mainWorkspace"/>
					</div>
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
					<t:commandLink id="yesSaveFormBtn" forceId="true" onclick="fbsave();window.location.href = '/workspace/forms/list/';" value="Yes" />
					<t:commandLink id="noSaveFormBtn" forceId="true" onclick="window.location.href = '/workspace/forms/list/';" value="No" />
				</t:div>
				<t:div id="bottomButtonsContainer" forceId="true" styleClass="fbBottomButtonsContainer">
					<t:div styleClass="states">
						<t:commandLink id="previewButton" styleClass="leftButton viewSwitchBtn" forceId="true">
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
					<core:DownloadLink id="exportAndDownloadFormSourceCode" styleClass="rightButton" downloadWriter="#{workspace.formSourceDownloaderClassName}" value="#{localizedStrings['com.idega.formbuilder']['toolbar_export_and_download_form']}">
						<f:param id="resourceToExportId" value="#{formDocument.formId}"/>
					</core:DownloadLink>
					<t:commandLink id="saveFormButton" styleClass="rightButton" forceId="true" value="#{localizedStrings['com.idega.formbuilder']['toolbar_save']}"></t:commandLink>
					<t:commandLink id="homeButton" styleClass="rightButton smoothbox" forceId="true" value="#{localizedStrings['com.idega.formbuilder']['toolbar_home']}"></t:commandLink>
				</t:div>
			</h:form>
		</ws:page>
	</f:view>
</jsp:root>