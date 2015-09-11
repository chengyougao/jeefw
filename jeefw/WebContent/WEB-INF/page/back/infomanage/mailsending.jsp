<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>

<link rel="stylesheet"
	href="${contextPath}/static/assets/css/datepicker.css" />
<link rel="stylesheet"
	href="${contextPath}/static/assets/css/jquery-ui.css" />
<link rel="stylesheet"
	href="${contextPath}/static/assets/css/ui.jqgrid.css" />


<!-- ajax layout which only needs content area -->

<div class="row">
	<div class="col-xs-12">
		<div class="row">
			<div class="col-xs-12">
				<table width="100%;" style="margin-top: 20px;">
					<tr align="left">
						<td width="30%;"><label
							style="width: 100px; text-align: right; color: #393939; font-family: 'Open Sans'; font-size: 13px; line-height: 1.5;">投资人：</label>
							<input type="text" id="investor"/></td>
						<td width="30%;"><label
							style="width: 100px; text-align: right; color: #393939; font-family: 'Open Sans'; font-size: 13px; line-height: 1.5;">投资期限：</label>
							<input type="text" id="investTerm"/></td>
						<td width="30%;"><label
							style="width: 100px; text-align: right; color: #393939; font-family: 'Open Sans'; font-size: 13px; line-height: 1.5;">状态：</label>
							<select class="selectpicker show-tick dropup" id="checkStatus" style="width: 170px; height: 34px;">
								<option
									class="color: #393939; font-family: 'Open Sans'; font-size: 13px; line-height: 1.5;" value="1">已复核</option>
								<option
									class="color: #393939; font-family: 'Open Sans'; font-size: 13px; line-height: 1.5;" value="0">未复核</option>
							</select>
						</td>
					</tr>
					<tr style="height: 20px;"></tr>
					<tr>
						<td><label style="width: 100px; text-align: right; color: #393939; font-family: 'Open Sans'; font-size: 13px; line-height: 1.5;">产品：</label> 
						<input type="text" id="produt"/></td>
						<td>
							<div class="form-inline">
								<label
									style="width: 100px; text-align: right; color: #393939; font-family: 'Open Sans'; font-size: 13px; line-height: 1.5;"
									class="form-group">错配日期：</label>
								<div class="input-group form-group" style="width: 170px;">
									<input class="form-control date-picker" id="datePicker"
										type="text" data-date-format="yyyy-mm-dd" /> <span
										class="input-group-addon"> <i
										class="fa fa-calendar bigger-110"></i>
									</span>
								</div>
							</div>
						</td>
						<td></td>
					</tr>
					<tr style="height: 20px;"></tr>
					<tr>
						<td></td>
						<td align="right"><button
								class="fm-button ui-state-default ui-corner-all fm-button-icon-left btn btn-sm btn-primary"
								style="width: 80px; text-align: center; background-color: #d6487e !important; border-color: #d6487e;">删除重做</button>
						</td>
						<td>
							<button
								class="fm-button ui-state-default ui-corner-all fm-button-icon-left btn btn-sm btn-primary"
								style="width: 80px; text-align: center; margin-left: 15px;"
								id="sendEmail">发送邮件</button>
							<button
								class="fm-button ui-state-default ui-corner-all fm-button-icon-left btn btn-sm btn-primary"
								style="width: 80px; text-align: center; margin-left: 15px;"
								id="search">查询</button>
						</td>
					</tr>
				</table>

				<div class="hr hr-15 dotted hr-double"></div>

				<!-- 添加表格 -->
				<table id="grid-table"></table>
				<!-- 添加下面的脚注 -->
				<div id="grid-pager"></div>

			</div>
		</div>
	</div>
</div>


<!-- page specific plugin scripts-->
<script type="text/javascript">
	var scripts = [
			null,
			"${contextPath}/static/assets/js/jquery.dataTables.js",
			"${contextPath}/static/assets/js/date-time/bootstrap-datepicker.js",
			"${contextPath}/static/assets/js/jqGrid/jquery.jqGrid.js",
			"${contextPath}/static/assets/js/jqGrid/i18n/grid.locale-cn.js",
			null ];
	var grid_selector = "#grid-table";
	var pager_selector = "#grid-pager";
	var investIds ="";
	$(document).ready(function(){
		showDetails(grid_selector);
		$("#search").bind("click",function(){
			BrowseFolder();
			//getMess();
		});
		$("#sendEmail").bind("click",function(){
			investIds = getCheckVal(grid_selector);
			createPDF(investIds);
		});
	});
	//获得选中信息
	function getCheckVal(grid_selector){
		var rowData = jQuery(grid_selector).jqGrid('getGridParam','selarrrow');
	    if(rowData.length) {
	        for(var i=0;i<rowData.length;i++)
	        {
	           var id= jQuery(grid_selector).jqGrid('getCell',rowData[i],'ids');//name是colModel中的一属性
	           var status= jQuery(grid_selector).jqGrid('getCell',rowData[i],'status');
	           var mailStatus= jQuery(grid_selector).jqGrid('getCell',rowData[i],'mailStatus');
	           if(status==0){
	        		alert("存在未复核选项，不能发送邮件！");
	        		return;
	           } 
        	   if(mailStatus ==2){
	        	   alert("邮件已发送，不能重复发送邮件！");
	        		return;
	           }else{
	        	   if(i<rowData.length-1){
		        	   investIds=investIds+id+",";
		           }else{
		        	   investIds=investIds+id;
		           } 
	           }
	        }
	    }
		return investIds;
	}
	//生成pdf文件
	function createPDF(investIds){
		var postData=investIds+"/"+$("#investor").val()+"/"+$("#investTerm").val()+"/"+$("#produt").val()+"/"+$("#datePicker").val()+"/"+$("#checkStatus").val();
		$.ajax({
		      type: "POST",
		      url: "${contextPath}/sys/mail/sendMails",
		      data: {"postData":postData},
		      async:false,
		      success: function(msg){
				data =(new Function("","return "+msg))();
				alert(data.message);
		      }
		  });
		getMess(); 
	}
	//信息查询
	function getMess(){
		var postData="/"+$("#investor").val()+"/"+$("#investTerm").val()+"/"+$("#produt").val()+"/"+$("#datePicker").val()+"/"+$("#checkStatus").val();
		jQuery(grid_selector).jqGrid("setGridParam", {
			url : "${contextPath}/sys/mail/index",
			datatype : "json", //设置数据类型
			postData :{"postData":postData}, 
		}).trigger('reloadGrid');
	}

	//初始化页面信息
	function showDetails(grid_selector,investIds) {
		$('.page-content-area').ace_ajax('loadScripts',scripts,function() {
				jQuery(function($) {
					$('.date-picker').datepicker({
						autoclose : true,
						todayHighlight : true,
					})
					.next().on(ace.click_event, function() {
						$(this).prev().focus();
					});
					if(!$("#datePicker").val()){
						$(".date-picker").datepicker('setDate', new Date());
					};
					$(window).on('resize.jqGrid',
							function() {$(grid_selector).jqGrid('setGridWidth',$(".page-content").width());
							});
					// resize on sidebar collapse/expand
					var parent_column = $(grid_selector).closest('[class*="col-"]');
					$(document).on('settings.ace.jqGrid',function(ev, event_name, collapsed) {
										if (event_name === 'sidebar_collapsed'|| event_name === 'main_container_fixed') {
											setTimeout(function() {$(grid_selector).jqGrid('setGridWidth',
																		parent_column.width());
													}, 0);
										}
									});
				 });
				
				
				var postData="/"+$("#investor").val()+"/"+$("#investTerm").val()+"/"+$("#produt").val()+"/"+$("#datePicker").val()+"/"+$("#checkStatus").val();
				
				jQuery(grid_selector).jqGrid({
							subGrid : false,
							loadonce: true,
							url : "${contextPath}/sys/mail/index", 
							datatype : "json",
							postData: {"postData":postData},
							height : 160,
							colNames : [ 'ID','投资人', '投资人证件',
									'投资金额', '投资期限', '投资起期',
									'产品', '状态', '邮件状态',
									'操作' ],
							colModel : [
						            {
				        				name : 'ids',
				        				index : 'ids',
				        				label : 'ID',
				        				editable : true,
				        				hidden : true,
				        			},
									{
										name : 'assigneeName',
										index : 'investors',
										label : '投资人',
										editable : true,
										align : 'center',
										formoptions:{rowpos: 2, colpos: 1}
									},
									{
										name : 'assigneeIdentityId',
										index : 'inveID',
										editable : true,
										label : '投资人证件',
										align : 'center',
										formoptions:{rowpos: 2, colpos: 2} 
									},
									{
										name : 'assigneeMoney',
										index : 'inveSum',
										label : '投资金额',
										align : 'center',
										formatter : "currency",
										formatoptions : {
											thousandsSeparator : ",",
											decimalSeparator : ".",
											prefix : "￥"
										}
									},
									{
										name : 'assigneeTmer',
										index : 'assigneeTmer',
										label : '投资期限',
										align : 'center',
										editable : true,
										formoptions:{rowpos: 3, colpos: 1} 
									},
									{
										name : 'assigneeDate',
										index : 'assigneeDate',
										label : '投资起期',
										align : 'center',
										editable : true,
										formoptions:{rowpos: 3, colpos: 2}
									},
									{
										name : 'product',
										index : 'product',
										label : '产品',
										align : 'center',
									},
									{
										name : 'status',
										index : 'status',
										label : '状态',
										formatter : "select",
				        				editoptions : {value : "0:未复核;1:已复核;2:已复核"},
										align : 'center',
									},
									 {
										name : 'mailStatus',
										index : 'mailStatus',
										label : '邮件状态',
										formatter: "select",
				        				editoptions : {value : "0:未发送;1:未发送;2:已发送"},
										align : 'center',
									}, 
									{
										name : 'operation',
										index : 'operation',
										label : '操作',
										align : 'center',
										formatter : edit
									}],

							viewrecords : true,
							sortname : "ids",
							rowNum : 4,
							rowList : [ 10, 20, 30 ],
							pager : pager_selector,
							altRows : true,
							multiselect : true,
							multiboxonly : true,
							loadComplete : function(response) {
								var table = this;
								setTimeout(function() {
											Allchecked();
											updatePagerIcons(table); }, 0);
							},
							editurl : "${contextPath}/sys/mail/updateMess"
					});
					function updatePagerIcons(table) {
						var replacement = {
							'ui-icon-seek-first' : 'ace-icon fa fa-angle-double-left bigger-140',
							'ui-icon-seek-prev' : 'ace-icon fa fa-angle-left bigger-140',
							'ui-icon-seek-next' : 'ace-icon fa fa-angle-right bigger-140',
							'ui-icon-seek-end' : 'ace-icon fa fa-angle-double-right bigger-140'
						};
						$('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(
							function() {
								var icon = $(this);
								var $class = $.trim(icon.attr('class').replace('ui-icon',''));
								if ($class in replacement)
									icon.attr('class','ui-icon '+ replacement[$class]);
							}
						);
					};
					function Allchecked() {
						var rowIds = jQuery(grid_selector).jqGrid('getDataIDs');
						for ( var k = 0; k <= rowIds.length; k++) {
							jQuery(grid_selector).jqGrid('setSelection', k);
						};
					};
					
	        		function edit( ) {
	        			var template = "<button onclick='ondblClickRow()' class='btn btn-xs btn-warning edit'>修改</button>";
	        			return template;
	        		};
			});
		
	};
	
	function ondblClickRow() {
		setTimeout('editView()',1);
	};
	function editView() {
		var id = jQuery(grid_selector).jqGrid('getGridParam','selrow');
		jQuery(grid_selector).jqGrid('editGridRow',id,
			{
				recreateForm : true,
				beforeShowForm : function(e) {
					$(".ui-jqdialog-title").html("客户信息修改");
					$("#sData").html("保存");
					e.find('#assigneeTmer').attr('readOnly',id=='new'?false:true);
					e.find('#assigneeDate').attr('readOnly',id=='new'?false:true)
				}, 
				afterComplete :function(response) {
					getMess();
					var result = eval('('+ response.responseText+ ')');
					alert(result.message);
				},
				errorTextFormat : function(response) {
					var result = eval('('+ response.responseText+ ')');
					alert(result.message);
				},
				top : $(".page-content").height() / 2,
				left : $(".page-content").width() / 2,
				closeAfterEdit:true,
				width:'500'
			}
		);
	}
	
	function BrowseFolder() {
		alert(11);
	    try {
	        var Message = "Please select the folder path.";  //选择框提示信息
	        var Shell = new ActiveXObject("Shell.Application");
	        var Folder = Shell.BrowseForFolder(0, Message, 0x0040, 0x11); //起始目录为：我的电脑
	        //var Folder = Shell.BrowseForFolder(0,Message,0); //起始目录为：桌面
	        if (Folder != null) {
	            Folder = Folder.items();  // 返回 FolderItems 对象
	            Folder = Folder.item();  // 返回 Folderitem 对象
	            Folder = Folder.Path;   // 返回路径
	            if (Folder.charAt(Folder.length - 1) != "\\") {
	                Folder = Folder + "\\";
	            }
	            alert(Folder);
	            return Folder;
	        }
	    } catch (e) {
	        alert(e.message);
	    }
	}
	
</script>
