<form name="myForm">
	<table border="0">
		<tr>
			<td><img src="/images/heading3.gif"></td>
		</tr>
		<tr>
			<td><p>Are you sure you want/nto delete this form?</p></td>
		</tr>
		<tr>
			<td><input type="button" value="No" onclick="closeMessage()">
				<input type="button" value="Yes" onclick="">
			</td>
		</tr>
	</table>
	<script type="text/javascript">
		document.myForm.firstname.focus();
	</script>
</form>