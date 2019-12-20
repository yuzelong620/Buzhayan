$(document).ready(function(){
layui.use('table', function(){
  var table = layui.table
  ,form = layui.form;
  
  var count = pioneerCount();
  var refresh = false;

  table.render({
    elem: '#eyePioneerList'
    ,url:'/gm/pioneerList'
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
      ,{field:'creatTime',title:'加入时间', align:'center', minWidth:100,templet:function(d){
			return datetimeFormat(d.creatTime);
      }}
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
      ,{field:'dealNum',title:'治理次数', align:'center', minWidth:140}
      ,{field:'succcessNum',title:'成功次数', align:'center', minWidth:140}
      ,{field:'isEyeshield',title:'是否护眼', align:'center', minWidth:60,templet:function(d){
			if(d.isEyeshield == 0){
				return "✖";
			} else {
				return "✔";
			}
	  }}
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
  
  table.on('tool(eyePioneerList)', function(obj){
	  if(obj.event == "rem"){
		  $.ajax({
				url: "/gm/relievePioneerById",
				type: 'get',
				data:{
					playerId:obj.data.playerId
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					if(XMLHttpRequest.status == 401) {
						notLogin(XMLHttpRequest, textStatus, errorThrown);
					} else {
						layer.msg(XMLHttpRequest.responseJSON.message);
					}
				},
				success: function(result) {
					layer.msg("解除成功");
					$(".layui-laypage-btn")[0].click();
				}
			});
	  } else {
		refresh = true; //是否刷新整个页面
		  var count = illegalRecord(obj.data.playerId);
			layui.use('table', function(){
			  var table = layui.table
			  ,form = layui.form;
			  
			  table.render({
			    elem: '#eyePioneerList'
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
  
  $('#buIdAndTime').click(function() {
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
		$(".layui-table-page").show();  //显示分页
		refresh = true; //是否刷新整个页面
		$.ajax({
			url: "/gm/getStatisticalPioneer",
			type: 'get',
			data:{
				roleId:roleId,
				date:stime
			},
			async: false,
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				if(XMLHttpRequest.status == 401) {
					notLogin(XMLHttpRequest, textStatus, errorThrown);
				} else {
					$(".layui-table-main").html('<div class="layui-none">'+ XMLHttpRequest.responseJSON.message +'</div>');  //表格内提示语处理
					$(".layui-table-page").hide();  //隐藏分页
					//$(".layui-table-page").remove();
					//layer.msg(XMLHttpRequest.responseJSON.message);
				}
			},
			success: function(result) {
				layui.use('table', function(){
				  var table = layui.table
				  ,form = layui.form;
					table.render({
					    elem: '#eyePioneerList'
					    ,url:'/gm/getStatisticalPioneer'
					    ,cellMinWidth: 80
					    ,limit: 50 
						,data:result
					    ,cols: [[
					        {type:'numbers',title:'编号'}
					        ,{field:'setTitleTime',title:'加入时间', align:'center', minWidth:80}
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
			}
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
		
		$(".layui-table-page").show();  //显示分页
		refresh = true; //是否刷新整个页面
		
		$.ajax({
			url: "/gm/getByTimePioneerCount",
			type: 'get',
			data:{
				date:reqTime
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				if(XMLHttpRequest.status == 401) {
					notLogin(XMLHttpRequest, textStatus, errorThrown);
				} else {
					layer.msg(XMLHttpRequest.responseJSON.message);
				}
			},
			success: function(result) {
				table.render({
				    elem: '#eyePioneerList'
				    ,url:'/gm/getPioneerBytimeAll'
				    ,cellMinWidth: 80
				    ,limit: 50 
				    ,request: {
				        pageName: 'page',   // 页码的参数名称，默认：page
				        limitName: 'count' ,// 每页数据量的参数名，默认：limit
				    }
					,where:{
				    	date:reqTime,
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
				        ,{field:'setTitleTime',title:'加入时间', align:'center', minWidth:80}
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
			
			$(".layui-table-page").show();  //显示分页
			refresh = true; //是否刷新整个页面
			
			$.ajax({
				url: "/gm/getPioneerByRoleId",
				type: 'get',
				data:{
					id:roleId
				},
				async: false,
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					if(XMLHttpRequest.status == 401) {
						notLogin(XMLHttpRequest, textStatus, errorThrown);
					} else {
						$(".layui-table-main").html('<div class="layui-none">'+ XMLHttpRequest.responseJSON.message +'</div>');  //表格内提示语处理
						$(".layui-table-page").hide(); //table 上级页面分页缓存无法去除 通过标签属性隐藏
						//layer.msg(XMLHttpRequest.responseJSON.message);
					}
				},
				success: function(result) {
					table.render({
				    elem: '#eyePioneerList'
				    ,cellMinWidth: 80
				    ,limit: 50 
					,data:[result]  //无法解析对象返回结果  转为数组格式json数据
				    ,cols: [[
					      {type:'numbers',title:'编号'}
					      ,{field:'creatTime',title:'加入时间', align:'center', minWidth:100,templet:function(d){
								return datetimeFormat(d.creatTime);
					      }}
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
					      ,{field:'dealNum',title:'治理次数', align:'center', minWidth:140}
					      ,{field:'succcessNum',title:'成功次数', align:'center', minWidth:140}
					      ,{field:'isEyeshield',title:'是否护眼', align:'center', minWidth:60,templet:function(d){
								if(d.isEyeshield == 0){
									return "✖";
								} else {
									return "✔";
								}
						  }}
					      ,{field:'', title:'移除', align:'center', width:100, templet: '#rem', unresize: true}
					      ,{field:'', title:'查看违规记录', align:'center', width:150, templet: '#foulRecord', unresize: true}
					    ]]
			          ,page:false
			 		 });
				 }
		  	});
	});
		
		$('#byIdItemTitle').click(function() {
			var roleId = document.getElementById('assignData').value;
			var posPattern = /^[0-9]*[1-9][0-9]*$/;
			if(roleId == null || roleId == "" || !posPattern.test(roleId)) {
				$("#assignData").val("");
				layer.msg('请输入正确的ID');
				return;
			}
			$.ajax({
				url: "/gm/setByIdPioneer",
				type: 'get',
				data:{
					roleId:roleId
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					if(XMLHttpRequest.status == 401) {
						notLogin(XMLHttpRequest, textStatus, errorThrown);
					} else {
						layer.msg(XMLHttpRequest.responseJSON.message);
					}
				},
				success: function() {
					if(refresh){
						layer.msg("设置成功");  //已经跳转二级页面 返回首页面加载数据
						setTimeout(function() {
							window.location.reload();
						}, 500);
					} else {
						layer.msg("设置成功");
						$(".layui-laypage-btn")[0].click();
					}
				}
			});
		});

		$('#byPhoneItemTitle').click(function() {
			var phone = document.getElementById('assignData').value;
			var posPattern = /^[1][3,4,5,6,7,8][0-9]{9}$/;
			if(phone == null || phone == "" || !posPattern.test(phone)) {
				$("#assignData").val("");
				layer.msg('请输入正确的手机号码');
				return;
			}
			$.ajax({
				url: "/gm/setByPhonePioneer",
				type: 'get',
				data:{
					phone:phone
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					if(XMLHttpRequest.status == 401) {
						notLogin(XMLHttpRequest, textStatus, errorThrown);
					} else {
						layer.msg(XMLHttpRequest.responseJSON.message);
					}
				},
				success: function() {
					if(refresh){
						layer.msg("设置成功");  //已经跳转二级页面 返回首页面加载数据
						setTimeout(function() {
							window.location.reload();
						}, 500);
					} else {
						layer.msg("设置成功");
						$(".layui-laypage-btn")[0].click();
					}
				}
			});
		});
		
});

});
function pioneerCount(){
	var num = "";
	$.ajax({
		url: "/gm/pioneerCount",
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

