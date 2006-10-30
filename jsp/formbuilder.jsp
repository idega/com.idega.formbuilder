<?xml version="1.0"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:fb="http://xmlns.idega.com/com.idega.formbuilder"
	xmlns:wf="http://xmlns.idega.com/com.idega.webface"
	xmlns:ws="http://xmlns.idega.com/com.idega.workspace" version="1.2"
	xmlns:t="http://myfaces.apache.org/tomahawk"
	xmlns:a4j="https://ajax4jsf.dev.java.net/ajax">
	<jsp:directive.page contentType="text/html" />
	<f:view>
		<ws:page id="formbuilder" javascripturls="/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/ajax.js,
												/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/modal-message.js,
												/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/ajax-dynamic-content.js,
												/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/drag-drop-custom.js,
												/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/upload.js,
												/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/prototype.js,
												/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/scriptaculous.js,
												/dwr/interface/actionmanager.js,
												/dwr/interface/dwrmanager.js,
												/dwr/engine.js">
			<h:form id="workspaceform1">
				<t:div styleClass="application_container" id="application_container" forceId="true">
					<t:div styleClass="toolbar_container" id="toolbar_container" forceId="true">
						<h:commandLink id="new_form_button" styleClass="toolbar_button float_left" onclick="displayMessage('/idegaweb/bundles/com.idega.formbuilder.bundle/resources/includes/new-dialog.inc');return false" value="#{localizedStrings['com.idega.formbuilder']['toolbar_new']}" />
						<h:commandLink id="delete_form_button" styleClass="toolbar_button float_left" onclick="displayMessage('/idegaweb/bundles/com.idega.formbuilder.bundle/resources/includes/confirm-delete.inc');return false" action="" value="Delete form"/>
						<h:commandLink id="import_form_button" styleClass="toolbar_button float_left" onclick="displayMessage('/idegaweb/bundles/com.idega.formbuilder.bundle/resources/includes/upload-dialog.inc');return false" action="" value="Import form"/>
						<h:commandLink id="export_form_button" styleClass="toolbar_button float_left" action="" value="Export form" />
						<h:selectOneMenu id="formList" styleClass="toolbar_field float_left" value="#{viewmanager.currentFormName}">
							<f:selectItems value="#{formList.forms}" />
							<a4j:support event="onchange" onsubmit="switchSelectedForm()" oncomplete="formSwitched()" action="#{viewmanager.changeSelectedForm}" ajaxSingle="true" reRender="workspaceform1:tab01,form_container" />
						</h:selectOneMenu>
						<h:commandLink id="help_form_button" onclick="showInnerHtml(this)" styleClass="toolbar_button float_right" action="" value="Help"/>
						<h:selectOneMenu id="localeList" styleClass="toolbar_field float_right" value="#{viewmanager.currentLocale}">
							<f:selectItems value="#{localeList.locales}" />
							<a4j:support event="onchange" onsubmit="switchSelectedForm()" oncomplete="formSwitched()" action="#{viewmanager.changeSelectedForm}" ajaxSingle="true" reRender="workspaceform1:tab01,form_container" />
						</h:selectOneMenu>
					</t:div>
					<t:div styleClass="main_container" id="main_container" forceId="true">
						<t:div styleClass="options_container" id="options_container" forceId="true">
							<t:panelTabbedPane id="options_tabbed_pane"
								selectedIndex="1"
								serverSideTabSwitch="false"
						  		styleClass="tabbedPane"
		                        activeTabStyleClass="activeTab"
		                        inactiveTabStyleClass="inactiveTab"
		                        disabledTabStyleClass="disabledTab"
		                        activeSubStyleClass="activeSub"
		                        inactiveSubStyleClass="inactiveSub"
		                        tabContentStyleClass="tabContent">
		                        <f:verbatim>
							    	<script type="text/javascript" src="/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/palette.js" />
							    </f:verbatim>
							    <t:panelTab id="tab01" label="Form properties">
								    <t:outputLabel for="formTitle" value="Form title" />
								    <t:htmlTag value="br" />
								    <t:inputText id="formTitleInput" forceId="true" value="#{viewmanager.formTitle}">
								    	<a4j:support event="onkeyup" ajaxSingle="true" requestDelay="500" reRender="formHeadingHeader" />
								    </t:inputText>
								    <t:htmlTag value="br" />
								    <t:outputLabel for="formDescription" value="Description" />
								    <t:htmlTag value="br" />
								    <t:inputTextarea id="formDescription" forceId="true" value="#{viewmanager.formDescription}">
								    	<a4j:support event="onkeyup" ajaxSingle="true" requestDelay="500" reRender="formHeadingBody" />
								    </t:inputTextarea>
							    </t:panelTab>
							    <t:panelTab id="tab02" label="Add new field">
									<fb:palette id="firstlist" 
												styleClass="componentsList" 
												itemStyleClass="paletteComponent" 
												items="#{palette.components}" 
												columns="2" />
							    </t:panelTab>
							    <t:panelTab id="tab03" label="Field properties">
							        <t:outputText value="something else" />
							    </t:panelTab>
							</t:panelTabbedPane>
						</t:div>
						<fb:workspace styleClass="form_container" id="form_container" view="design">
							<f:facet name="design">
								<fb:formDesignView id="dropBox" styleClass="dropBox" componentStyleClass="formElement">
								<f:facet name="noFormNoticeFacet">
									<t:div id="noFormNotice" styleClass="visibleFacet" forceId="true">
										<t:outputText id="noFormNoticeHeader" forceId="true" value="#{localizedStrings['com.idega.formbuilder']['labels_noform_header']}" />
										<t:htmlTag value="br" />
										<t:outputText id="noFormNoticeBody" forceId="true" value="#{localizedStrings['com.idega.formbuilder']['labels_noform_body']}" />
									</t:div>
								</f:facet>
								<f:facet name="formHeaderFacet">
									<t:div id="formHeading" styleClass="invisibleFacet" forceId="true">
										<t:outputText id="formHeadingHeader" forceId="true" onclick="selectFormHeader()" value="#{viewmanager.formTitle}" />
										<t:htmlTag value="br" />
										<t:outputText id="formHeadingBody" forceId="true" value="#{viewmanager.formDescription}" />
										<t:htmlTag value="hr" />
									</t:div>
								</f:facet>
								<f:facet name="emptyFormFacet">
									<t:div id="emptyForm" styleClass="invisibleFacet" forceId="true">
										<t:outputText id="emptyFormHeader" forceId="true" value="#{localizedStrings['com.idega.formbuilder']['labels_noform_header']}" />
										<t:htmlTag value="br" />
										<t:outputText id="emptyFormBody" forceId="true" value="#{localizedStrings['com.idega.formbuilder']['labels_noform_body']}" />
									</t:div>
								</f:facet>
							</fb:formDesignView>
							</f:facet>
						</fb:workspace>
						<!--
						<t:div styleClass="form_container" id="form_container" forceId="true">
							<fb:formDesignView id="dropBox" styleClass="dropBox" componentStyleClass="formElement">
								<f:facet name="noFormNoticeFacet">
									<t:div id="noFormNotice" styleClass="visibleFacet" forceId="true">
										<t:outputText id="noFormNoticeHeader" forceId="true" value="#{localizedStrings['com.idega.formbuilder']['labels_noform_header']}" />
										<t:htmlTag value="br" />
										<t:outputText id="noFormNoticeBody" forceId="true" value="#{localizedStrings['com.idega.formbuilder']['labels_noform_body']}" />
									</t:div>
								</f:facet>
								<f:facet name="formHeaderFacet">
									<t:div id="formHeading" styleClass="invisibleFacet" forceId="true">
										<t:outputText id="formHeadingHeader" forceId="true" onclick="selectFormHeader()" value="#{viewmanager.formTitle}" />
										<t:htmlTag value="br" />
										<t:outputText id="formHeadingBody" forceId="true" value="#{viewmanager.formDescription}" />
										<t:htmlTag value="hr" />
									</t:div>
								</f:facet>
								<f:facet name="emptyFormFacet">
									<t:div id="emptyForm" styleClass="invisibleFacet" forceId="true">
										<t:outputText id="emptyFormHeader" forceId="true" value="#{localizedStrings['com.idega.formbuilder']['labels_noform_header']}" />
										<t:htmlTag value="br" />
										<t:outputText id="emptyFormBody" forceId="true" value="#{localizedStrings['com.idega.formbuilder']['labels_noform_body']}" />
									</t:div>
								</f:facet>
							</fb:formDesignView>
						</t:div>
						-->
						<f:verbatim>
							<script type="text/javascript" src="/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/formbuilder.js" />
							<script type="text/javascript">
							<!--
							/*var messageObj = new DHTML_modalMessage();*/
							-->
							</script>
						</f:verbatim>
					</t:div>
					<t:div id="bottom_tab_container" forceId="true">
						<h:commandLink id="design_view_button" styleClass="bottom_tab_button float_center" action="" value="Design"/>
						<h:commandLink id="preview_view_button" styleClass="bottom_tab_button float_center" action="" value="Preview"/>
						<h:commandLink id="source_view_button" styleClass="bottom_tab_button float_center" action="" value="Source"/>
					</t:div>
				</t:div>
			</h:form>
		</ws:page>
	</f:view>
</jsp:root>