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
				<div>
					<div class="row well well-sm">
						<div class="col-xs-6 col-sm-7 col-md-7" style="width: 30%;">
							<!-- #section:plugins/date-time.datepicker -->
							<div class="input-group">
								<input class="form-control date-picker" id="id-date-picker-1"
									name="id-date-picker-1" type="text"
									data-date-format="yyyy-mm-dd" readonly="readonly" /> <span
									class="input-group-addon"> <i
									class="fa fa-calendar bigger-110"></i> </span>
							</div>
						</div>
						<div class="col-xs-6 col-sm-7 col-md-7">
							<button id="historyDateBtn"
								class=" fm-button ui-state-default ui-corner-all fm-button-icon-left btn btn-sm btn-primary">查询</button>
						</div>
					</div>
					<div class="row">
						<div class="form-group" style="margin-left: 40px;">
							<div class="col-md-5 " style="height: 40px;">
								<label> 到期投资金额： </label><span style="color: red;">￥150.388</span>
							</div>
							<div class="col-md-7" style="height: 40px;">
								<label> 到期资产金额 ： </label> <span style="color: red;">￥110.755</span>
							</div>

							<div class="col-md-6" style="height: 40px;">
								<label> 合计资金盈亏： </label> <span style="color: red;">￥39.633</span>
							</div>
						</div>
					</div>

					<div class="hr hr-18 dotted hr-double"></div>

					<!-- 添加表格 -->
					<table id="grid-table"></table>
					<!-- 添加下面的脚注 -->
					<div id="grid-pager"></div>

				</div>
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
	$('.page-content-area').ace_ajax('loadScripts',scripts,function() {
						jQuery(function($) {
							//datepicker plugin
							$('.date-picker').datepicker({
								autoclose : true,
								todayHighlight : true,
							//initialDate:new Date(),
							//todayBtn :'linked', 
							//forceParse:true,
							//currentText:' 123',
							// maxDate: "+0d" ,
							//  defaultDate: +7,
							//onSelect:function(dateText,inst){
							//   $(".date-picker").datepicker("option","maxDate",dateText);
							//  $("#start_date").datepicker("option","default",dateText);
							// }
							})
							//show datepicker when clicking on the icon
							.next().on(ace.click_event, function() {
								$(this).prev().focus();
							});
							$(".date-picker").datepicker('setDate', new Date());

							//在页面加载完的时候，自动获取当前日期
							var historyDate = $('#id-date-picker-1').val();

							//datepicker的值改变是事件
							//$('.date-picker').datepicker().on('hide', function(ev){
							//		   alert(423);
							//})

							var grid_selector = "#grid-table";
							var pager_selector = "#grid-pager";
							// resize to fit page size
							$(window).on(
									'resize.jqGrid',
									function() {
										$(grid_selector).jqGrid('setGridWidth',
												$(".page-content").width());
									});
							// resize on sidebar collapse/expand
							var parent_column = $(grid_selector).closest(
									'[class*="col-"]');
							$(document)
									.on(
											'settings.ace.jqGrid',
											function(ev, event_name, collapsed) {
												if (event_name === 'sidebar_collapsed'
														|| event_name === 'main_container_fixed') {
													// setTimeout is for webkit only to give time for DOM changes and then redraw!!!
													setTimeout(
															function() {
																$(grid_selector)
																		.jqGrid(
																				'setGridWidth',
																				parent_column
																						.width());
															}, 0);
												}
											});

							jQuery(grid_selector)
									.jqGrid(
											{
												subGrid : false,
												url : "${contextPath}/sys/capitalhistory/capitalhistoryQuery",
												datatype : "json",
												postData : {
													'historyDate' : historyDate
												},
												height : 160,
												colNames : [ 'ID', '投资人',
														'投资金额', '投资到期日', '产品包',
														'借款金额', '借款到期日' ],
												colModel : [ {
													name : 'ids',
													index : 'ids',
													label : 'ID',
													align : 'center',
													hidden : true,
													key :true,
												}, {
													name : 'investor',
													index : 'investor',
													label : '投资人',
													align : 'center',
												}, {
													name : 'inveSum',
													index : 'inveSum',
													label : '投资金额',
													formatter:'currency', 
													formatoptions:{ thousandsSeparator: ",", prefix: "￥ ", decimalPlaces: 0},
													//formatoptions:{decimalSeparator:",", thousandsSeparator: ",", decimalPlaces: 2, prefix: "￥ "},
													align : 'center',
												}, {
													name : 'inveEndDate',
													index : 'inveEndDate',
													label : '投资到日期',
													formatter : 'date',
													formatoptions : {
														newformat : 'Y-m-d'
													},
													align : 'center',
												}, {
													name : 'investProdut',
													index : 'investProdut',
													label : '产品包',
													align : 'center',
												}, {
													name : 'loanSum',
													index : 'loanSum',
													label : '借款金额',
													formatter:'currency', 
													formatoptions:{ thousandsSeparator: ",", prefix: "￥ ", decimalPlaces: 0},
													align : 'center',
												}, {
													name : 'loadEndDate',
													index : 'loadEndDate',
													label : '借款到日期',
													align : 'center',
												} ],
												//scroll : 1, // set the scroll property to 1 to enable paging with scrollbar - virtual loading of records
												//sortname : "id",
												//sortorder : "asc",
												viewrecords : true,
												rowNum : 10,
												rowList : [ 10, 20, 30 ],
												pager : pager_selector,
												altRows : true,
												//toppager : true,
												//multiselect : true,
												//multikey : "ctrlKey",
												//multiboxonly : true,
												loadComplete : function() {
													var table = this;
													setTimeout(
															function() {
																//styleCheckbox(table);
																//updateActionIcons(table);
																updatePagerIcons(table);
																//enableTooltips(table);
																$(".ui-jqgrid-labels")
																.find('div')
																.each(function (){

																	$(this).attr({'style':'text-align:center'});

																	});
															}, 0);
												},
												//	editurl : "${contextPath}/sys/department/operateDepartment",
												//caption : "用户管理列表",
												autowidth : true,
											/**
											grouping : true, 
											groupingView : { 
												 groupField : ['name'],
												 groupDataSorted : true,
												 plusicon : 'fa fa-chevron-down bigger-110',
												 minusicon : 'fa fa-chevron-up bigger-110'
											},
											 */
											});


							$(window).triggerHandler('resize.jqGrid');// trigger window resize to make the grid get the correct size

						});
					});
</script>
