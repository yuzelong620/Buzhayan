$(document).ready(function(){
layui.use('table', function(){
  var table = layui.table
  ,form = layui.form;
  
  var count = playerCount();
  
  table.render({
    elem: '#noSpeak'
    ,url:'/gm/getPlayerAllData'
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
    , cols: [[
		{ type: 'numbers', title: '编号' }
		, { field: 'roleId', title: '玩家ID', align: 'center', minWidth: 80 }
		, {field: 'nickName', title: '昵称', align: 'center', minWidth: 80}
		, { field: 'phone', title: '手机号', align: 'center', minWidth: 150,templet: function(res){
			 if(res.phone == null || res.phone == "null" || res.phone == ""){
				return "玩家未绑定手机号";
			 } else {
				return res.phone
			 }
		}}
		, { field: 'achievementDebris', title: '碎片数', align: 'center', minWidth: 80,sort: true}
		, { field: 'clue', title: '线索值', align: 'center', minWidth: 80,sort: true}
		, { field: 'attentionCount', title: '关注数', align: 'center', minWidth: 80,sort: true}
		, { field: 'fansCount', title: '粉丝数', align: 'center', minWidth: 80,sort: true}
		, { field: 'playNum', title: '玩过剧本个数', align: 'center', minWidth: 50,sort: true, templet: function(res){
			if(res.playNum == null || res.playNum == "null" || res.playNum == ""){
				return "0";
			} else {
				return res.playNum
			}
		}}
		, { field: 'playDuration', title: '游戏时长(分钟)', align: 'center', minWidth: 120,sort: true, templet: function(res){
			if(res.playDuration == null || res.playDuration == "null" || res.playDuration == ""){
				return "0";
			} else {
				return timeFormat(res.playDuration);
			}
		}}
		,{field:'0', title: '禁言(操作)', align:'center', minWidth:230, edit: 'text', sort: false,templet: function(res){
			if(res.noSpeakTime == 0){
				return "<font color='#009100'>正常</font>"
			} else {
				var timestamp=new Date().getTime();
				if(timestamp - res.noSpeakTime > 0){ //获取当前时间的时间戳 对比是否过了禁言时间
					return "<font color='#009100'>正常</font>"
				} else {
					return "解除时间: " + datetimeFormat(res.noSpeakTime) 
				}
			}
		}}
		,{field:'isBlock', title:'封号', align:'center', width:120, templet: '#switchTpl', unresize: false}
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
				url: "/gm/callisblock/" + obj.value + "",
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
						layer.msg('解封成功');
						$(".layui-laypage-btn").click();
					} else {
						layer.msg('解封失败');
						$(".layui-laypage-btn").click();
					}
				}
			});
		} else {
			$.ajax({
				url: "/gm/getisblock/" + this.value + "",
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
						layer.msg("封禁成功");
					} else {
						layer.msg("封禁失败");
					}
				}
			});
		}
	});

	$("#byIdData").click(function(){
	var roleId = document.getElementById('findPlayerData').value;
	var posPattern = /^(0|\+?[1-9][0-9]*)$/;
	if(roleId == null || roleId == "" || !posPattern.test(roleId)) {
		$("byIdData").val("");
		layer.msg('请输入正确的ID');
		return;
	}	
	
	var table = layui.table
	, form = layui.form;
	
	table.render({
		elem: '#noSpeak'
		 ,url:'/gm/findByIdPlayerData'
		 ,cellMinWidth: 80
		 ,limit: 50
		 ,parseData:function(res){
			if(res.roleId == null){
				return{
				 "code":200,
				 "msg":"无数据",
				 data:[res]
			 }
			} else {
				 return{
				 "code":0,
				 "msg":"",
				 data:[res]
			 }
			}
		 }
		,where:{
			roleId:roleId,
		}
		, cols: [[
		{ type: 'numbers', title: '编号' }
		, { field: 'roleId', title: '玩家ID', align: 'center', minWidth: 80 }
		, {field: 'nickName', title: '昵称', align: 'center', minWidth: 80}
		, { field: 'phone', title: '手机号', align: 'center', minWidth: 150,templet: function(res){
			 if(res.phone == null || res.phone == "null" || res.phone == ""){
				return "玩家未绑定手机号";
			 } else {
				return res.phone
			 }
		}}
		, { field: 'achievementDebris', title: '碎片数', align: 'center', minWidth: 80,sort: true}
		, { field: 'clue', title: '线索值', align: 'center', minWidth: 80,sort: true}
		, { field: 'attentionCount', title: '关注数', align: 'center', minWidth: 80,sort: true}
		, { field: 'fansCount', title: '粉丝数', align: 'center', minWidth: 80,sort: true}
		, { field: 'playNum', title: '玩过剧本个数', align: 'center', minWidth: 50,sort: true, templet: function(res){
			if(res.playNum == null || res.playNum == "null" || res.playNum == ""){
				return "0";
			} else {
				return res.playNum
			}
		}}
		, { field: 'playDuration', title: '游戏时长(分钟)', align: 'center', minWidth: 120,sort: true, templet: function(res){
			if(res.playDuration == null || res.playDuration == "null" || res.playDuration == ""){
				return "0";
			} else {
				return timeFormat(res.playDuration);
			}
		}}
		,{field:'0', title: '禁言(操作)', align:'center', minWidth:230, edit: 'text', sort: false,templet: function(res){
			if(res.noSpeakTime == 0){
				return "<font color='#009100'>正常</font>"
			} else {
				var timestamp=new Date().getTime();
				if(timestamp - res.noSpeakTime > 0){ //获取当前时间的时间戳 对比是否过了禁言时间
					return "<font color='#009100'>正常</font>"
				} else {
					return "解除时间: " + datetimeFormat(res.noSpeakTime) 
				}
			}
		}}
		,{field:'isBlock', title:'封号', align:'center', width:120, templet: '#switchTpl', unresize: false}
	]]
		, page: false
	});		
	
})

$("#byNameData").click(function(){
	var nickName = document.getElementById('findPlayerData').value;
	
	var table = layui.table
	, form = layui.form;

		var count = byNamePlayerCount(nickName);

		table.render({
			elem: '#noSpeak'
			 ,url:'/gm/findByNamePlayerData'
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
				nickName:nickName,
			}
			, cols: [[
				{ type: 'numbers', title: '编号' }
				, { field: 'roleId', title: '玩家ID', align: 'center', minWidth: 80 }
				, {field: 'nickName', title: '昵称', align: 'center', minWidth: 80}
				, { field: 'phone', title: '手机号', align: 'center', minWidth: 150,templet: function(res){
					 if(res.phone == null || res.phone == "null" || res.phone == ""){
						return "玩家未绑定手机号";
					 } else {
						return res.phone
					 }
				}}
				, { field: 'achievementDebris', title: '碎片数', align: 'center', minWidth: 80,sort: true}
				, { field: 'clue', title: '线索值', align: 'center', minWidth: 80,sort: true}
				, { field: 'attentionCount', title: '关注数', align: 'center', minWidth: 80,sort: true}
				, { field: 'fansCount', title: '粉丝数', align: 'center', minWidth: 80,sort: true}
				, { field: 'playNum', title: '玩过剧本个数', align: 'center', minWidth: 50,sort: true, templet: function(res){
					if(res.playNum == null || res.playNum == "null" || res.playNum == ""){
						return "0";
					} else {
						return res.playNum
					}
				}}
				, { field: 'playDuration', title: '游戏时长(分钟)', align: 'center', minWidth: 120,sort: true, templet: function(res){
					if(res.playDuration == null || res.playDuration == "null" || res.playDuration == ""){
						return "0";
					} else {
						return timeFormat(res.playDuration);
					}
				}}
				,{field:'0', title: '禁言(操作)', align:'center', minWidth:230, edit: 'text', sort: false,templet: function(res){
					if(res.noSpeakTime == 0){
						return "<font color='#009100'>正常</font>"
					} else {
						var timestamp=new Date().getTime();
						if(timestamp - res.noSpeakTime > 0){ //获取当前时间的时间戳 对比是否过了禁言时间
							return "<font color='#009100'>正常</font>"
						} else {
							return "解除时间: " + datetimeFormat(res.noSpeakTime) 
						}
					}
				}}
				,{field:'isBlock', title:'封号', align:'center', width:120, templet: '#switchTpl', unresize: false}
			]]
			, page: {
				layout: ['count', 'prev', 'page', 'next', 'limit', 'refresh', 'skip']
				, limits: [20, 50, 100]
				, first: false
				, last: false
			}
		});		
	
})

$("#byPhoneData").click(function(){
	var phone = document.getElementById('findPlayerData').value;
	var posPattern = /^[1][3,4,5,6,7,8][0-9]{9}$/;
	if(phone == null || phone == "" || !posPattern.test(phone)) {
		$("#byIdData").val("");
		layer.msg('请输入正确的手机号码');
		return;
	}
	
	var table = layui.table
	, form = layui.form;
	
	table.render({
		elem: '#noSpeak'
		 ,url:'/gm/findByPhonePlayerData'
		 ,cellMinWidth: 80
		 ,limit: 50
		 ,parseData:function(res){
			 if(res.roleId == null){
				return{
				 "code":200,
				 "msg":"无数据",
				 data:[res]
			 }
			} else {
				 return{
				 "code":0,
				 "msg":"",
				 data:[res]
			 }
			}
		 }
		,where:{
			phone:phone,
		}
		, cols: [[
			{ type: 'numbers', title: '编号' }
			, { field: 'roleId', title: '玩家ID', align: 'center', minWidth: 80 }
			, {field: 'nickName', title: '昵称', align: 'center', minWidth: 80}
			, { field: 'phone', title: '手机号', align: 'center', minWidth: 150,templet: function(res){
				 if(res.phone == null || res.phone == "null" || res.phone == ""){
					return "玩家未绑定手机号";
				 } else {
					return res.phone
				 }
			}}
			, { field: 'achievementDebris', title: '碎片数', align: 'center', minWidth: 80,sort: true}
			, { field: 'clue', title: '线索值', align: 'center', minWidth: 80,sort: true}
			, { field: 'attentionCount', title: '关注数', align: 'center', minWidth: 80,sort: true}
			, { field: 'fansCount', title: '粉丝数', align: 'center', minWidth: 80,sort: true}
			, { field: 'playNum', title: '玩过剧本个数', align: 'center', minWidth: 50,sort: true, templet: function(res){
				if(res.playNum == null || res.playNum == "null" || res.playNum == ""){
					return "0";
				} else {
					return res.playNum
				}
			}}
			, { field: 'playDuration', title: '游戏时长(分钟)', align: 'center', minWidth: 120,sort: true, templet: function(res){
				if(res.playDuration == null || res.playDuration == "null" || res.playDuration == ""){
					return "0";
				} else {
					return timeFormat(res.playDuration);
				}
			}}
			,{field:'0', title: '禁言(操作)', align:'center', minWidth:230, edit: 'text', sort: false,templet: function(res){
				if(res.noSpeakTime == 0){
					return "<font color='#009100'>正常</font>"
				} else {
					var timestamp=new Date().getTime();
					if(timestamp - res.noSpeakTime > 0){ //获取当前时间的时间戳 对比是否过了禁言时间
						return "<font color='#009100'>正常</font>"
					} else {
						return "解除时间: " + datetimeFormat(res.noSpeakTime) 
					}
				}
			}}
			,{field:'isBlock', title:'封号', align:'center', width:120, templet: '#switchTpl', unresize: false}
		]]
		, page: false
	});		
	
})
$("#findAtFoulPlay").click(function(){
	var table = layui.table
	, form = layui.form;
	
	var count = findAtFoulPlayCount();
	
	table.render({
		elem: '#noSpeak'
		 ,url:'/gm/findAtFoulPlayAll'
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
		, cols: [[
			{ type: 'numbers', title: '编号' }
			, { field: 'roleId', title: '玩家ID', align: 'center', minWidth: 80 }
			, {field: 'nickName', title: '昵称', align: 'center', minWidth: 80}
			, { field: 'phone', title: '手机号', align: 'center', minWidth: 150,templet: function(res){
				 if(res.phone == null || res.phone == "null" || res.phone == ""){
					return "玩家未绑定手机号";
				 } else {
					return res.phone
				 }
			}}
			, { field: 'achievementDebris', title: '碎片数', align: 'center', minWidth: 80,sort: true}
			, { field: 'clue', title: '线索值', align: 'center', minWidth: 80,sort: true}
			, { field: 'attentionCount', title: '关注数', align: 'center', minWidth: 80,sort: true}
			, { field: 'fansCount', title: '粉丝数', align: 'center', minWidth: 80,sort: true}
			, { field: 'playNum', title: '玩过剧本个数', align: 'center', minWidth: 50,sort: true, templet: function(res){
				if(res.playNum == null || res.playNum == "null" || res.playNum == ""){
					return "0";
				} else {
					return res.playNum
				}
			}}
			, { field: 'playDuration', title: '游戏时长(分钟)', align: 'center', minWidth: 120,sort: true, templet: function(res){
				if(res.playDuration == null || res.playDuration == "null" || res.playDuration == ""){
					return "0";
				} else {
					return timeFormat(res.playDuration);
				}
			}}
			,{field:'0', title: '禁言(操作)', align:'center', minWidth:230, edit: 'text', sort: false,templet: function(res){
				if(res.noSpeakTime == 0){
					return "<font color='#009100'>正常</font>"
				} else {
					var timestamp=new Date().getTime();
					if(timestamp - res.noSpeakTime > 0){ //获取当前时间的时间戳 对比是否过了禁言时间
						return "<font color='#009100'>正常</font>"
					} else {
						return "解除时间: " + datetimeFormat(res.noSpeakTime) 
					}
				}
			}}
			,{field:'isBlock', title:'封号', align:'center', width:120, templet: '#switchTpl', unresize: false}
		]]
		, page: {
				layout: ['count', 'prev', 'page', 'next', 'limit', 'refresh', 'skip']
				, limits: [20, 50, 100]
				, first: false
				, last: false
		}
	});		
	
})
	table.on('edit(noSpeak)', function(obj){
    var value = obj.value //得到修改后的值
    ,data = obj.data //得到所在行所有键值
    ,field = obj.field; //得到字段
    var posPattern = /^[+]{0,1}(\d+)$/;
	if(!posPattern.test(value)) {
		layer.msg("请输入正确禁言时长(小时)");
		$(".layui-laypage-btn").click();
		return;
	} else if(value == 0){
		$.ajax({
			url: "/gm/callnospeak",
			type: 'post',
			data:{
				playerId:data.playerId,
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				if(XMLHttpRequest.status == 401) {
					notLogin(XMLHttpRequest, textStatus, errorThrown);
				} else {
					layer.msg(XMLHttpRequest.responseJSON.message);
				}
			},
			success: function() {
				layer.msg("解除禁言成功");
				$(".layui-laypage-btn").click();
			}
		});
	} else {
		$.ajax({
			url: "/gm/setByIdNoSpeak",
			type: 'get',
			data:{
				playerId:data.playerId,
				time:value * 60 * 60 * 1000
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				if(XMLHttpRequest.status == 401) {
					notLogin(XMLHttpRequest, textStatus, errorThrown);
				} else {
					layer.msg(XMLHttpRequest.responseJSON.message);
				}
			},
			success: function() {
				layer.msg("成功禁言玩家[ <font color='#EAC100'>" + data.nickName + "</font> ] 禁言时长:" + value + "小时");
				$(".layui-laypage-btn").click();
			}
		});
	}
  });
});
function playerCount() {
	var num = "";
	$.ajax({
		url: "/gm/findPlayerCount",
		type: 'get',
		async: false,
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			if (XMLHttpRequest.status == 401) {
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

function byNamePlayerCount(nickName) {
	var num = "";
	$.ajax({
		url: "/gm/findByNamePlayerCount",
		type: 'get',
		data:{
			nickName:nickName
		},
		async: false,
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			if (XMLHttpRequest.status == 401) {
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

function findAtFoulPlayCount() {
	var num = "";
	$.ajax({
		url: "/gm/findAtFoulPlayCount",
		type: 'get',
		async: false,
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			if (XMLHttpRequest.status == 401) {
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