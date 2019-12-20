$(document).ready(function() {
	layui.use('table', function() {
		var table = layui.table
			, form = layui.form;

		var count = dynamicCount();

		table.render({
			elem: '#dynamicComment'
			 ,url:'/gm/findDynamicAll'
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
			, cols: [[
				{ type: 'numbers', title: '编号' }
				, {
					field: 'resList', title: '评论内容', align: 'center', minWidth: 80, templet: function(d) {
						var str = "";
						for(var i = 0;i < d.resList.length;i++){
							if(d.resList[i].type == 1){
								str += "<div>文字内容[  <font color='#FD7E3D'>" + d.resList[i].content + " </font>]</div>"
							}
							if(d.resList[i].type == 2){
								str += "<div style='max-height:100px;max-width:100px; float：left;'><img src=" + d.resList[i].content + "></div>";
							}
							if(d.resList[i].type == 3){
								str += "<video width='150' height='90'  controls>"
								str += "<source src=" + d.resList[i].content + " type='video/mp4'></video>"
							}
						}
						return str;
					}
				}
				, { field: 'praiseNum', title: '点赞数', align: 'center', width: 100 }
				, { field: 'replieNum', title: '评论数', align: 'center', width: 100 }
				, { field: 'itemTitle', title: '护眼大队', align: 'center', width: 100 ,templet:function(d){
					if(d.itemTitle == null || d.itemTitle == "") {
						return "✖";
					} else {
						for(var i = 0; i < d.itemTitle.length; i++) {
							if(d.itemTitle[i] == 100001) {
								return "✔";
							} else {
								return "✖";
							}
						}
					}
				}}
				, { field: 'createTime', title: '评论时间', align: 'center', width: 130 ,templet:function(d){
					return datetimeFormat(d.createTime);
				}}
				, { field: '', title: '神评', align: 'center', width: 120, templet: '#switchTpl', unresize: true }
				, { field: '', title: '处理', align: 'center', width: 100, templet: '#dispose', unresize: true }
				, { field: '', title: '删除', align: 'center', width: 100, templet: '#delete', unresize: true }
			]]
			, page: {
				layout: ['count', 'prev', 'page', 'next', 'limit', 'refresh', 'skip']
				, limits: [20, 50, 100]
				, first: false
				, last: false
			}
			, done: function(res, curr, count) {
				hoverOpenImg();//显示大图
			}
		});
		
		table.on('tool(dynamicComment)', function(obj) {
			if(obj.event == "dis"){//处理
				//var atNumber = $(obj.tr).data('index')+1; //获取当前表格编号+1
				var itemTitle = false;
				if(!obj.data.itemTitle == null){
					for(var i = 0;i < obj.data.itemTitle.length;i++){
						if(obj.data.itemTitle[i] == 100001){
							itemTitle = true;
						}
					}
				} 
				if(itemTitle){
					layer.open({   //是护眼大队
						type: 1,
						title:'处理',
						skin: 'layui-layer-molv', //加上边框
						area: ['910px', '340px'], //宽高                                                        
						content:"<br>&nbsp&nbsp<b>玩家  :</b>&nbsp;<b>" + obj.data.fromNickName + "</b><br><br>" +
							"&nbsp&nbsp<b>操作 :</b><button class='btn btn-danger' id='delectShelve' onclick='shutUps(this)' style='margin-left:5px;'>删除评论</button><br><br>" +
							"<button class='btn btn-danger' style='margin-left:48px;' onclick='shutUps(this)' id='banned'>封号并删除评论</button><br><br>" +
							"<button class='btn btn-danger' style='margin-left:48px;'>禁言并删除评论</button><b> : </b>" +
							"<button class='btn btn-default' onclick='shutUps(300000)' style='margin-left:0px;'>5分钟</button>" +
							"<button class='btn btn-default' onclick='shutUps(1800000)' style='margin-left:3px;'>30分钟</button>" +
							"<button class='btn btn-default' onclick='shutUps(7200000)' style='margin-left:3px;'>2小时</button>" +
							"<button class='btn btn-default' onclick='shutUps(86400000)' style='margin-left:3px;'>1天</button>" +
							"<button class='btn btn-default' onclick='shutUps(604800000)' style='margin-left:3px;'>7天</button>" +
							"<button class='btn btn-default' onclick='shutUps(2592000000)' style='margin-left:3px;'>30天</button>" +
							"<div class='input-group' style='width:200px;' id='customIn'><input type='text' id='duration' class='form-control' placeholder='区间5分钟~9999分钟'></div>" +
							"<button id='pcustom' class='btn btn-warning' onclick='shutUps(this)'>自定义时长禁言</button>"+
							"<br><br><div><button class='btn btn-danger' style='margin-left:48px;'>身份</button><b> : </b>" +
							"<button class='btn btn-primary' style='margin-left:0px;'>护眼大队</button>" +
							"<button class='btn btn-default' id='removeEye' onclick='shutUps(this)' style='margin-left:8px;'>移除权限</button>" +
							"<button class='btn btn-default' id='updataEye' onclick='shutUps(this)' style='margin-left:3px;'>修改权限</button></div>"
					});
				} else {
					layer.open({    //不是护眼大队
						type: 1,
						title:'处理',
						skin: 'layui-layer-molv', //加上边框
						area: ['910px', '290px'], //宽高                                                        
						content:"<br>&nbsp&nbsp<b>玩家  :</b>&nbsp;<b>" + obj.data.fromNickName + "</b><br><br>" +
							"&nbsp&nbsp<b>操作 :</b><button class='btn btn-danger' id='delectShelve' onclick='shutUps(this)' style='margin-left:5px;'>删除评论</button><br><br>" +
							"<button class='btn btn-danger' style='margin-left:48px;' onclick='shutUps(this)' id='banned'>封号并删除评论</button><br><br>" +
							"<button class='btn btn-danger' style='margin-left:48px;'>禁言并删除评论</button><b> : </b>" +
							"<button class='btn btn-default' onclick='shutUps(300000)' style='margin-left:0px;'>5分钟</button>" +
							"<button class='btn btn-default' onclick='shutUps(1800000)' style='margin-left:3px;'>30分钟</button>" +
							"<button class='btn btn-default' onclick='shutUps(7200000)' style='margin-left:3px;'>2小时</button>" +
							"<button class='btn btn-default' onclick='shutUps(86400000)' style='margin-left:3px;'>1天</button>" +
							"<button class='btn btn-default' onclick='shutUps(604800000)' style='margin-left:3px;'>7天</button>" +
							"<button class='btn btn-default' onclick='shutUps(2592000000)' style='margin-left:3px;'>30天</button>" +
							"<div class='input-group' style='width:200px;' id='customIn'><input type='text' id='duration' class='form-control' placeholder='区间5分钟~9999分钟'></div>" +
							"<button id='pcustom' class='btn btn-warning' onclick='shutUps(this)'>自定义时长禁言</button>"
					});
				}
			} 
			if(obj.event == "del"){
				//删除
				deleteComment("删除"); //删除评论
			}
			
			shutUps = function (time){  //执行方法处理
				  if(time.id == "pcustom"){
					  var bannedTime = document.getElementById("duration").value;
					  if(bannedTime >= 5 && bannedTime <= 9999) {
						  executeBanned(bannedTime);
					  } else {
						  layer.msg('请输入5~9999之间的整数');
					  }
				  } else if (time.id == "delectShelve"){
					  deleteComment("删除");  //删除评论
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
						url: "/gm/setByIdNoSpeak",
						type: 'get',
						dataType: "json",
						data: {
							playerId: obj.data.fromPlayerId,
							time: time
						},
						error: function(XMLHttpRequest, textStatus, errorThrown) {
							if(XMLHttpRequest.status == 401) {
								notLogin(XMLHttpRequest, textStatus, errorThrown);
							} else {
								layer.msg(XMLHttpRequest.responseJSON.message);
							}
						},
						success: function(result) {
							if(result) {
								deleteComment("禁言");
							} else {
								layer.msg('禁言失败');
							}
						}
					});
			  }
			  
			  function bannedAccount() {  //封号
					$.ajax({
						url: "/gm/getisblock/" + obj.data.fromPlayerId,
						type: 'get',
						dataType: "json",
						error: function(XMLHttpRequest, textStatus, errorThrown) {
							if(XMLHttpRequest.status == 401) {
								notLogin(XMLHttpRequest, textStatus, errorThrown);
							} else {
								layer.msg(XMLHttpRequest.responseJSON.message);
							}
						},
						success: function(result) {
							if(result) {
								deleteComment("封号");
							} else {
								layer.msg('封号失败');
							}
						}
					});
			  }
		  
			  function deleteComment(message){  //删除评论
				  $.ajax({
						url: "/gm/deleteByIdDynamic/" + obj.data.id,
						type: 'get',
						dataType: "json",
						error: function(XMLHttpRequest, textStatus, errorThrown) {
							if(XMLHttpRequest.status == 401) {
								notLogin(XMLHttpRequest, textStatus, errorThrown);
							} else {
								layer.msg(XMLHttpRequest.responseJSON.message);
							}
						},
						success: function(result) {
							if(result) {
								layer.msg(message + '成功');
								setTimeout(function() {
									layer.closeAll();
									$(".layui-laypage-btn")[0].click();
								}, 500);
							} else {
								layer.msg(message + '失败');
							}
						}
				  });
			  }
	
			  function removeEye(){  //移除护眼大队
				$.ajax({
					url: "/gm/removeProTitle/" + obj.data.fromPlayerId,
					type: 'get',
					dataType: "json",
					error: function(XMLHttpRequest, textStatus, errorThrown) {
						if(XMLHttpRequest.status == 401) {
							notLogin(XMLHttpRequest, textStatus, errorThrown);
						} else {
							layer.msg(XMLHttpRequest.responseJSON.message);
						}
					},
					success: function(result) {
						if(result) {
							layer.msg("移除权限成功");
							setTimeout(function() {
								layer.closeAll();
								$(".layui-laypage-btn")[0].click();
							}, 500);
						} else {
							layer.msg('移除权限失败');
						}
					}
				});
			}
			
		});
		
		form.on('switch(sexDemo)', function(obj) {
			if(obj.elem.checked == false) {
				$.ajax({
					url: "/gm/removeByIdDynamicIsBest",
					type: 'get',
					data:{
						dynamicId:this.value
					},
					error: function(XMLHttpRequest, textStatus, errorThrown) {
						if(XMLHttpRequest.status == 401) {
							notLogin(XMLHttpRequest, textStatus, errorThrown);
						} else {
							layer.msg(XMLHttpRequest.responseJSON.message);
						}
					},
					success: function(result) {
						if(result == 1) {
							layer.msg("取消成功");
						} else {
							layer.msg("取消失败");
						}
					}
				});
			} else {
				$.ajax({
					url: "/gm/setByIdDynamicIsBest",
					type: 'get',
					data:{
						dynamicId:this.value
					},
					error: function(XMLHttpRequest, textStatus, errorThrown) {
						if(XMLHttpRequest.status == 401) {
							notLogin(XMLHttpRequest, textStatus, errorThrown);
						} else {
							layer.msg(XMLHttpRequest.responseJSON.message);
						}
					},
					success: function(result) {
						if(result == 1) {
							layer.msg("设置成功");
						} else {
							layer.msg("设置失败");
						}
					}
				});
			}
		});
		
			
	});
});
function dynamicCount() {
	var num = "";
	$.ajax({
		url: "/gm/findDynamicCount",
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
function hoverOpenImg() {
	var img_show = null;
	$('td img').hover(function() {
		var img = "<img class='img_msg' src='" + $(this).attr('src') + "' style='width:300px;' />";
		img_show = layer.tips(img, this, {
			tips: [2, 'rgba(60,60,60,.9)']
			,time: 100000
			, area: ['330px']
		});
	}, function() {
		layer.close(img_show);
	});
	$('td img').attr('style', 'max-width:70px');
}