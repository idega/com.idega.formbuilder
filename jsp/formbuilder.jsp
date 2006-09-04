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
												/idega/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/tab-view.js,
												/idega/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/modal-message.js,
												/idega/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/ajax-dynamic-content.js,
												/idega/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/prototype.js,
												/idega/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/scriptaculous.js">
			<h:form id="workspaceform1">
				<wf:container styleClass="application_container">
					<wf:container styleClass="toolbar_container">
						<f:verbatim>
							<div id="dhtmlgoodies_menu">
								<ul>
									<li><a href="#">New form</a></li>
									<li><a href="#">Save form</a></li>
									<li><a href="#" onclick="displayMessage('/idega/idegaweb/bundles/com.idega.formbuilder.bundle/resources/includes/confirm-delete.inc');return false">Delete form</a></li>
									<li><a href="#">Import</a></li>
									<li><a href="#">Export</a></li>
									<li><a href="#">Switch form</a></li>
								</ul>
							</div>
							<script type="text/javascript">
								messageObj = new DHTML_modalMessage();
								messageObj.setShadowOffset(5);

								function displayMessage(url) {
									messageObj.setSource(url);
									messageObj.setCssClassMessageBox(false);
									messageObj.setSize(250,100);
									messageObj.setShadowDivVisible(true);
									messageObj.display();
								}

								function closeMessage() {
									messageObj.close();	
								}
							</script>
						</f:verbatim>
						<h:selectOneMenu />
					</wf:container>
					<wf:container styleClass="main_container">
						<wf:container styleClass="options_container">
							<f:verbatim>
								<div id="dhtmlgoodies_tabView1">
								  <div class="dhtmlgoodies_aTab">
								    Content of tab 1
								  
								  </div>
								  <div class="dhtmlgoodies_aTab">
								    Elements
								  	<ul id="firstlist">
										<li>
											<a href="#">New form</a>
										
										</li>
										<li><a href="#">Save form</a></li>
										<li><a href="#">Delete form</a></li>
										<li><a href="#">Import</a></li>
										<li><a href="#">Export</a></li>
										<li><a href="#">Switch form</a></li>
									</ul>
								  </div>
								  <div class="dhtmlgoodies_aTab">
								    Content of tab 3
								  
								  </div>
								</div>
								<script type="text/javascript">
									initTabs('dhtmlgoodies_tabView1',Array('Form properties','Add field','Form properties'),1,400,400);
								</script>
							</f:verbatim>
						</wf:container>
						<wf:container styleClass="form_container">
							<t:dataList id="secondlist"
						        styleClass="standardList" 
						       	var="field"
						        value="#{palette.fields}"
						        layout="unorderedList">
						        <fb:field value="#{field.name}" />
						    </t:dataList>
							<f:verbatim>
								<script type="text/javascript">
									// <![CDATA[
									   Sortable.create("firstlist",{dropOnEmpty:true,containment:["firstlist","workspaceform1:secondlist"],constraint:false});
									   Sortable.create("workspaceform1:secondlist",{dropOnEmpty:true,containment:["firstlist","workspaceform1:secondlist"],constraint:false});
									 // ]]>
								</script>
							</f:verbatim>
						</wf:container>
					</wf:container>
				</wf:container>
			</h:form>
		</ws:page>
	</f:view>
</jsp:root>
