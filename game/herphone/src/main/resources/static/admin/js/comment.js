$(document).ready(function(){
layui.use('table', function(){
  var table = layui.table
  ,form = layui.form;
  
  var count = findDataCount();
  
  table.render({
    elem: '#comment'
    ,url:'/gm/getDiscussAll'
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
      ,{field:'scenario', title:'剧本名', align:'center', width:100, unresize: false, sort: false}
      ,{field:'c_id', title:'C_ID', align:'center', width:80}
      ,{field:'content', align:'center', minWidth:80,  title:'评论内容'}
      ,{field:'praiseNum', title: '点赞数', align:'center', width:100, sort: true}
      ,{field:'replieNum', title: '回复数', align:'center', width:100, sort: true}
      ,{field:'fromRoleId', title: '玩家ID', align:'center', width:130, sort: false}
      ,{field:'fromNickName', title: '评论玩家', align:'center', width:180, sort: false}
      ,{field:'timeStamp', title: '评论时间', align:'center', width:180, sort: false}
      ,{field:'isBest', title:'神评', align:'center', width:120, templet: '#switchTpl', unresize: false}
      ,{field:'replieNum', title:'删除', align:'center', width:110, templet: '#checkboxTpl', unresize: true}
    ]]
    ,page:{
    	 layout: ['count', 'prev', 'page', 'next', 'limit', 'refresh', 'skip']
    	 ,limits: [20,50,100] 
    	 ,first: false 
    	 ,last: false 
    }
  });
  
	form.on('switch(sexDemo)', function(obj) {
		if(obj.elem.checked == false) {
			$.ajax({
				url: "/gm/removeDeity/" + this.value + "",
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
					if(result == 1) {
						layer.msg("取消成功");
					} else {
						layer.msg("取消失败");
					}
				}
			});
		} else {
			$.ajax({
				url: "/gm/setGmIsHot/" + this.value + "",
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
					if(result == 1) {
						layer.msg("设置成功");
					} else {
						layer.msg("设置失败");
					}
				}
			});
		}
	});

	table.on('tool(comment)', function(obj){
	    $.ajax({
			url: "/gm/removeDiscuss/" + obj.data.id + "",
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
				if(result == 1) {
					layer.msg('删除成功');
					$(".layui-laypage-btn")[0].click();
				} else {
					layer.msg('删除失败');
					$(".layui-laypage-btn")[0].click();
				}
			}
		});
	 });
});

function findDataCount() {
	var num = "";
	$.ajax({
		url: "/gm/getCommentCount",
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
});