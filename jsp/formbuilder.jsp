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
				 				/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/ajax.js,
								/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/modal-message.js,
								/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/ajax-dynamic-content.js,
								/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/categories-tabs.js,
								/dwr/engine.js,
								/dwr/util.js,
								/dwr/interface/FormComponent.js,
								/dwr/interface/FormDocument.js,
								/dwr/interface/FormPage.js,
								/dwr/interface/JbpmBusiness.js,
								/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/formbuilder.js"
				stylesheeturls="/idegaweb/bundles/com.idega.formbuilder.bundle/resources/style/formbuilder.css">
			<h:form id="workspaceform1">
				<f:verbatim>
                	<script type="text/javascript">
                		window.addEvent('domready', function() {
							var errorHanlder = function() {
								reloadPage();
							}
							DWREngine.setErrorHandler(errorHanlder);
						});
                	</script>
                </f:verbatim>
				<t:div id="mainApplication" forceId="true">
					<fb:workspace id="mainWorkspace" view="#{workspace.view}"/>
				</t:div>
				<t:div styleClass="fbBottomButtonsContainer">
					<!-- 
					<f:verbatim>
	                	<a href="/servlet/ObjectInstanciator?idegaweb_instance_class=com.idega.formbuilder.presentation.components.FBSourceView" rel="moodalbox" >values</a>
	                </f:verbatim>
	                 -->
					<t:commandLink id="previewButton" styleClass="leftButton" forceId="true" onclick="saveFormDocument();return false" value="Preview"></t:commandLink>
					<t:commandLink id="sourceCodeButton" styleClass="leftButton" forceId="true" onclick="saveFormDocument();return false" value="Source"></t:commandLink>
					<t:commandLink id="newFormButton" styleClass="rightButton" forceId="true" onclick="displayMessage('/idegaweb/bundles/com.idega.formbuilder.bundle/resources/includes/new-dialog.inc');return false" value="#{localizedStrings['com.idega.formbuilder']['toolbar_new']}"></t:commandLink>
					<t:commandLink id="saveFormButton" styleClass="rightButton" forceId="true" onclick="saveFormDocument();return false" value="#{localizedStrings['com.idega.formbuilder']['toolbar_save']}"></t:commandLink>
					<t:commandLink id="homeButton" styleClass="rightButton" forceId="true" onclick="window.location.href = '/workspace/forms/';return false;" value="#{localizedStrings['com.idega.formbuilder']['toolbar_home']}"></t:commandLink>
				</t:div>
			</h:form>
		</ws:page>
	</f:view>
</jsp:root>