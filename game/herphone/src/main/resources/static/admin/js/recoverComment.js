$(document).ready(function(){
layui.use('table', function(){
  var table = layui.table
  ,form = layui.form;
  
  var count = findDataCount();
  
  table.render({
    elem: '#recoverComment'
    ,url:'/gm/getRecoverDiscussAll'
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
      ,{field:'content', align:'center', minWidth:80, title:'评论内容'}
      ,{field:'praiseNum', title: '点赞数', align:'center', width:100, sort: true}
      ,{field:'replieNum', title: '回复数', align:'center', width:100, sort: true}
      ,{field:'fromRoleId', title: '玩家ID', align:'center', width:130, sort: false}
      ,{field:'fromNickName', title: '评论玩家', align:'center', width:180, sort: false}
      ,{field:'timeStamp', title: '评论时间', align:'center', width:180, sort: false}
      ,{field:'replieNum', title:'恢复', align:'center', width:95, templet: '#barDemo'}
    ]]
    ,page:{
    	 layout: ['count', 'prev', 'page', 'next', 'limit', 'refresh', 'skip']
    	 ,limits: [20,50,100] 
    	 ,first: false 
    	 ,last: false 
    }
  });
  
  table.on('tool(recoverComment)', function(obj){
	    var data = obj.data;
	    $.ajax({
			url: "/gm/recoverComment/" + obj.data.id + "",
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
					layer.msg('恢复成功');
					$(".layui-laypage-btn")[0].click();
				} else {
					layer.msg('恢复失败');
					$(".layui-laypage-btn")[0].click();
				}
			}
		});
	  });
});
function findDataCount(){
	var num = "";
	$.ajax({
		url: "/gm/getRecoverCommentCount",
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