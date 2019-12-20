$(document).ready(function(){
layui.use('table', function(){
  var table = layui.table
  ,form = layui.form;

  var count = itemTitleCount();
  var refresh = false;

  table.render({
    elem: '#itemTitle'
    ,url:'/gm/getItemTitlePlayer'
    ,cellMinWidth: 80
    ,limit: 50 
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
    ,cols: [[
      {type:'numbers',title:'编号'}
      ,{field:'setTitleTime',title:'加入时间', align:'center', minWidth:170}
      ,{field:'roleId',title:'ID', align:'center', minWidth:80}
      ,{field:'nickName',title:'昵称', align:'center', minWidth:140}
      ,{field:'sex',title:'性别', align:'center', minWidth:80,templet:function(d){
    	  if(d.sex == 0){
    		  return "外星人";
    	  } else if(d.sex == 1) {
    		  return "男";
    	  } else {
    		  return "女";
    	  }
      }}
      ,{field:'fansCount',title:'粉丝数', align:'center', minWidth:140}
      ,{field:'sendDiscussNum',title:'评论数', align:'center', minWidth:140}
      ,{field:'sendGodDiscuss',title:'送神评数', align:'center', minWidth:140}
      ,{field:'totalLogin',title:'登录天数', align:'center', minWidth:140}
      ,{field:'online',title:'游戏时长(小时)', align:'center', minWidth:140}
      ,{field:'', title:'移除', align:'center', width:100, templet: '#rem', unresize: true}
      ,{field:'', title:'查看违规记录', align:'center', width:150, templet: '#foulRecord', unresize: true}
    ]]
    ,page:{
    	 layout: ['count', 'prev', 'page', 'next', 'limit', 'refresh', 'skip']
    	 ,limits: [20,50,100] 
    	 ,first: false 
    	 ,last: false 
    }
  });
  
  table.on('tool(itemTitle)', function(obj){
	  if(obj.event == "rem"){
		  $.ajax({
				url: "/gm/removeProTitle/" + obj.data.playerId,
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
						layer.msg("移除成功");
						$(".layui-laypage-btn")[0].click();
					} else {
						layer.msg("移除失败");
						$(".layui-laypage-btn")[0].click();
					}
				}
			});
	  } else {
		refresh = true; //是否刷新整个页面
		  var count = illegalRecord(obj.data.playerId);
			layui.use('table', function(){
			  var table = layui.table
			  ,form = layui.form;
			  
			  table.render({
			    elem: '#itemTitle'
			    ,url:'/gm/getIllegalRecord' 
			    ,cellMinWidth: 80
			    ,limit: 50 
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
			    	playerId:obj.data.playerId,
			    }
			    ,cols: [[
			      {type:'numbers',title:'编号'}
			      ,{field:'type',title:'违规类型', align:'center', minWidth:80,templet:function(d){
			    	  if(d.type == 1){
			    		  return "封号";
			    	  } else {
			    		  return "禁言";
			    	  }
			      }}
			      ,{field:'create_time',title:'违规时间', align:'center', minWidth:140,templet:function(d){
			    	  return datetimeFormat(d.create_time);
			      }}
			      ,{field:'bannedDuration',title:'封禁时长(分钟)', align:'center', minWidth:180,templet:function(d){
			    	  if(d.bannedDuration == 0){
			    		  return "封号";
			    	  } else {
			    		  return timeFormat(d.bannedDuration);
			    	  }
			      }}
			    ]]
			    ,page:{
			    	 layout: ['count', 'prev', 'page', 'next', 'limit', 'refresh', 'skip']
			    	 ,limits: [20,50,100] 
			    	 ,first: false 
			    	 ,last: false 
			    }
			  });
			});
	  }
	 });
  
  $('#buIdAndTime').click(function(page) {
		var times = document.getElementById("inDataAndId").value;
		if(times == null || times == "") {
			layer.msg("请选择时间");
			return;
		}
		var m = times.split("-");
		var stime = m[0] + "" + m[1] + "" + m[2];
		var roleId = document.getElementById('inIdAndTime').value;
		var posPattern = /^[0-9]*[1-9][0-9]*$/;
		
		if(roleId == null || roleId == "" || !posPattern.test(roleId)) {
			$("inIdAndTime").val("");
			layer.msg('请输入正确的ID');
			return;
		}		
		
		refresh = true; //是否刷新整个页面
		
		layui.use('table', function(){
			  var table = layui.table
			  ,form = layui.form;
				table.render({
				    elem: '#itemTitle'
				    ,url:'/gm/findByIdAndTimeTitle'
				    ,cellMinWidth: 80
				    ,limit: 50 
				    ,parseData:function(res){
				    	if(res.playerId == null){
				    		return{
					    		"code":200,
					    		"msg":"无数据",
					    		data:res
					    	}
				    	} else {
				    		return{
					    		"code":0,
					    		"msg":"",
					    		data:res
					    	}
				    	}
				    }
				    ,request: {
				        pageName: 'page',   // 页码的参数名称，默认：page
				        limitName: 'count' ,// 每页数据量的参数名，默认：limit
				    }
				    ,where:{
				    	roleId:roleId,
				    	date:stime,
				    }
				    ,cols: [[
				        {type:'numbers',title:'编号'}
				        ,{field:'setTitleTime',title:'加入时间', align:'center', minWidth:170}
				        ,{field:'roleId',title:'ID', align:'center', minWidth:80}
				        ,{field:'nickName',title:'昵称', align:'center', minWidth:140}
				        ,{field:'achievement',title:'性别', align:'center', minWidth:80,templet:function(d){
				      	  if(d.sex == 0){
				      		  return "外星人";
				      	  } else if(d.sex == 1) {
				      		  return "男";
				      	  } else {
				      		  return "女";
				      	  }
				        }}
				        ,{field:'fansCount',title:'粉丝数', align:'center', minWidth:140}
				        ,{field:'sendDiscussNum',title:'评论数', align:'center', minWidth:140}
				        ,{field:'sendGodDiscuss',title:'送神评数', align:'center', minWidth:140}
				        ,{field:'totalLogin',title:'登录天数', align:'center', minWidth:140}
				        ,{field:'online',title:'游戏时长(小时)', align:'center', minWidth:140}
				        ,{field:'', title:'移除', align:'center', width:100, templet: '#rem', unresize: true}
				        ,{field:'', title:'查看违规记录', align:'center', width:150, templet: '#foulRecord', unresize: true}
				      ]]
				    ,page:false
				  });
				});
});
  
  $('#buTime').click(function() {
		var time = document.getElementById("inDatas").value;
		if(time == null || time == "") {
			layer.msg("请选择时间");
			return;
		}
		var m = time.split("-");
		var reqTime = m[0] + "" + m[1] + "" + m[2];

		refresh = true; //是否刷新整个页面

		$.ajax({
			url: "/gm/getByTimeAuditCount/" + reqTime,
			type: 'get',
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				if(XMLHttpRequest.status == 401) {
					notLogin(XMLHttpRequest, textStatus, errorThrown);
				} else {
					layer.msg(XMLHttpRequest.responseJSON.message);
				}
			},
			success: function(result) {
				table.render({
				    elem: '#itemTitle'
				    ,url:'/gm/getByTimeAuditPlayer'
				    ,cellMinWidth: 80
				    ,limit: 50 
				    ,request: {
				        pageName: 'page',   // 页码的参数名称，默认：page
				        limitName: 'count' ,// 每页数据量的参数名，默认：limit
				    }
					,where:{
				    	time:reqTime,
				    }
				    ,parseData:function(res){
				    	return{
				    		"code":0,
				    		"msg":"",
				    		"count":result,
				    		data:res
				    	}
				    }
				    ,cols: [[
				        {type:'numbers',title:'编号'}
				        ,{field:'setTitleTime',title:'加入时间', align:'center', minWidth:170}
				        ,{field:'roleId',title:'ID', align:'center', minWidth:80}
				        ,{field:'nickName',title:'昵称', align:'center', minWidth:140}
				        ,{field:'achievement',title:'性别', align:'center', minWidth:80,templet:function(d){
				      	  if(d.sex == 0){
				      		  return "外星人";
				      	  } else if(d.sex == 1) {
				      		  return "男";
				      	  } else {
				      		  return "女";
				      	  }
				        }}
				        ,{field:'fansCount',title:'粉丝数', align:'center', minWidth:140}
				        ,{field:'sendDiscussNum',title:'评论数', align:'center', minWidth:140}
				        ,{field:'sendGodDiscuss',title:'送神评数', align:'center', minWidth:140}
				        ,{field:'totalLogin',title:'登录天数', align:'center', minWidth:140}
				        ,{field:'online',title:'游戏时长(小时)', align:'center', minWidth:140}
				        ,{field:'', title:'移除', align:'center', width:100, templet: '#rem', unresize: true}
				        ,{field:'', title:'查看违规记录', align:'center', width:150, templet: '#foulRecord', unresize: true}
				      ]]
				    ,page:{
				    	 layout: ['count', 'prev', 'page', 'next', 'limit', 'refresh', 'skip']
				    	 ,limits: [20,50,100] 
				    	 ,first: false 
				    	 ,last: false 
				    }
				  });
			}
		});
  });
		
		$('#buId').click(function() {
			var roleId = document.getElementById('inIds').value;
			var posPattern = /^[0-9]*[1-9][0-9]*$/;
			if(roleId == null || roleId == "" || !posPattern.test(roleId)) {
				$("#inIds").val("");
				layer.msg('请输入正确的ID');
				return;
			}

			refresh = true; //是否刷新整个页面

			$.ajax({
				url: "/gm/getByIdAuditPlayerCount",
				data:{
					roleId:roleId
				},
				type: 'get',
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					if(XMLHttpRequest.status == 401) {
						notLogin(XMLHttpRequest, textStatus, errorThrown);
					} else {
						layer.msg(XMLHttpRequest.responseJSON.message);
					}
				},
				success: function(result) {
					table.render({
					    elem: '#itemTitle'
					    ,url:'/gm/getByIdAuditPlayer'
					    ,cellMinWidth: 80
					    ,limit: 50 
					    ,request: {
					        pageName: 'page',   // 页码的参数名称，默认：page
					        limitName: 'count' ,// 每页数据量的参数名，默认：limit
					    }
						,where:{
							roleId:roleId,
					    }
					    ,parseData:function(res){
					    	return{
					    		"code":0,
					    		"msg":"",
					    		"count":result,
					    		data:res
					    	}
					    }
					    ,cols: [[
					        {type:'numbers',title:'编号'}
					        ,{field:'setTitleTime',title:'加入时间', align:'center', minWidth:170}
					        ,{field:'roleId',title:'ID', align:'center', minWidth:80}
					        ,{field:'nickName',title:'昵称', align:'center', minWidth:140}
					        ,{field:'achievement',title:'性别', align:'center', minWidth:80,templet:function(d){
					      	  if(d.sex == 0){
					      		  return "外星人";
					      	  } else if(d.sex == 1) {
					      		  return "男";
					      	  } else {
					      		  return "女";
					      	  }
					        }}
					        ,{field:'fansCount',title:'粉丝数', align:'center', minWidth:140}
					        ,{field:'sendDiscussNum',title:'评论数', align:'center', minWidth:140}
					        ,{field:'sendGodDiscuss',title:'送神评数', align:'center', minWidth:140}
					        ,{field:'totalLogin',title:'登录天数', align:'center', minWidth:140}
					        ,{field:'online',title:'游戏时长(小时)', align:'center', minWidth:140}
					        ,{field:'', title:'移除', align:'center', width:100, templet: '#rem', unresize: true}
					        ,{field:'', title:'查看违规记录', align:'center', width:150, templet: '#foulRecord', unresize: true}
					      ]]
					    ,page:{
					    	 layout: ['count', 'prev', 'page', 'next', 'limit', 'refresh', 'skip']
					    	 ,limits: [20,50,100] 
					    	 ,first: false 
					    	 ,last: false 
					    }
					  });
				}
		});
	});
		
		$('#byIdItemTitle').click(function(page) {
			var roleId = document.getElementById('assignData').value;
			var posPattern = /^[0-9]*[1-9][0-9]*$/;
			if(roleId == null || roleId == "" || !posPattern.test(roleId)) {
				$("#assignData").val("");
				layer.msg('请输入正确的ID');
				return;
			}
			$.ajax({
				url: "/gm/setByIdItemTitle/" + roleId + "",
				type: 'get',
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					if(XMLHttpRequest.status == 401) {
						notLogin(XMLHttpRequest, textStatus, errorThrown);
					} else {
						layer.msg(XMLHttpRequest.responseJSON.message);
					}
				},
				success: function(result) {
					if(result == 0) {
						layer.msg("该玩家己经加入");
					} else if(result == 1) {
						if(refresh){
							layer.msg("设置成功");  //已经跳转二级页面 返回首页面加载数据
							setTimeout(function() {
								window.location.reload();
							}, 500);
						} else {
							layer.msg("设置成功");
							$(".layui-laypage-btn")[0].click();
						}
					} else {
						layer.msg("没有找到该玩家");
					}
				}
			});
		});

		$('#byPhoneItemTitle').click(function(page) {
			var phone = document.getElementById('assignData').value;
			var posPattern = /^[1][3,4,5,6,7,8][0-9]{9}$/;
			if(phone == null || phone == "" || !posPattern.test(phone)) {
				$("#assignData").val("");
				layer.msg('请输入正确的手机号码');
				return;
			}
			$.ajax({
				url: "/gm/setByPhoneItemTitle/" + phone + "",
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
					if(result == 0) {
						layer.msg("该玩家己经加入");
					} else if(result == 1) {
						if(refresh){
							layer.msg("设置成功");  //已经跳转二级页面 返回首页面加载数据
							setTimeout(function() {
								window.location.reload();
							}, 500);
						} else {
							layer.msg("设置成功");
							$(".layui-laypage-btn")[0].click();
						}
					} else {
						layer.msg("没有找到此手机号码玩家");
					}
				}
			});
		});
		
});

});
function itemTitleCount(){
	var num = "";
	$.ajax({
		url: "/gm/getItemTitleCount",
		type: 'get',
		async: false,
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			if(XMLHttpRequest.status == 401) {
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

function illegalRecord(playerId){
	var num = "";
	$.ajax({
		url: "/gm/getIllegalRecordCount",
		type: 'get',
		data:{
			playerId:playerId
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
			num = result;
		}
  	});
	return num;
}

