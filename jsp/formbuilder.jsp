<?xml version="1.0"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:wf="http://xmlns.idega.com/com.idega.webface"
	xmlns:fb="http://xmlns.idega.com/com.idega.formbuilder"
	xmlns:ws="http://xmlns.idega.com/com.idega.workspace" version="1.2"
	xmlns:t="http://myfaces.apache.org/tomahawk"
	xmlns:a4j="https://ajax4jsf.dev.java.net/ajax">
	<jsp:directive.page contentType="text/html" />
	<f:view>
		<ws:page id="formbuilder" javascripturls="/idega/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/ajax.js,
												/idega/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/modal-message.js,
												/idega/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/ajax-dynamic-content.js,
												/idega/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/drag-drop-custom.js">
			<h:form id="workspaceform1">
				<wf:container styleClass="application_container">
					<wf:container styleClass="toolbar_container">
						<h:commandLink id="new_form_button" styleClass="toolbar_button" action="" value="New form"/>
						<h:commandLink id="save_form_button" styleClass="toolbar_button" action="" value="Save form"/>
						<h:commandLink id="delete_form_button" styleClass="toolbar_button" action="" value="Delete form"/>
						<h:commandLink id="import_form_button" styleClass="toolbar_button" action="" value="Import form"/>
						<h:commandLink id="export_form_button" styleClass="toolbar_button" action="" value="Export form"/>
					</wf:container>
					<wf:container styleClass="main_container">
						  	<wf:container styleClass="options_container" id="options">
						  		<t:panelTabbedPane id="options_tabbed_pane"
						  			styleClass="tabbedPane"
		                           	activeTabStyleClass="activeTab"
		                           	inactiveTabStyleClass="inactiveTab"
		                           	disabledTabStyleClass="disabledTab"
		                           	activeSubStyleClass="activeSub"
		                           	inactiveSubStyleClass="inactiveSub"
		                           	tabContentStyleClass="tabContent">
							        <t:panelTab id="tab01" label="Form properties">
							        	<t:outputText value="something" />
							        </t:panelTab>
							        <t:panelTab id="tab02" label="Add new field">
							          	<t:dataList forceId="true"
											id="firstlist"
											styleClass="components_list"
											itemStyleClass=""
											var="field"
											value="#{palette.fields}"
											layout="unorderedList">
											<t:div styleClass="palette_component" id="field" forceId="true" forceIdIndex="true">
												<h:outputLabel value="#{field.name}" />
											</t:div>
										</t:dataList>
							        </t:panelTab>
							        <t:panelTab id="tab03" label="Field properties">
							          	<t:outputText value="something else" />
							        </t:panelTab>
							    </t:panelTabbedPane>
							</wf:container>
							<wf:container styleClass="form_container">
								<t:div id="dropBox" forceId="true" styleClass="dropBox">
									<t:div styleClass="form_component"><h:outputText value="This is your form" /></t:div>
								</t:div>
								<f:verbatim>
								    	<script type="text/javascript" src="/idega/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/formbuilder.js" />
								    </f:verbatim>
							</wf:container>
					</wf:container>
				</wf:container>
			</h:form>
		</ws:page>
	</f:view>
</jsp:root>
