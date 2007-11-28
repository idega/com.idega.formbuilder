<?xml version="1.0"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:fb="http://xmlns.idega.com/com.idega.formbuilder"
	xmlns:ws="http://xmlns.idega.com/com.idega.workspace" version="1.2"
	xmlns:t="http://myfaces.apache.org/tomahawk">
	<jsp:directive.page contentType="text/html" />
	<f:view>
		<ws:page id="formbuilder" showFunctionMenu="false"
					javascripturls="/idegaweb/bundles/com.idega.block.web2.0.bundle/resources/javascript/mootools/1.11/mootools-all.js,
									/idegaweb/bundles/com.idega.block.web2.0.bundle/resources/javascript/transcorners/Transcorners.js,
									/idegaweb/bundles/com.idega.block.web2.0.bundle/resources/javascript/moodalbox/1.2.1/js/moodalbox.js,
												
									/dwr/interface/FormDocument.js,
									/dwr/engine.js,
												
									/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/formshome.js"
					stylesheeturls="/idegaweb/bundles/com.idega.formbuilder.bundle/resources/style/formshome.css,
									/idegaweb/bundles/com.idega.block.web2.0.bundle/resources/javascript/moodalbox/1.2.1/css/moodalbox.css">
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
				<t:div id="newFormDialog" forceId="true" styleClass="newFormDialogStyle" style="display: none;">
					<t:htmlTag styleClass="accordionHeading" value="span">
						<t:outputText value="New form" styleClass="title"> </t:outputText>
					</t:htmlTag>
					<t:inputText id="newFormDialogInput" forceId="true"></t:inputText>
					<t:commandLink id="createFormBtn" forceId="true"  value="Create"></t:commandLink>
				</t:div>
				<t:div styleClass="fbBottomButtonsContainer">
					<t:commandLink id="newFormButton" styleClass="rightButton" forceId="true" value="#{localizedStrings['com.idega.formbuilder']['toolbar_new']}"></t:commandLink>
				</t:div>
			</h:form>
		</ws:page>
	</f:view>
</jsp:root>