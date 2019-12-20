$(document).ready(function(){
layui.use('table', function(){
  var table = layui.table
  ,form = layui.form;
  
  var count = findDataCount();
  
  table.render({
    elem: '#fansComment'
    ,url:'/gm/fansCommentFindList'
    ,cellMinWidth: 80
    ,limit: 50 
    ,request: {
        pageName: 'page',   // 页码的参数名称，默认：page
        limitName: 'count'   // 每页数据量的参数名，默认：limit
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
      ,{field:'gameName', title:'剧本名', align:'center', width:100, unresize: false, sort: false}
      ,{field:'message',title:'评论内容', align:'center', minWidth:80}
      ,{field:'praiseNum',title:'点赞数', align:'center', width:80}
      ,{field:'createTime',title:'评论时间', align:'center', width:180,templet:function(d){
    	  return datetimeFormat(d.createTime);
      }}
      ,{field:'', title:'处理', align:'center', width:95, templet: '#dispose'}
      ,{field:'', title:'删除', align:'center', width:95, templet: '#delete'}
    ]]
    ,page:{
    	 layout: ['count', 'prev', 'page', 'next', 'limit', 'refresh', 'skip']
    	 ,limits: [20,50,100] 
    	 ,first: false 
    	 ,last: false 
    }
  });
  
  table.on('tool(fansComment)', function(obj){
	  if(obj.event == "dis"){
		  layer.open({
				type: 1,
				title:'处理',
				skin: 'layui-layer-molv', //加上边框
				area: ['910px', '310px'], //宽高
				content: "<br>&nbsp&nbsp<b>玩家  :</b>&nbsp;<b>" + obj.data.fromPlayerNick + "</b><br><br>" +
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
	  } else {
		  deleteComment("删除");
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
				url: "/gm/deleteByIdfansComment/" + obj.data.id,
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

function findDataCount(){
	var num = "";
	$.ajax({
		url: "/gm/fansCommentFindCount",
		type: 'get',
		async:false,
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

});