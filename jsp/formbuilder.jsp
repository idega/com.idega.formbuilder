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
												/idega/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/tab-view.js,
												/idega/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/modal-message.js,
												/idega/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/ajax-dynamic-content.js,
												/idega/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/drag-drop-custom.js">
			<h:form id="workspaceform1">
				<wf:container styleClass="application_container">
					<wf:container styleClass="toolbar_container">
						<t:div id="dhtmlgoodies_menu" forceId="true">
							<f:verbatim>
								<ul>
									<li><a href="#" >New form</a></li>
									<li><a href="#" >Save form</a></li>
									<li><a href="#" onclick="displayMessage('/idega/idegaweb/bundles/com.idega.formbuilder.bundle/resources/includes/confirm-delete.inc');return false">Delete form</a></li>
									<li><a href="#">Import</a></li>
									<li><a href="#">Export</a></li>
									<li><a href="#">Switch form</a></li>
								</ul>
							</f:verbatim>
						</t:div>
						<h:selectOneMenu />
					</wf:container>
					<wf:container styleClass="main_container">
						  	<wf:container styleClass="options_container" id="options">
						  		<t:panelTabbedPane styleClass="tabbedPane"
		                           activeTabStyleClass="activeTab"
		                           inactiveTabStyleClass="inactiveTab"
		                           disabledTabStyleClass="disabledTab"
		                           activeSubStyleClass="activeSub"
		                           inactiveSubStyleClass="inactiveSub"
		                           tabContentStyleClass="tabContent">
							        <t:panelTab id="tab01" label="Form properties">
							          <t:outputText value="something"></t:outputText>
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
							          <t:outputText value="something else"></t:outputText>
							        </t:panelTab>
							    </t:panelTabbedPane>
						  		<!--
								<t:div id="dhtmlgoodies_tabView1" forceId="true">
									<t:div styleClass="dhtmlgoodies_aTab">
										  <h:outputText value="Content of tab 1" />
									</t:div>
									<t:div styleClass="dhtmlgoodies_aTab" id="component_palette" forceId="true">
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
									</t:div>
									<t:div styleClass="dhtmlgoodies_aTab">
										   <h:outputText value="Content of tab 3" />
									</t:div>
								</t:div>
								-->
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
