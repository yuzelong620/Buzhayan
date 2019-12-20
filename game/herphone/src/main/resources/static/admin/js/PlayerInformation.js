$(document).ready(function() {
	layui.use('table', function() {
		var table = layui.table
			, form = layui.form;

		var count = playerCount();

		table.render({
			elem: '#PlayerInformation'
			 ,url:'/gm/getPlayerAllData'
			, cellMinWidth: 80
			, limit: 50
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
				, { field: 'phone', title: '手机号', align: 'center', minWidth: 80,templet: function(res){
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
				, { field: 'playNum', title: '玩过剧本个数', align: 'center', minWidth: 80,sort: true, templet: function(res){
					if(res.playNum == null || res.playNum == "null" || res.playNum == ""){
						return "0";
					} else {
						return res.playNum
					}
      			}}
				, { field: 'playDuration', title: '总游戏时长(分钟)', align: 'center', minWidth: 80,sort: true, templet: function(res){
					if(res.playDuration == null || res.playDuration == "null" || res.playDuration == ""){
						return "0";
					} else {
						return timeFormat(res.playDuration);
					}
      			}}
			]]
			, page: {
				layout: ['count', 'prev', 'page', 'next', 'limit', 'refresh', 'skip']
				, limits: [20, 50, 100]
				, first: false
				, last: false
			}
		});		
		
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
		elem: '#PlayerInformation'
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
				, { field: 'phone', title: '手机号', align: 'center', minWidth: 80,templet: function(res){
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
				, { field: 'playNum', title: '玩过剧本个数', align: 'center', minWidth: 80,sort: true, templet: function(res){
					if(res.playNum == null || res.playNum == "null" || res.playNum == ""){
						return "0";
					} else {
						return res.playNum
					}
      			}}
				, { field: 'playDuration', title: '总游戏时长(分钟)', align: 'center', minWidth: 80,sort: true, templet: function(res){
					if(res.playDuration == null || res.playDuration == "null" || res.playDuration == ""){
						return "0";
					} else {
						return timeFormat(res.playDuration);
					}
      			}}
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
			elem: '#PlayerInformation'
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
				, { field: 'phone', title: '手机号', align: 'center', minWidth: 80,templet: function(res){
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
				, { field: 'playNum', title: '玩过剧本个数', align: 'center', minWidth: 80,sort: true, templet: function(res){
					if(res.playNum == null || res.playNum == "null" || res.playNum == ""){
						return "0";
					} else {
						return res.playNum
					}
      			}}
				, { field: 'playDuration', title: '总游戏时长(分钟)', align: 'center', minWidth: 80,sort: true, templet: function(res){
					if(res.playDuration == null || res.playDuration == "null" || res.playDuration == ""){
						return "0";
					} else {
						return timeFormat(res.playDuration);
					}
      			}}
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
		elem: '#PlayerInformation'
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
				, { field: 'phone', title: '手机号', align: 'center', minWidth: 80,templet: function(res){
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
				, { field: 'playNum', title: '玩过剧本个数', align: 'center', minWidth: 80,sort: true, templet: function(res){
					if(res.playNum == null || res.playNum == "null" || res.playNum == ""){
						return "0";
					} else {
						return res.playNum
					}
      			}}
				, { field: 'playDuration', title: '总游戏时长(分钟)', align: 'center', minWidth: 80,sort: true, templet: function(res){
					if(res.playDuration == null || res.playDuration == "null" || res.playDuration == ""){
						return "0";
					} else {
						return timeFormat(res.playDuration);
					}
      			}}
			]]
		, page: false
	});		
	
})

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