$(document).ready(function(){
layui.use('table', function(){
  var table = layui.table
  ,form = layui.form;
  
  illegalRecord(1);
  
  table.render({
    elem: '#titleAudit'
    ,url:'/gm/getTitleAuditPlayer'
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
    		"count":3000,
    		data:res
    	}
    }
    ,cols: [[
      {type:'numbers',title:'编号'}
      ,{field:'roleId',title:'ID', align:'center', minWidth:80}
      ,{field:'nickName',title:'昵称', align:'center', minWidth:140}
      ,{field:'achievement',title:'成就排名', align:'center', minWidth:180}
      ,{field:'clues',title:'探索排名', align:'center', minWidth:140}
      ,{field:'fansCount',title:'粉丝数', align:'center', minWidth:140}
      ,{field:'discussCount',title:'热评数', align:'center', minWidth:140}
      ,{field:'totalLogin',title:'登录天数', align:'center', minWidth:140}
      ,{field:'playerOnline',title:'游戏时长', align:'center', minWidth:140}
      ,{field:'', title:'查看违规记录', align:'center', width:150, templet: '#foulRecord', unresize: true}
    ]]
    ,page:{
    	 layout: ['count', 'prev', 'page', 'next', 'limit', 'refresh', 'skip']
    	 ,limits: [20,50,100] 
    	 ,first: false 
    	 ,last: false 
    }
  });
  
  table.on('tool(titleAudit)', function(obj){
		var count = illegalRecord(obj.data.playerId);
		layui.use('table', function(){
		  var table = layui.table
		  ,form = layui.form;
		  
		  table.render({
		    elem: '#titleAudit'
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
	 });
  
  $('#buTimesr').click(function(page) {
		var times = document.getElementById("inDatacs").value;
		if(times == null || times == "") {
			layer.msg("请选择时间");
			return;
		}
		var m = times.split("-");
		var stime = m[0] + "" + m[1] + "" + m[2];
		var roleId = document.getElementById('inDatacr').value;
		var posPattern = /^[0-9]*[1-9][0-9]*$/;
		
		if(roleId == null || roleId == "" || !posPattern.test(roleId)) {
			$("inDatacr").val("");
			layer.msg('请输入正确的ID');
			return;
		}		

//		$.ajax({
//			url: "/gm/findByIdAndTimeAudit",
//			data: {
//				roleId:roleId,
//				date:stime
//			},
//			type: 'get',
//			error: function(XMLHttpRequest, textStatus, errorThrown) {
//				if(XMLHttpRequest.status == 401) {
//					notLogin(XMLHttpRequest, textStatus, errorThrown);
//				} else {
//					layer.msg(XMLHttpRequest.responseJSON.message);
//				}
//			},
//			success: function(result) {
				layui.use('table', function(){
					  var table = layui.table
					  ,form = layui.form;
						table.render({
						    elem: '#titleAudit'
						    ,url:'/gm/findByIdAndTimeAudit'
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
						      ,{field:'roleId',title:'ID', align:'center', minWidth:80}
						      ,{field:'nickName',title:'昵称', align:'center', minWidth:140}
						      ,{field:'achievement',title:'成就排名', align:'center', minWidth:180}
						      ,{field:'clues',title:'探索排名', align:'center', minWidth:140}
						      ,{field:'fansCount',title:'粉丝数', align:'center', minWidth:140}
						      ,{field:'discussCount',title:'热评数', align:'center', minWidth:140}
						      ,{field:'totalLogin',title:'登录天数', align:'center', minWidth:140}
						      ,{field:'playerOnline',title:'游戏时长', align:'center', minWidth:140}
						      ,{field:'', title:'查看违规记录', align:'center', width:150, templet: '#foulRecord', unresize: true}
						    ]]
						    ,page:false
						  });
						//if(result.roleId == undefined) {
		          		//$(".layui-table-main").html('<div class="layui-none">无数据</div>');
						//}
				});
			//}
	//});
});
  
  $('#buTimes').click(function() {
		var time = document.getElementById("inDatac").value;
		if(time == null || time == "") {
			layer.msg("请选择时间");
			return;
		}
		var m = time.split("-");
		var reqTime = m[0] + "" + m[1] + "" + m[2];

		$.ajax({
			url: "/gm/assignEyeTimeCount/" + reqTime,
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
				    elem: '#titleAudit'
				    ,url:'/gm/getEyeTitleAuditPlayer'
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
				      ,{field:'roleId',title:'ID', align:'center', minWidth:80}
				      ,{field:'nickName',title:'昵称', align:'center', minWidth:140}
				      ,{field:'achievement',title:'成就排名', align:'center', minWidth:180}
				      ,{field:'clues',title:'探索排名', align:'center', minWidth:140}
				      ,{field:'fansCount',title:'粉丝数', align:'center', minWidth:140}
				      ,{field:'discussCount',title:'热评数', align:'center', minWidth:140}
				      ,{field:'totalLogin',title:'登录天数', align:'center', minWidth:140}
				      ,{field:'playerOnline',title:'游戏时长', align:'center', minWidth:140}
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
			var roleId = document.getElementById('inId').value;
			var posPattern = /^[0-9]*[1-9][0-9]*$/;
			if(roleId == null || roleId == "" || !posPattern.test(roleId)) {
				$("#assignData").val("");
				layer.msg('请输入正确的ID');
				return;
			}

			$.ajax({
				url: "/gm/findByIdTitleAuditCount",
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
					    elem: '#titleAudit'
					    ,url:'/gm/findByIdTitleAudit'
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
					      ,{field:'roleId',title:'ID', align:'center', minWidth:80}
					      ,{field:'nickName',title:'昵称', align:'center', minWidth:140}
					      ,{field:'achievement',title:'成就排名', align:'center', minWidth:180}
					      ,{field:'clues',title:'探索排名', align:'center', minWidth:140}
					      ,{field:'fansCount',title:'粉丝数', align:'center', minWidth:140}
					      ,{field:'discussCount',title:'热评数', align:'center', minWidth:140}
					      ,{field:'totalLogin',title:'登录天数', align:'center', minWidth:140}
					      ,{field:'playerOnline',title:'游戏时长', align:'center', minWidth:140}
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
		
});
});
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
