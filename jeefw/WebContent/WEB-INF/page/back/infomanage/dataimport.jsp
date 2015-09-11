<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>

	<div class="col-xs-12" id ="test">
		<div class="form-group margin-top-10">
				<table align="center" style="width: 50%; ">
					<tr class="FormData"  >
						<td style=" text-align: right;">
							投资人信息：
						</td>
						<td>
							<input  style="margin: 8px"  readonly="readonly" class="form-control "   id="invest"/>
						</td>
						<td>
			        		<button id="investBrowse" name="button" class="fm-button ui-state-default ui-corner-all fm-button-icon-left btn btn-sm btn-primary" type="button" title="打开">打开</button>
						</td>
						<td>
			        		<button id="investUpload" data-toggle="dropdown" name="button" data-toggle="modal" data-target="#myModal" class="fm-button ui-state-default ui-corner-all fm-button-icon-left btn btn-sm btn-primary" type="button" title="打开">导入</button>
						</td>
						
					</tr>
					<tr class="FormData" >
						<td style="text-align: right;">
							产品信息：
						</td>
						<td>
							<input size='15' style="margin: 8px" readonly="readonly" class="form-control "  id="loan"/>
						</td>
						<td>
				        	<button id="loanBrowse" name="button" class="fm-button ui-state-default ui-corner-all fm-button-icon-left btn btn-sm btn-primary" type="button" title="打开">打开</button>
						</td>
						<td>
			        		<button id="loanUpload" name="button" class="fm-button ui-state-default ui-corner-all fm-button-icon-left btn btn-sm btn-primary" type="button" title="打开">导入</button>
						</td>
						<td>
							<button id = "crossButton"            class="fm-button ui-state-default ui-corner-all fm-button-icon-left btn btn-sm btn-primary"  type="button">错配</button>
						</td>
					</tr>
			     </table>
			<!-- Modal -->
			<div class="modal hide fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
				<div class="modal-dialog">
					<div class="modal-content">
				         <div class="modal-header">
				            <button type="button" class="close" 
				               data-dismiss="modal" aria-hidden="true">
				                  &times;
				            </button>
				            <h4 class="modal-title" id="myModalLabel">
				             		  模态框（Modal）标题
				            </h4>
				         </div>
				         <div class="modal-body">
				            	在这里添加一些文本
				         </div>
				         <div class="modal-footer">
				            <button type="button" class="btn btn-default" 
				               data-dismiss="modal">关闭
				            </button>
				            <button type="button" class="btn btn-primary">
				               	提交更改
				            </button>
				         </div>
				      </div><!-- /.modal-content -->
				</div>
			</div>
	</div>
</div>
<script src="${contextPath}/static/assets/js/ace/elements.fileinput.js"></script>
<script src="${contextPath}/static/assets/js/fileupload/ajaxupload.js"></script>
<!--提示框-->
<script src="${contextPath}/static/assets/js/common/common.js"></script>
<script src="${contextPath}/static/assets/js/jquery.tips.js"></script>

<%-- <script src="${contextPath}/static/assets/js/bootstrap.min.js"></script> --%>

<script type="text/javascript">

//投资
var invest = {
	uploadVal : function() {
		var investFile = $("#investBrowse");
		var investUploadFile = new AjaxUpload(investFile, {
			action : ISS.Config.Global.rootUrl+"/uploadFile/uploadInvestFile",
			name : "file",
			autoSubmit : false,
			onChange : function(file, extension) {
				if(ISS.FileType.Exvel(file,"invest")){
					$("#invest").val(file);
				};
			}, 
			onComplete : function(data, extension) {
				data =(new Function("","return "+extension))();
				alert(data.result);
				$("#invest").val("");
				$('#myModal').modal('hide');
			}
		});
		$("#investUpload").click(function() {
			if(ISS.FileType.Have("invest")){
				$('#myModal').modal('show');
				investUploadFile.submit();
			};
		})
	}
}
//借款
var loan = {
		uploadVal : function() {
			var loanFile = $("#loanBrowse");
			var loanUploadFile = new AjaxUpload(loanFile, {
				action : ISS.Config.Global.rootUrl+"/uploadFile/uploadLoanFile",
				name : "file",
				autoSubmit : false,
				onChange : function(file, extension) {
					if(ISS.FileType.Exvel(file,"loan")){
						$("#loan").val(file)
					};
				}, 
				onComplete : function(data, extension) {
					data =(new Function("","return "+extension))();
					alert(data.result);
					$("#loan").val("");
					$('#myModal').modal('hide');
				}
			});
			$("#loanUpload").click(function() {
				if(ISS.FileType.Have("loan")){
					$('#myModal').modal('show');
					loanUploadFile.submit();
				};
			})
		}
	}

$(document).ready(function() {
	  $("#invest #loan").val("");
	  invest.uploadVal();
	  loan.uploadVal();
});

</script>
