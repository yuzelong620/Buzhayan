$(document).ready(function(){
layui.use('table', function(){
  var table = layui.table
  ,form = layui.form;
  
  var data = getDataAll();
  
  table.render({
    elem: '#gameDeletePhone'
    ,cellMinWidth: 80
    ,limit: 100 
    ,data:data
    ,page:false
    ,cols: [[
      {type:'numbers',title:'编号'}
      ,{field:'gameName', title:'剧本名', align:'center', minWidth:80, unresize: false, sort: false}
      ,{field:'authorName', title: '作者', align:'center', minWidth:80, sort: false}
      ,{field:'phone', title: '手机号码(可修改)', align:'center', minWidth:80, edit: 'text', sort: false}
      ,{field:'', title:'删除', align:'center', width:100, templet: '#barDemo', unresize: true}
    ]]
    ,id:'dataTable'
  });
  
  table.on('edit(gameDeletePhone)', function(obj){
    var value = obj.value //得到修改后的值
    ,data = obj.data //得到所在行所有键值
    ,field = obj.field; //得到字段
    var posPattern = /^[1][3,4,5,6,7,8][0-9]{9}$/;
	if(!posPattern.test(value)) {
		layer.msg("请输入正确手机号码格式");
		var data = getDataAll();
		table.reload('dataTable', {
			data:data
		});
		return;
	} else {
		$.ajax({
			url: "/gm/gmUpdatePhone",
			type: 'get',
			data:{
				playerId:data.playerId,
				phone:value
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				if(XMLHttpRequest.status == 401) {
					notLogin(XMLHttpRequest, textStatus, errorThrown);
				} else {
					layer.msg(XMLHttpRequest.responseJSON.message);
				}
			},
			success: function() {
				layer.msg("成功修改作者[ " + data.authorName + " ]的手机号为:" + value);
				table.reload('dataTable', {
					data:getDataAll()
				});
			}
		});
	}
  });
  
  function getDataAll(){
		var data = new Array();
		$.ajax({
			url: "/gm/getHavePhone",
			type: 'get',
			dataType: "json",
			async: false,
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				if(XMLHttpRequest.status == 401) {
					notLogin(XMLHttpRequest, textStatus, errorThrown);
				} else {
					layer.msg(XMLHttpRequest.responseJSON.message);
				}
			},
			success: function(result) {
				for(var i = 0;i < result.length;i++){
					data.push(result[i]);
				}
			}
		});
		return data;
	}
  
	table.on('tool(gameDeletePhone)', function(obj){
	    $.ajax({
			url: "/gm/gmDeletePhone",
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
				layer.msg('删除成功');
				var data = getDataAll();
				table.reload('dataTable', {
					data:data
				});
			}
		});
	 });
});
});