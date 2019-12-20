$(document).ready(function(){
	
	layui.use('element', function(){
	  var element = layui.element;
	  element.on('nav(demo)', function(elem){
	    layer.msg(elem.text());
	  });
	});
	
	layui.use('table', function(){
	  var table = layui.table
	  ,form = layui.form;
	  
	  var data = getDataAll();
	  
	  table.render({
	    elem: '#gameScore'
	    ,cellMinWidth: 80
	    ,limit: 100
	    ,data:data
	  	,page:false
	    ,cols: [[
	      {type:'numbers',title:'编号'}
	      ,{field:'gameName', title:'剧本名称', align:'center', minWidth:80}
	      ,{field:'score', title:'平均分', align:'center', minWidth:80, sort: true}
	      ,{field:'discussPeopleNum', title: '评论人数', align:'center', minWidth:80, sort: true}
	      ,{field:'playPeopleNum', title: '玩过人数', align:'center', minWidth:80, sort: true}
	    ]]
	 
	  });
	});
	
	function getDataAll(){
		var data = new Array();
		$.ajax({
			url: "/gm/getGameScore",
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
});