<?xml version="1.0"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:wf="http://xmlns.idega.com/com.idega.webface"
	xmlns:fb="http://xmlns.idega.com/com.idega.formbuilder"
	xmlns:ws="http://xmlns.idega.com/com.idega.workspace" version="1.2"
	xmlns:t="http://myfaces.apache.org/tomahawk">
	<jsp:directive.page contentType="text/html" />
	<f:view>
		<ws:page id="formbuilder" javascripturls="/idega/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/ajax.js,
												/idega/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/modal-message.js,
												/idega/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/ajax-dynamic-content.js,
												/idega/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/drag-drop-custom.js,
												/idega/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/upload.js">
			<h:form id="workspaceform1">
				<t:div styleClass="application_container" id="application_container" forceId="true">
					<t:div styleClass="toolbar_container" id="toolbar_container" forceId="true">
						<h:commandLink id="new_form_button" styleClass="toolbar_button float_left" action="#{viewmanager.newForm}" value="#{localizedStrings['com.idega.formbuilder']['toolbar_new']}"/>
						<h:commandLink id="save_form_button" styleClass="toolbar_button float_left" action="#{viewmanager.doStuff}" value="Save form" />
						<h:commandLink id="delete_form_button" styleClass="toolbar_button float_left" onclick="displayMessage('/idega/idegaweb/bundles/com.idega.formbuilder.bundle/resources/includes/confirm-delete.inc');return false" action="" value="Delete form"/>
						<h:commandLink id="import_form_button" styleClass="toolbar_button float_left" onclick="displayMessage('/idega/idegaweb/bundles/com.idega.formbuilder.bundle/resources/includes/upload-dialog.inc');return false" action="" value="Import form"/>
						<h:commandLink id="export_form_button" styleClass="toolbar_button float_left" action="" value="Export form" />
						<t:div styleClass="toolbar_field float_left">
							<h:outputText value="Switch to:" />
							<h:selectOneMenu />
						</t:div>
						<h:commandLink id="help_form_button" styleClass="toolbar_button float_right" action="" value="Help"/>
					</t:div>
					<t:div styleClass="main_container" id="main_container" forceId="true">
						<t:div styleClass="options_container" id="options_container" forceId="true">
							<t:panelTabbedPane id="options_tabbed_pane"
								selectedIndex="1"
								serverSideTabSwitch="true"
						  		styleClass="tabbedPane"
		                        activeTabStyleClass="activeTab"
		                        inactiveTabStyleClass="inactiveTab"
		                        disabledTabStyleClass="disabledTab"
		                        activeSubStyleClass="activeSub"
		                        inactiveSubStyleClass="inactiveSub"
		                        tabContentStyleClass="tabContent">
		                        <t:tabChangeListener type="com.idega.formbuilder.presentation.UIManager" />
							    <t:panelTab id="tab01" label="Form properties">
							    	<t:outputText value="something" />
							    </t:panelTab>
							    <t:panelTab id="tab02" label="Add new field">
							        <t:dataList id="firstlist"
										styleClass="components_list"
										itemStyleClass=""
										var="field"
										value="#{viewmanager.fields}"
										layout="unorderedList">
										<t:div styleClass="palette_component" id="field" forceId="true" forceIdIndex="true">
											<h:outputLabel value="#{field.name}" rendered="true" />
											<h:outputText value="#{field.type}" style="display: none;"/>
										</t:div>
									</t:dataList>
							    </t:panelTab>
							    <t:panelTab id="tab03" label="Field properties">
							        <t:outputText value="something else" />
							    </t:panelTab>
							</t:panelTabbedPane>
						</t:div>
						<t:div styleClass="form_container" id="form_container" forceId="true">
							<t:div id="dropBox" forceId="true" styleClass="dropBox" binding="#{viewmanager.formView}">
								<t:div styleClass="form_element form_heading">
									<t:outputText id="form_heading_title" forceId="true" value="Untitled form" />
									<t:outputText id="form_heading_description" forceId="true" value="Some description" />
								</t:div>
							</t:div>
							<f:verbatim>
								<script type="text/javascript" src="/idega/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/formbuilder.js" />
							</f:verbatim>
						</t:div>
					</t:div>
					<t:div id="bottom_tab_container" forceId="true">
						<h:commandLink id="design_view_button" styleClass="bottom_tab_button float_center" action="" value="Design"/>
						<h:commandLink id="preview_view_button" styleClass="bottom_tab_button float_center" action="" value="Preview"/>
						<h:commandLink id="source_view_button" styleClass="bottom_tab_button float_center" action="" value="Source"/>
					</t:div>
					<t:div id="hidden_container" forceId="true">
						<t:commandLink id="add_field_button" forceId="true" action="#{viewmanager.addFormField}" value="" />
						<t:commandLink id="select_field_button" forceId="true" action="#{viewmanager.selectFormField}" value="" />
						<t:inputHidden id="selected_field_type" forceId="true" value="#{viewmanager.selectedFieldTypeValue}" />
						<t:inputHidden id="selected_field_id" forceId="true" binding="#{viewmanager.selectedFieldId}" />
					</t:div>
				</t:div>
			</h:form>
		</ws:page>
	</f:view>
</jsp:root>
