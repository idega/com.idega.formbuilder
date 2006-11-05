<form name="newFormDialogForm">
	<table border="0">
		<tr>
			<td><img src="/images/heading3.gif"></td>
		</tr>
		<tr>
			<td><p>Enter the title for the new form</p></td>
		</tr>
		<tr>
			<td>
				<input class="default" type="text" id="formName" name="file1"/>
			</td>
		</tr>
		<tr>
			<td><input type="button" value="Cancel" onclick="closeMessage()">
				<input type="button" value="Create" onclick="createNewForm()">
			</td>
		</tr>
	</table>
	<script type="text/javascript">
		document.forms['newFormDialogForm'].elements['formName'].focus();
	</script>
</form>