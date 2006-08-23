<?xml version="1.0"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:wf="http://xmlns.idega.com/com.idega.webface"
	xmlns:fb="http://xmlns.idega.com/com.idega.formbuilder"
	xmlns:ws="http://xmlns.idega.com/com.idega.workspace" version="1.2">

	<jsp:directive.page contentType="text/html" />

	<f:view>
		<ws:page id="formbuilder">
			<h:form id="workspaceform1">
				<fb:container id="level0" style="height: 100%">
					<fb:container id="lftBar">
						<f:verbatim>
    This is the left navigation ...
    </f:verbatim>
					</fb:container>
					<fb:container id="level1">

						<fb:container id="level2">

							<fb:container id="rgtBar">
								<f:verbatim>
          This is the right navigation...
          </f:verbatim>
							</fb:container>
							<fb:container id="level3">
								<fb:container id="toolBar">
									<fb:toolbar id="FBtoolbar">
					<wf:toolbarbutton id="button1" displayText="aaaaaa" />
					<wf:toolbarbutton id="button2" displayText="uhuhu" />
				</fb:toolbar>
								</fb:container>
								<fb:container id="main">
									<f:verbatim>CONTENT -</f:verbatim>
								</fb:container>
							</fb:container>
						</fb:container>
					</fb:container>
				</fb:container>
				<!--<fb:toolbar id="FBtoolbar" styleClass="fb_bgg">
					<wf:toolbarbutton id="button1" displayText="aaaaaa" styleClass="fb_bgg" />
					<wf:toolbarbutton id="button2" displayText="uhuhu" />
				</fb:toolbar>
				<fb:container
					style="width: 100px; height: 100px; background-color: red;">
				</fb:container>
				<fb:container
					style="width: 100px; height: 100px; background-color: yellow;">

				</fb:container>
			-->
			</h:form>
		</ws:page>
	</f:view>
</jsp:root>
