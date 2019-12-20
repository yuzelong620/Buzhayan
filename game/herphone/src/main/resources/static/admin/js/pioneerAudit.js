$(document).ready(function() {
	layui.use('table', function() {
		var table = layui.table
			, form = layui.form;

		var count = pioneerAuditCount();

		table.render({
			elem: '#pioneerAudit'
			 ,url:'/gm/pioneerAuditAll'
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
					field: 'type', title: '所属范围', align: 'center', width: 150, templet: function(d) {
						if (d.type == 0) {
							return "剧本评论";
						}
						if (d.type == 1) {
							return "广场评论";
						}
						if (d.type == 2) {
							return "粉圈评论";
						}
					}
				}
				, {
					field: 'content', title: '被处理内容', align: 'center', minWidth: 80, templet: function(d) {
						if (d.type == 0 || d.type == 2) {
							return d.content;
						}
						if (d.type == 1) {
							var str = "";
							for (var i = 0; i < d.content.length; i++) {
								if (d.content[i].type == 1) {
									str += "<div>文字内容[  <font color='#FD7E3D'>" + d.content[i].content + " </font>]</div>"
								}
								if (d.content[i].type == 2) {
									str += "<img class='img-rounded' src=" + d.content[i].content + ">";
								}
								if (d.content[i].type == 3) {
									str += "<video width='150' height='90'  controls>"
									str += "<source src=" + d.content[i].content + " type='video/mp4'></video>"
								}
							}
							return str;
						}
					}
				}
				, { field: 'reportPlayer', title: '举报人', align: 'center', width: 150 }
				, { field: 'dealPlayer1', title: '投票人①', align: 'center', width: 150 }
				, { field: 'dealPlayer2', title: '投票人②', align: 'center', width: 150 }
				, { field: 'creatTime', title: '生效时间', align: 'center', width: 150 }
				, { field: '', title: '处理', align: 'center', width: 100, templet: '#dispose', unresize: true }
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
		var playerId = "";
		table.on('tool(pioneerAudit)', function(obj) {
			  $.ajax({
				url: "/gm/getByDiscussIdAndTypeData",
				type: 'get',
				data: {
					discussId: obj.data.discussId,
					type:obj.data.type
				},
				async: false,
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					if(XMLHttpRequest.status == 401) {
						notLogin(XMLHttpRequest, textStatus, errorThrown);
					} else {
						layer.msg(XMLHttpRequest.responseJSON.message);
					}
				},
				success: function(result) {
					playerId = result.playerId;
					layer.open({
						type: 1,
						title:'处理',
						skin: 'layui-layer-molv', //加上边框
						area: ['910px', '240px'], //宽高
						content:"<br>&nbsp&nbsp<b>操作 :</b><button class='btn btn-danger' id='delectShelve' onclick='shutUps(this)' style='margin-left:5px;'>删除评论</button><br><br>" +
							"<button class='btn btn-danger' style='margin-left:48px;' onclick='shutUps(this)' id='banned'>封号并删除评论</button><br><br>" +
							"<button class='btn btn-danger' style='margin-left:48px;'>禁言并删除评论</button><b> : </b>" +
							"<button class='btn btn-default' onclick='shutUps(300000)' style='margin-left:0px;'>5分钟</button>" +
							"<button class='btn btn-default' onclick='shutUps(1800000)' style='margin-left:3px;'>30分钟</button>" +
							"<button class='btn btn-default' onclick='shutUps(7200000)' style='margin-left:3px;'>2小时</button>" +
							"<button class='btn btn-default' onclick='shutUps(86400000)' style='margin-left:3px;'>1天</button>" +
							"<button class='btn btn-default' onclick='shutUps(604800000)' style='margin-left:3px;'>7天</button>" +
							"<button class='btn btn-default' onclick='shutUps(2592000000)' style='margin-left:3px;'>30天</button>" +
							"<div class='input-group' style='width:200px;position: absolute;top:128px;' id='customIn'><input type='text' id='duration' class='form-control' placeholder='区间5分钟~9999分钟'></div>" +
							"<button id='pcustom' class='btn btn-warning' onclick='shutUps(this)' 2592000000 >自定义时长禁言</button>"
					});
				  }
			  });
		});
		
		shutUps = function (time){  //执行方法处理
		  if(time.id == "pcustom"){
			  var bannedTime = document.getElementById("duration").value;
			  if(bannedTime >= 5 && bannedTime <= 9999) {
				  executeBanned(bannedTime);
			  } else {
				  layer.msg('请输入5~9999之间的整数');
			  }
		  } else if (time.id == "delectShelve"){
			  deleteComment("删除");
		  } else if (time.id == "banned"){
			  bannedAccount();
		  } else {
			  executeBanned(time);
		  }
			
	  }
	  
	  function executeBanned(time){  //禁言
		  $.ajax({
				url: "/gm/setByIdNoSpeak",
				type: 'get',
				dataType: "json",
				data: {
					playerId: playerId,
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
				url: "/gm/getisblock/" + playerId,
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
				url: "/gm/deleteByIdfansComment/" + obj.data.discussId,
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
			
	});
});
function pioneerAuditCount() {
	var num = "";
	$.ajax({
		url: "/gm/pioneerReportCount",
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
		img_show = layer.tips(img,this, {
			tips: [2, 'rgba(60,60,60,.5)']
			,time: 100000
			, area: ['330px']
		});
	}, function() {
		layer.close(img_show);
	});
	$('td img').attr('style', 'max-width:70px');
}