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
							<input type="text" id='investor' />
						</td>
						<td width="30%;"><label
							style="width: 100px; text-align: right; color: #393939; font-family: 'Open Sans'; font-size: 13px; line-height: 1.5;">借款人：</label>
							<input type="text" id='loancompany' /></td>
						<td width="30%;"><label
							style="width: 100px; text-align: right; color: #393939; font-family: 'Open Sans'; font-size: 13px; line-height: 1.5;">状态：</label>
							<select class="selectpicker show-tick dropup"
							style="width: 170px; height: 34px;" id='status'>
								<option
									class="color: #393939; font-family: 'Open Sans'; font-size: 13px; line-height: 1.5;">未复核</option>
								<option
									class="color: #393939; font-family: 'Open Sans'; font-size: 13px; line-height: 1.5;">已复核</option>
						</select></td>
					</tr>
					<tr style="height: 20px;"></tr>
					<tr>
						<td><label
							style="width: 100px; text-align: right; color: #393939; font-family: 'Open Sans'; font-size: 13px; line-height: 1.5;">投资期限：</label>
							<input type="text" id='investorterm' /></td>
						<td><label
							style="width: 100px; text-align: right; color: #393939; font-family: 'Open Sans'; font-size: 13px; line-height: 1.5;">产
								品：</label> <input type="text" id='product' /></td>
						<td></td>
					</tr>
				</table>

				<div class="row" style="margin-left: 20px; margin-top: 20px;">
					<div class="form-group">
						<div class="col-sm-3 ">
							<label
								class="color: #393939; font-family: 'Open Sans'; font-size: 13px; line-height: 1.5;">
								总投资金额： </label><span style="color: red;">￥150.000</span>
						</div>
						<div class="col-sm-3 ">
							<label
								class="color: #393939; font-family: 'Open Sans'; font-size: 13px; line-height: 1.5;">
								总资产金额 ： </label> <span style="color: red;">￥150.380</span>
						</div>
						<div class="col-sm-2 ">
							<label
								class="color: #393939; font-family: 'Open Sans'; font-size: 13px; line-height: 1.5;">
								总记录数： </label> <span style="color: red;">200</span>
						</div>
						<button type="button"
							class="fm-button ui-state-default ui-corner-all fm-button-icon-left btn btn-sm btn-primary"
							style="margin-right: 10px; margin-left: 20px;">导出</button>
						<button type="button" id='review'
							class="fm-button ui-state-default ui-corner-all fm-button-icon-left btn btn-sm btn-primary"
							style="margin-right: 10px;">复核</button>
						<button type="button" id='detailedaccounting'
							class="fm-button ui-state-default ui-corner-all fm-button-icon-left btn btn-sm btn-primary">查询</button>
					</div>
				</div>

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
	$('.page-content-area')
			.ace_ajax(
					'loadScripts',
					scripts,
					function() {jQuery(function($) {

							var grid_selector = "#grid-table";
							var pager_selector = "#grid-pager";

							// resize to fit page size
							$(window).on('resize.jqGrid',
									function() {$(grid_selector).jqGrid('setGridWidth',$(".page-content").width());
									});
							// resize on sidebar collapse/expand
							var parent_column = $(grid_selector).closest(
									'[class*="col-"]');
                            //初始化jQgrid的时候调用
							$(document)
									.on(
											'settings.ace.jqGrid',
											function(ev, event_name, collapsed) {
												if (event_name === 'sidebar_collapsed'
														|| event_name === 'main_container_fixed') {
													// setTimeout is for webkit only to give time for DOM changes and then redraw!!!
													setTimeout(function() {

														$(grid_selector).jqGrid('setGridWidth',parent_column.width());
														
													}, 0);
												}
											});

							jQuery(grid_selector)
									.jqGrid(
											{
												subGrid : false,
												url : "${contextPath}/sys/detailedaccounting/detailedaccountingQuery",
												datatype : "json",
												height : 160,
												colNames : [ 'InvID', '投资人',
														'投资人证件', '投资金额',
														'投资期限', '投资起期', '产品',
														'错配ID', '借贷人', '借款金额',
														'状态', '邮件状态' ],
												colModel : [
														{
															name : 'invID',
															index : 'invID',
															label : 'InvID',
															align : 'center',
															editable : true,
															hidden : true,
														},
														{
															name : 'investor',
															index : 'investor',
															label : '投资人',
															align : 'center',
															editable : true,
														//formatter : cLink,
															formoptions: {rowpos:2,colpos:1} ,

														},
														{
															name : 'investorID',
															index : 'investorID',
															label : '投资人证件',
															editable : true,
															align : 'center',
															formoptions: {rowpos:2,colpos:2} ,
														},
														{
															name : 'investMoney',
															index : 'investMoney',
															label : '投资金额',
															editoptions : {
																readonly : true
															},
															formatter : 'currency',
															formatoptions : {
																thousandsSeparator : ",",
																prefix : "￥ ",
																decimalPlaces : 0,
															},
															editable : true,
															align : 'center',
															formoptions: {rowpos:3,colpos:1} 
														},
														{
															name : 'investorterm',
															index : 'investorterm',
															label : '投资期限',
															editoptions : {
																readonly : true
															},
															editable : true,
															align : 'center',
															formoptions: {rowpos:3,colpos:2} ,
														},
														{
															name : 'investDate',
															index : 'investDate',
															label : '投资起期',
															editoptions : {
																readonly : true
															},
															//edittype:'custom',
															editable : true,
															//editoptions:{custom_element: myelem, custom_value:myvalue},
															//custom:true,
															//custom_func:mythod,
															formatter : 'date',
															formatoptions : {
																newformat : 'Y-m-d'
															},
															align : 'center',
															formoptions: {rowpos:4,colpos:1} 
														},
														{
															name : 'product',
															index : 'product',
															editoptions : {
																readonly : true
															},
															label : '产品',
															editable : true,
															align : 'center',
															formoptions: {rowpos:4,colpos:2} ,
														},
														{
															name : 'mismatchID',
															index : 'mismatchID',
															label : '错配ID',
															editable : false,
															align : 'center',
															hidden : true,
															//formoptions: {rowpos:5,colpos:1} ,
														},
														{
															name : 'loancompany',
															index : 'loancompany',
															label : '借贷人',
															editoptions : {
																readonly : true
															},
															editable : true,
															align : 'center',
															formoptions: {rowpos:5,colpos:1} ,
														},
														{
															name : 'loanMoney',
															index : 'loanMoney',
															formatter : 'currency',
															editoptions : {
																readonly : true
															},
															formatoptions : {
																thousandsSeparator : ",",
																prefix : "￥ ",
																decimalPlaces : 0
															},
															label : '借款金额',
															editable : true,
															align : 'center',
															formoptions: {rowpos:5,colpos:2} ,
														},
														{
															name : 'status',
															index : 'status',
															label : '状态',
															formatter : JqGridInlineEditor_SelectFormatter,
															editable : true,
															edittype : "select",
															editoptions : {
																disabled : true,
																value : {
																	0 : '未复核',
																	1 : '已复核',
																	2 : '已复核',
																}
															},
															align : 'center',
															formoptions: {rowpos:6,colpos:1} ,
														},
														{
															name : 'mailStatus',
															index : 'mailStatus',
															label : '邮件状态',
															formatter : JqGridInlineEditor_SelectFormatter,
															editable : true,
															edittype : "select",
															editoptions : {
																disabled : true,
																value : {
																	0 : '未发送',
																	1 : '已发送'
																}
															},
															align : 'center',
															formoptions: {rowpos:6,colpos:2} ,
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
												multiselect : true,
												//multikey : "ctrlKey",
												multiboxonly : true,
												loadComplete : function() {
													var table = this;
													setTimeout(
															function() {
														
																Allchecked(table);
																styleCheckbox(table);
																updateActionIcons(table);
																updatePagerIcons(table);
																enableTooltips(table);
																$(".ui-jqgrid-labels")
																.find('div')
																.each(function (){

																	$(this).attr({'style':'text-align:center'});

																	});
															}, 0);
												},
												editurl : "${contextPath}/sys/detailedaccounting/detailedaccountingModify",
												//caption : "用户管理列表",
												//autowidth : true,
												/**
												grouping : true,
												groupingView : {
												groupField : [ 'name' ],
												groupDataSorted : true,
												plusicon : 'fa fa-chevron-down bigger-110',
												minusicon : 'fa fa-chevron-up bigger-110'
												},*/
												//onSelectRow:function (){}单击
												ondblClickRow : function(rowid) {
													jQuery(this).jqGrid('editGridRow',rowid,
																	{
																		// edit record form
																		// closeAfterEdit: true,
																		 width: 700,
																		//align:'left',
																		recreateForm : true,
																		beforeShowForm : function(
																				e) {
																			var form = $(e[0]);
																			form
																					.closest(
																							'.ui-jqdialog')
																					.find(
																							'.ui-jqdialog-titlebar')
																					.wrapInner(
																							'<div class="widget-header" />')
																			style_edit_form(form);
																			//jQuery('#editmodgrid-table').jqGrid('setLabel','investors',[{color:'red'}]);
																		},
																		errorTextFormat : function(
																				response) {
																			var result = eval('('
																					+ response.responseText
																					+ ')');
																			return result.message;
																		},
																		top : $(
																		        ".page-content")
																		             .height()/4,
																		left : $(
																				".page-content")
																				.width() / 3,

																	});

												},

											});

							//后台传值选择的非格式值转换
							function JqGridInlineEditor_SelectFormatter(
									cellvalue, options, rowObject) {
								var temp = '';
								$.each(options.colModel.editoptions.value,
										function(key, value) {
											if (cellvalue == key
													|| cellvalue == value) {
												temp = value;
											}
										});

								return temp;
							}

							//前台传值选择的非格式值转换
							function JqGrid_SelectFormatter(
									cellvalue, options, rowObject) {
								$.each(options.colModel.editoptions.value,
										function(key, value) {
											if (cellvalue == key
													|| cellvalue == value) {
												cellvalue = key;
											}
										});
							}
							//自定义编辑的格式

							function myelem(value, options) {
								var el = document.createElement("input");
								el.type = "text";
								el.value = value;
								//el.setAttribute("id","date-picker"); 
								//el.setAttribute("class","date-picker");   
								return el;
							}

							//获取值
							function myvalue(elem) {
								return $(elem).val();
							}

							//jQuery('#editmodgrid-table').jqGrid('setLabel','investors',[{color:'red'}]);
							function enableTooltips(table) {
								$('.navtable .ui-pg-button').tooltip({
									container : 'body'
								});
								$(table).find('.ui-pg-div').tooltip({
									container : 'body'
								});
							}

							$(window).triggerHandler('resize.jqGrid');// trigger window resize to make the grid get the correct size

							//jQuery(grid_selector).setColProp('investors',{editable:false});动态设置列属性
							//var a = jQuery(grid_selector).getColProp(
							//	'mailStatus');
							//alert(a);
							//function addRecord(){ alert(123);}
							/*	jQuery(grid_selector)
										.jqGrid(
												'navGrid',
												pager_selector,
												{
													add : <shiro:hasPermission name="ROLE_ADMIN:add">true</shiro:hasPermission><shiro:lacksPermission name="ROLE_ADMIN:add">false</shiro:lacksPermission>,
													addicon : 'ace-icon fa fa-plus-circle purple'
												},
												{
													// new record form
													// width: 700,
													closeAfterAdd : true,
													recreateForm : true,
													viewPagerButtons : false,
													beforeShowForm : function(e) {
														var form = $(e[0]);
														form
																.closest(
																		'.ui-jqdialog')
																.find(
																		'.ui-jqdialog-titlebar')
																.wrapInner(
																		'<div class="widget-header" />')
														style_edit_form(form);
													},
													errorTextFormat : function(
															response) {
														var result = eval('('
																+ response.responseText
																+ ')');
														return result.message;
													}
												});

							//};
							/*function beforeDeleteCallback(e) {
								var form = $(e[0]);
								if (form.data('styled'))
									return false;
								form.closest('.ui-jqdialog').find('.ui-jqdialog-titlebar').wrapInner('<div class="widget-header" />')
							/*	style_delete_form(form);
								form.data('styled', true);
							}

							function styleCheckbox(table) {

								$(table)
										.find('input:checkbox')
										.addClass('ace')
										.wrap('<label />')
										.after('<span class="lbl align-top" />');
								$('.ui-jqgrid-labels th[id*="_cb"]:first-child')
										.find('input.cbox[type=checkbox]')
										.addClass('ace')
										.wrap('<label />')
										.after('<span class="lbl align-top" />');

							}
							;*/

							function beforeEditCallback(e) {
								var form = $(e[0]);
								form.closest('.ui-jqdialog').find(
										'.ui-jqdialog-titlebar').wrapInner(
										'<div class="widget-header" />')
								style_edit_form(form);
							}

							//$('#edithdgrid-table').click(function(){alert(1243)})

							function style_edit_form(form) {
								//form.find('input[name=investDate1]')
								// enable datepicker on "birthday" field and switches for "stock" field
								form.find('input[name=investDate1]')
										.datepicker({
											format : 'yyyy/mm/dd',
											autoclose : true,
											language : 'zh-CN'
										})

								form.find('input[name=statusCn]').addClass(
										'ace ace-switch ace-switch-5').after(
										'<span class="lbl"></span>');
								// don't wrap inside a label element, the checkbox value won't be submitted (POST'ed)
								// .addClass('ace ace-switch ace-switch-5').wrap('<label class="inline" />').after('<span class="lbl"></span>');

								// update buttons classes
								var buttons = form.next().find(
										'.EditButton .fm-button');
								buttons.addClass('btn btn-sm').find(
										'[class*="-icon"]').hide();// ui-icon, s-icon
								buttons.eq(0).addClass('btn-primary').prepend(
										'<i class="ace-icon fa fa-check"></i>');
								buttons.eq(1).prepend(
										'<i class="ace-icon fa fa-times"></i>')
								//动态选择按钮
								/*buttons = form.next().find('.navButton a');
								buttons.find('.ui-icon').hide();
								buttons
										.eq(0)
										.append(
												'<i class="ace-icon fa fa-chevron-left"></i>');
								buttons
										.eq(1)
										.append(
												'<i class="ace-icon fa fa-chevron-right"></i>');*/
							}

							// replace icons with FontAwesome icons like above
							function updatePagerIcons(table) {
								var replacement = {
									'ui-icon-seek-first' : 'ace-icon fa fa-angle-double-left bigger-140',
									'ui-icon-seek-prev' : 'ace-icon fa fa-angle-left bigger-140',
									'ui-icon-seek-next' : 'ace-icon fa fa-angle-right bigger-140',
									'ui-icon-seek-end' : 'ace-icon fa fa-angle-double-right bigger-140'
								};
								$(
										'.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon')
										.each(
												function() {
													var icon = $(this);
													var $class = $.trim(icon
															.attr('class')
															.replace('ui-icon',
																	''));

													if ($class in replacement)
														icon
																.attr(
																		'class',
																		'ui-icon '
																				+ replacement[$class]);
												});
							}

							$(document).one('ajaxloadstart.page', function(e) {
								$(grid_selector).jqGrid('GridUnload');
								$('.ui-jqdialog').remove();
							});

							/*	var mydata = [ {
									investors : '张三',
									inveID : '3612111100',
									inveSum : '￥1000',
									inveTime : '1个月',
									inveStartDate : '2015.05.05',
									productBag : '惠普A',
									loadPerson : 'X公司',
									loadSum : '￥1,234.00',
									status : '已复核',
									mailStatus : '已发送',
								}, {
									investors : '张三',
									inveID : '560001277',
									inveSum : '￥1000',
									inveTime : '1个月',
									inveStartDate : '2015.05.05',
									productBag : '惠普A',
									loadPerson : 'Y公司',
									loadSum : '￥3,588.00',
									status : '待复核',
									mailStatus : '未发送',
								}, {
									investors : '张三',
									inveID : '778899001',
									inveSum : '￥1000',
									inveTime : '1个月',
									inveStartDate : '2015.05.05',
									productBag : '惠普A',
									loadPerson : 'Z公司',
									loadSum : '￥45,111.77',
									status : '已复核',
									mailStatus : '已发送',
								} ];
								for ( var i = 0; i < mydata.length; i++) //循环给每行添加数据
								{
									jQuery(grid_selector).jqGrid('addRowData',
											i + 1, mydata[i]);
								}
								;*/

							function Allchecked() {
								var rowIds = jQuery(grid_selector).jqGrid(
										'getDataIDs');
								for ( var k = 0; k <= rowIds.length; k++) {
									jQuery(grid_selector).jqGrid(
											'setSelection', k);
									//alert(12666);
									//    //alert('rowIds.length='+rowIds.length+","+'k='+k);

									//var curRowData = jQuery("#grid_selector").jqGrid('getRowData', rowIds[k]);
									//var curChk = $("#" + rowIds[k] + "").find(
									//			":checkbox");
									// curChk.attr('name', 'checkboxname');   //给每一个checkbox赋名字
									// curChk.attr('value', curRowData['MY_ID']);   //给checkbox赋值
									// curChk.attr('title', curRowData['NAME'] );   //给checkbox赋予额外的属性值
									//curChk.attr('checked', 'true'); //设置所有checkbox被选中

								}
								;
								//$(table).find('input[id=jqgh_grid-table_cb]').attr('checked', 'true');
								//jQuery(grid_selector).jqGrid(
								//'resetSelection');
							}
							;

							// enable search/filter toolbar
							// jQuery(grid_selector).jqGrid('filterToolbar',{defaultSearch:true,stringResult:true})
							// jQuery(grid_selector).filterToolbar({});
							// switch element when editing inline
							function aceSwitch(cellvalue, options, cell) {
								setTimeout(
										function() {
											$(cell)
													.find(
															'input[type=checkbox]')
													.addClass(
															'ace ace-switch ace-switch-5')
													.after(
															'<span class="lbl"></span>')
													.attr('checked', 'true');
											;
										}, 0);
							}

							// enable datepicker
							//$('#date-picker').datepicker({
							//	autoclose : true,
							//		todayHighlight : true,
							//		format : 'yyyy/mm/dd',
							//autoclose : true,
							//	language : 'zh-CN'
							//});

							function beforeDeleteCallback(e) {
								var form = $(e[0]);
								if (form.data('styled'))
									return false;
								form.closest('.ui-jqdialog').find(
										'.ui-jqdialog-titlebar').wrapInner(
										'<div class="widget-header" />')
								style_delete_form(form);
								form.data('styled', true);
							}

							function beforeEditCallback(e) {
								var form = $(e[0]);
								form.closest('.ui-jqdialog').find(
										'.ui-jqdialog-titlebar').wrapInner(
										'<div class="widget-header" />')
								style_edit_form(form);
							}

							// it causes some flicker when reloading or navigating grid
							// it may be possible to have some custom formatter to do this as the grid is being created to prevent this
							// or go back to default browser checkbox styles for the grid
							function styleCheckbox(table) {
								/**
								 * $(table).find('input:checkbox').addClass('ace') .wrap('<label />') .after('<span class="lbl align-top" />') $('.ui-jqgrid-labels th[id*="_cb"]:first-child')
								 * .find('input.cbox[type=checkbox]').addClass('ace') .wrap('<label />').after('<span class="lbl align-top" />');
								 */
							}

							// unlike navButtons icons, action icons in rows seem to be hard-coded
							// you can change them like this in here if you want
							function updateActionIcons(table) {

								var replacement = {
									'ui-ace-icon fa fa-pencil' : 'ace-icon fa fa-pencil blue',
									'ui-ace-icon fa fa-trash-o' : 'ace-icon fa fa-trash-o red',
									'ui-icon-disk' : 'ace-icon fa fa-check green',
									'ui-icon-cancel' : 'ace-icon fa fa-times red'
								};
								$(table).find('.ui-pg-div span.ui-icon').each(
										function() {
											var icon = $(this);
											var $class = $.trim(icon.attr(
													'class').replace('ui-icon',
													''));
											if ($class in replacement)
												icon.attr('class', 'ui-icon '
														+ replacement[$class]);
										});
							}

							//点击的查询的时候获取页面获取页面参数
							$('#detailedaccounting').on('click', function() {
								var investor = $('#investor').val();
								var loancompany = $('#loancompany').val();
								var status = $('#status').val();

	                            if(status=='未复核'){status=0; }
                                if(status=='已复核'){status=1; }

								var investorterm = $('#investorterm').val();
								var product = $('#product').val();
								jQuery(grid_selector).jqGrid("setGridParam", {
									datatype : "json", //设置数据类型
									postData : {
										'investor' : investor,
										'loancompany' : loancompany,
										'status' : status,
										'investorterm' : investorterm,
										'product' : product,

									}
								});

								jQuery(grid_selector).trigger('reloadGrid');
							})
							//点击的复核的时候获取页面获取页面状态
							$('#review')
									.on(
											'click',
											function() {
												var selectedRowCount = 0;
												var operation = getSelectedRow();
												if (operation == "") {
													return
												} else {
													selectedRowCount = operation.length;
													
												}
												$
														.ajax({
															url : "${contextPath}/sys/detailedaccounting/detailedaccountingStatusModify",
															async : true,
															data : {
																operation : operation,
																selectedRowCount : selectedRowCount,
															},
															type : 'post',
															dataType : 'json',
															success : function(
																	data) {
																jQuery(
																		grid_selector)
																		.trigger(
																				'reloadGrid');

															}
														});
											})

							//得到JQgrid选中行id
							function getSelectedRow() {
								var rowDataArr = [];

								//获取选中行(单行)的ID

								//var id = jQuery(grid_selector).jqGrid('getGridParam','selrow');

								//获取选中行(多行)的ID
								var idArr = jQuery(grid_selector).jqGrid(
										'getGridParam', 'selarrrow');

								//根据id获取行数据,返回的是列表
								//rowId不是idArr的值 而是 它从0开始的索引
								for ( var rowId in idArr) {
									var rowDatas = jQuery(grid_selector)
											.jqGrid('getRowData', idArr[rowId]);
									rowDataArr.push(rowDatas);
								}
								return rowDataArr;

								//取到选中行某一字段的值，其中name为colModel中定义的字段名
								//var row = rowDatas["name"];
							}

						});
					});
</script>
