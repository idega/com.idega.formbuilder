<form name="uploadForm">
	<table border="0">
		<tr>
			<td><p>Please choose the form you want to upload</p></td>
		</tr>
		<tr>
			<td>
				<input class="default" type="file" id="file1" name="file1"/>
			</td>
		</tr>
		<tr>
			<td>
				<input type="button" value="Upload" onclick="startProgress()">
				<input type="button" value="Cancel" onclick="closeMessage()">
			</td>
		</tr>
		<tr>
			<td>
				<div id="progressBar" style="display: none;">
            		<div id="theMeter">
               			<div id="progressBarText"></div>
                		<div id="progressBarBox">
                    		<div id="progressBarBoxContent"></div>
                		</div>
            		</div>
        		</div>
			</td>
		</tr>
	</table>
</form>