$(document).ready(function(){
layui.use('table', function(){
  var table = layui.table
  ,form = layui.form;
  
  var data = getDataAll();
  
  table.render({
    elem: '#gameAddPhone'
    ,cellMinWidth: 80
    ,limit: 100 
    ,data:data
    ,page:false
    ,cols: [[
      {type:'numbers',title:'编号'}
      ,{field:'gameName', title:'剧本名', align:'center', minWidth:80, unresize: false, sort: false}
      ,{field:'authorName', title: '作者', align:'center', minWidth:80, sort: false}
      ,{field:'', title:'添加', align:'center', width:150, templet: '#barDemo', unresize: true}
    ]]
    ,id:'dataTable'
  });
  
  function getDataAll(){
		var data = new Array();
		$.ajax({
			url: "/gm/getNotHavePhone",
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
  
	table.on('tool(gameAddPhone)', function(obj){
		layer.alert('<input name="title" id="phone" class="layui-input" type="text" placeholder="请输入手机号" autocomplete="off" lay-verify="title">', {
			  skin: 'layui-layer-molv' //样式类名
			  ,closeBtn: 1
			  ,title:'添加手机号'
			  ,btn: ['添加']
			}, function(){
			  var phone =document.getElementById('phone').value;
			  var posPattern = /^[1][3,4,5,6,7,8][0-9]{9}$/;
		      if(phone == null || phone == "" || !posPattern.test(phone)) {
			  $("#phone").val("");
				  layer.msg('请输入正确的手机号码');
				  return;
			  }
		      $.ajax({
					url: "/gm/gmAddPhone",
					type: 'get',
					data:{
						phone:phone,
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
						layer.msg('添加成功');
						var data = getDataAll();
						table.reload('dataTable', {
							data:data
						});
					}
				});
			 // layer.closeAll();
			});
	 });
});
});