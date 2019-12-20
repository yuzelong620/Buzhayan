$(document).ready(function(){
layui.use('table', function(){
  var table = layui.table
  ,form = layui.form;
  
  var count = findDataCount();
  
  table.render({
    elem: '#RecommendDiscuss'
    ,url:'/gm/findByUndeal'
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
      ,{field:'dramaName', title:'剧本名', align:'center', width:100, unresize: false, sort: false}
      ,{field:'c_id', title:'C_ID', align:'center', width:80}
      ,{field:'discussContent', align:'center', minWidth:80,  title:'评论内容'}
      ,{field:'handlerState', title:'神评', align:'center', width:120, templet: '#switchTpl', unresize: false}
      ,{field:'handlerState', title:'删除', align:'center', width:110, templet: '#checkboxTpl', unresize: true}
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
				url: "/gm/setRecommendUtd",
				type: 'get',
				data:{
					discussId:this.value
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					if(XMLHttpRequest.status == 401) {
						notLogin(XMLHttpRequest, textStatus, errorThrown);
					} else {
						layer.msg(XMLHttpRequest.responseJSON.message);
					}
				},
				success: function(result) {
					layer.msg("取消成功");
				}
			});
		} else {
			$.ajax({
				url: "/gm/recommendBestDeal",
				type: 'get',
				data:{
					discussId:this.value
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					if(XMLHttpRequest.status == 401) {
						notLogin(XMLHttpRequest, textStatus, errorThrown);
					} else {
						layer.msg(XMLHttpRequest.responseJSON.message);
					}
				},
				success: function(result) {
					layer.msg("设置成功");
				}
			});
		}
	});

	table.on('tool(RecommendDiscuss)', function(obj){
		$.ajax({
			url: "/gm/recommendBestHold",
			type: 'get',
			data: {
				discussId: obj.data.discussId
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				if(XMLHttpRequest.status == 401) {
					notLogin(XMLHttpRequest, textStatus, errorThrown);
				} else {
					layer.msg(XMLHttpRequest.responseJSON.message);
				}
			},
			success: function(result) {
				layer.msg("操作成功");
				$(".layui-laypage-btn")[0].click();
			},
		});
	 });
	
});

function findDataCount() {
	var num = "";
	$.ajax({
		url: "/gm/recommendCount",
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