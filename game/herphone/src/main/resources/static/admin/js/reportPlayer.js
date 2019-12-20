$(document).ready(function() {
	layui.use('table', function() {
		var table = layui.table
			, form = layui.form;

		var count = reportPlayerCount();

		table.render({
			elem: '#reportPlayer'
			 ,url:'/gm/getListReportPlayer'
			, cellMinWidth: 80
			, limit: 50
			 ,request: {
				 pageName: 'page',   // 页码的参数名称，默认：page
				 limitName: 'count' ,// 每页数据量的参数名，默认：limit
			 }
			 ,parseData:function(res){
				 return{
					 "code":0,
					 "msg":"",
					 "count":count,
					 data:res
				 }
			 }
		    ,where:{
				state:0,
			}
			, cols: [[
				{ type: 'numbers', title: '编号' }
				, { field: 'toplayer', title: '被举报人', align: 'center', width: 140 }
				, {field: 'content', title: '评论内容', align: 'center', minWidth: 80,}
				, { field: 'fromplayer', title: '举报人', align: 'center', width: 140 }
				, { field: 'protectEyeTitle', title: '举报人是否护眼', align: 'center', width: 130,templet:function(d){
					if(d.protectEyeTitle == 0){
						return "✖";
					} 
					if(d.protectEyeTitle == 1){
						return "✔";
					}
				}}
				, { field: 'timestamp', title: '举报时间', align: 'center', width: 180}
				, { field: '', title: '处理', align: 'center', width: 100, templet: '#dispose', unresize: true }
				, { field: '', title: '删除', align: 'center', width: 100, templet: '#delete', unresize: true }
			]]
			, page: {
				layout: ['count', 'prev', 'page', 'next', 'limit', 'refresh', 'skip']
				, limits: [20, 50, 100]
				, first: false
				, last: false
			}
		});
		
		table.on('tool(reportPlayer)', function(obj) {
			console.log(obj);
			if(obj.event == "dis"){ //处理
				var itemTitle = "";   //被举报人是否护眼大队
				$.ajax({
					url: "/gm/getToPlayerIdentity/" + obj.data.toplayerId,
					type: 'get',
					async: false,
					dataType: "json",
					error: function(XMLHttpRequest, textStatus, errorThrown) {
						if(XMLHttpRequest.status == 401) {
							notLogin(XMLHttpRequest, textStatus, errorThrown);
						} else {
							layer.msg(XMLHttpRequest.responseJSON.message);
						}
					},
					success: function(result) {
						itemTitle = result
					}
				});
				
				if(itemTitle == 0){ //不是护眼大队
					layer.open({    //不是护眼大队
						type: 1,
						title:'处理',
						skin: 'layui-layer-molv', //加上边框
						area: ['830px', '230px'], //宽高                                                        
						content:"<br>&nbsp&nbsp<b>玩家  :</b>&nbsp;<b>" + obj.data.toplayer + "</b><br><br>" +
							"&nbsp&nbsp<b>操作 :</b><button class='btn btn-danger' style='margin-left:5px;' onclick='shutUps(this)' id='banned'>封禁帐号</button><br><br>" +
							"<button class='btn btn-danger' style='margin-left:48px;'>禁言</button><b> : </b>" +
							"<button class='btn btn-default' onclick='shutUps(300000)' style='margin-left:0px;'>5分钟</button>" +
							"<button class='btn btn-default' onclick='shutUps(1800000)' style='margin-left:3px;'>30分钟</button>" +
							"<button class='btn btn-default' onclick='shutUps(7200000)' style='margin-left:3px;'>2小时</button>" +
							"<button class='btn btn-default' onclick='shutUps(86400000)' style='margin-left:3px;'>1天</button>" +
							"<button class='btn btn-default' onclick='shutUps(604800000)' style='margin-left:3px;'>7天</button>" +
							"<button class='btn btn-default' onclick='shutUps(2592000000)' style='margin-left:3px;'>30天</button>" +
							"<div class='input-group' style='width:200px;position: absolute; top:114px; left:480px;' id='customIn'><input type='text' id='duration' class='form-control' placeholder='区间5分钟~9999分钟'></div>" +
							"<button id='pcustom' style='position: absolute; left:680px;' class='btn btn-warning' onclick='shutUps(this)'>自定义时长禁言</button>"
					});
				} 
				if(itemTitle == 1){ //是护眼大队
					layer.open({   //是护眼大队
						type: 1,
						title:'处理',
						skin: 'layui-layer-molv', //加上边框
						area: ['830px', '280px'], //宽高                                                        
						content:"<br>&nbsp&nbsp<b>玩家  :</b>&nbsp;<b>" + obj.data.toplayer + "</b><br><br>" +
							"&nbsp&nbsp<b>操作 :</b><button class='btn btn-danger' style='margin-left:5px;' onclick='shutUps(this)' id='banned'>封禁账号</button><br><br>" +
							"<button class='btn btn-danger' style='margin-left:48px;'>禁言</button><b> : </b>" +
							"<button class='btn btn-default' onclick='shutUps(300000)' style='margin-left:0px;'>5分钟</button>" +
							"<button class='btn btn-default' onclick='shutUps(1800000)' style='margin-left:3px;'>30分钟</button>" +
							"<button class='btn btn-default' onclick='shutUps(7200000)' style='margin-left:3px;'>2小时</button>" +
							"<button class='btn btn-default' onclick='shutUps(86400000)' style='margin-left:3px;'>1天</button>" +
							"<button class='btn btn-default' onclick='shutUps(604800000)' style='margin-left:3px;'>7天</button>" +
							"<button class='btn btn-default' onclick='shutUps(2592000000)' style='margin-left:3px;'>30天</button>" +
							"<div class='input-group' style='width:200px;position: absolute; top:114px;left:480px;' id='customIn'><input type='text' id='duration' class='form-control' placeholder='区间5分钟~9999分钟'></div>" +
							"<button id='pcustom' style='position: absolute; left:680px;' class='btn btn-warning' onclick='shutUps(this)'>自定义时长禁言</button>"+
							"<br><br><div><button class='btn btn-danger' style='margin-left:48px;'>身份</button><b> : </b>" +
							"<button class='btn btn-primary' style='margin-left:0px;'>护眼大队</button>" +
							"<button class='btn btn-default' id='removeEye' onclick='shutUps(this)' style='margin-left:8px;'>移除权限</button>" +
							"<button class='btn btn-default' id='updataEye' onclick='shutUps(this)' style='margin-left:3px;'>修改权限</button></div>"
					});
				}
			} 
			if(obj.event == "del"){ //删除举报信息
				$.ajax({
						url: "/gm/removeReportPlayer/" + obj.data.id,
						type: 'get',
						error: function(XMLHttpRequest, textStatus, errorThrown) {
							if(XMLHttpRequest.status == 401) {
								notLogin(XMLHttpRequest, textStatus, errorThrown);
							} else {
								layer.msg(XMLHttpRequest.responseJSON.message);
							}
						},
						success: function() {
							layer.msg('删除成功');
							setTimeout(function() {
								$(".layui-laypage-btn")[0].click();
							}, 500);
						}
					});
			} 
			
			shutUps = function (time){  //执行方法处理
				  if(time.id == "pcustom"){
					  var bannedTime = document.getElementById("duration").value;
					  if(bannedTime >= 5 && bannedTime <= 9999) {
						  executeBanned(bannedTime);
					  } else {
						  layer.msg('请输入5~9999之间的整数');
					  }
				  } else if (time.id == "banned"){
					  bannedAccount();  //封号
				  } else if(time.id == "removeEye"){
				      removeEye(); //移除护眼大队
				  } else if(time.id == "updataEye"){
					  layer.msg("功能开发中....");
				  } else {
					  executeBanned(time);    //禁言
				  }
					
			  }
			  
			  function executeBanned(time){  //禁言
				  $.ajax({
						url: "/gm/setNoSpeak",
						type: 'get',
						data: {
							id:obj.data.id, 
							playerId:obj.data.toplayerId, 
							noSpeakTime:time
						},
						error: function(XMLHttpRequest, textStatus, errorThrown) {
							if(XMLHttpRequest.status == 401) {
								notLogin(XMLHttpRequest, textStatus, errorThrown);
							} else {
								layer.msg(XMLHttpRequest.responseJSON.message);
							}
						},
						success: function() {
							layer.msg('禁言成功');
							setTimeout(function() {
								layer.closeAll();
								$(".layui-laypage-btn")[0].click();
							}, 500);
						}
					});
			  }
			  
			  function bannedAccount() {  //封号
					$.ajax({
						url: "/gm/setBlockPlayer",
						type: 'get',
						data:{
							id:obj.data.id, 
							playerId:obj.data.toplayerId
						},
						error: function(XMLHttpRequest, textStatus, errorThrown) {
							if(XMLHttpRequest.status == 401) {
								notLogin(XMLHttpRequest, textStatus, errorThrown);
							} else {
								layer.msg(XMLHttpRequest.responseJSON.message);
							}
						},
						success: function() {
							layer.msg('封号成功');
							setTimeout(function() {
								layer.closeAll();
								$(".layui-laypage-btn")[0].click();
							}, 500);
						}
					});
			  }
	
			  function removeEye(){  //移除护眼大队
				$.ajax({
					url: "/gm/removeReportPlayerToPlayer",
					type: 'get',
					data:{
						id:obj.data.id, 
						playerId:obj.data.toplayerId
					},
					error: function(XMLHttpRequest, textStatus, errorThrown) {
						if(XMLHttpRequest.status == 401) {
							notLogin(XMLHttpRequest, textStatus, errorThrown);
						} else {
							layer.msg(XMLHttpRequest.responseJSON.message);
						}
					},
					success: function() {
						layer.msg("移除权限成功");
						setTimeout(function() {
							layer.closeAll();
							$(".layui-laypage-btn")[0].click();
						}, 500);
					}
				});
			}
			
		});
		
});
});
function reportPlayerCount() {
	var num = "";
	$.ajax({
		url: "/gm/getReportPlayerCount",
		type: 'get',
		async: false,
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			if (XMLHttpRequest.status == 401) {
				notLogin(XMLHttpRequest, textStatus, errorThrown);
			} else {
				layer.msg(XMLHttpRequest.responseJSON.message);
			}
		},
		success: function(result) {
			num = result;
		}
	});
	return num;
}