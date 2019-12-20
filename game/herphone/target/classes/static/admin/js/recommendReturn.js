$(document).ready(function(){
layui.use('table', function(){
  var table = layui.table
  ,form = layui.form;
  
  var count = findDataCount();
  
  table.render({
    elem: '#recommendReturn'
    ,url:'/gm/findByhold'
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
      ,{field:'c_id', title:'C_ID', align:'center', width:100}
      ,{field:'discussContent',title:'评论内容', align:'center', minWidth:80}
      ,{field:'replieNum', title:'恢复', align:'center', width:95, templet: '#barDemo'}
    ]]
    ,page:{
    	 layout: ['count', 'prev', 'page', 'next', 'limit', 'refresh', 'skip']
    	 ,limits: [20,50,100] 
    	 ,first: false 
    	 ,last: false 
    }
  });
  
  table.on('tool(recommendReturn)', function(obj){
	    var data = obj.data;
	    $.ajax({
			url: "/gm/ecommendBestUndeal",
			type: 'get',
			data:{
				discussId:obj.data.discussId
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				if(XMLHttpRequest.status == 401) {
					notLogin(XMLHttpRequest, textStatus, errorThrown);
				} else {
					layer.msg(XMLHttpRequest.responseJSON.message);
				}
			},
			success: function(result) {
				layer.msg('恢复成功');
				$(".layui-laypage-btn")[0].click();
			}
		});
	  });
});
function findDataCount(){
	var num = "";
	$.ajax({
		url: "/gm/findcountByhold",
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