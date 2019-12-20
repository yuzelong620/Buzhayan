$(document).ready(function(){
layui.use('table', function(){
  var table = layui.table
  ,form = layui.form;
  
  var count = findDataCount();
  
  table.render({
    elem: '#information'
    ,url:'/gm/listPlayerMessge'
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
    ,cols: [[
      {type:'numbers',title:'编号'}
      ,{field:'content',title:'内容', align:'center', minWidth:80}
      ,{field:'fromUser',title:'操作人', align:'center', width:140}
      ,{field:'sendTime',title:'评论时间', align:'center', width:180}
      ,{field:'toUser',title:'接收用户', align:'center', width:140}
      ,{field:'', title:'查看历史记录', align:'center', width:150, templet: '#history', unresize: true}
    ]]
    ,page:{
    	 layout: ['count', 'prev', 'page', 'next', 'limit', 'refresh', 'skip']
    	 ,limits: [20,50,100] 
    	 ,first: false 
    	 ,last: false 
    }
  });
  
  table.on('tool(information)', function(obj){
	  $.ajax({
			url: "/gm/getPlayerMessgeByPlayerId/" + obj.data.playerId,
			type: 'get',
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				if(XMLHttpRequest.status == 401) {
					notLogin(XMLHttpRequest, textStatus, errorThrown);
				} else {
					layer.msg(XMLHttpRequest.responseJSON.message);
				}
			},
			success: function(result) {
				str = "";
				str += "<br>";
				var name = "";
				for(var i = result.length - 1; i >= 0; i--) {
					if(result[i].fromUser == "超级管理员") {
						str += "<div id='adminTime'>" + result[i].sendTime + "</div>";
						str += "<li class='msgContent right'>" + result[i].content + "</li>";
						str += "<div style='clear:both'></div><br>";
						name = result[i].toUser;
					} else {
						str += "<div id='userTime'>" + result[i].sendTime + "</div>";
						str += "<li class='msgContent left'>" + result[i].content + "</li>";
						str += "<div style='clear:both'></div><br>";
						name = result[i].fromUser;
					}
				}
				layer.open({
					type: 1,
					skin: 'layui-layer-molv', //加上边框
					area: ['850px', '600px'], //宽高
					content: "<br><div id='dialogBox'><center><h4><b>用户<span><font color='#FD7E3D'> " + name + " </font></span>的对话记录</b></h4></center></div><div id='main' class='main'>" +
						"<ul id='neirong' class='content'></ul>" +
						"<textarea id='msg_input' class='msgInput'></textarea>" +
						"<div class='radios' id='selectdr' style='margin-left:12px;'>" +
						"   <label>" +
						"       <input type='radio' name='optionsRadios' id='optionsRadios1' value='0' checked>纯文本" +
						"   </label>&nbsp;" +
						"   <label>" +
						"       <input type='radio' name='optionsRadios' id='optionsRadios2' value='1'>文本加链接" +
						"   </label>" +
						"</div>" +
						"<button id='sendbtn' class='sendbtn' value=" + obj.data.playerId + " onclick='insTell(this)'>发送</button>" +
						"</div>",
					title: "对话窗口"
				});
				pIds = obj.data.playerId;
				neirong.innerHTML = str;
				$('#neirong').scrollTop($('#neirong')[0].scrollHeight);
			}
		});
	 });
});

function findDataCount(){
	var num = "";
	$.ajax({
		url: "/gm/getInforCount",
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

$("#phoneReceive").click(function() {
	var data = document.getElementById('inData').innerText;
	var playerData = document.getElementById('receivePlayer').value;
	var posPattern = /^[1][3,4,5,6,7,8][0-9]{9}$/;
	var selectedColor = $('#selects input:radio:checked').val();
	if(selectedColor == undefined) {
		layer.msg("请选择发送格式");
		return;
	}
	if(data.length < 1 || data.length > 200) {
		if(playerData == null || playerData == "" || !posPattern.test(playerData)) {
			layer.msg("内容与手机号码格式错误请重新输入");
			return;
		}
		layer.msg("请输入1~200个字之间的内容");
		return;
	}
	if(playerData == null || playerData == "" || !posPattern.test(playerData)) {
		layer.msg("请输入正确的11位手机号码");
		return;
	} else {
		$.ajax({
			url: "/gm/systemSendMessgaeByPhone",
			type: 'get',
			data: {
				content: data,
				contentType: selectedColor,
				phone: playerData
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
					layer.msg("发送消息成功");
					setTimeout(function() {
						$(".layui-laypage-btn")[0].click();
					}, 500);
				} else {
					layer.msg("手机号不存在");
				}
			},
			error: function(jqXHR, textStatus, errorThrown) {
				layer.msg("发送失败");
			}
		});
	}
});

$("#idReceive").click(function() {
	var data = document.getElementById('inData').innerText;
	var playerData = document.getElementById('receivePlayer').value;
	var posPattern = /^\d*\.?\d+$/;
	var selectedColor = $('#selects input:radio:checked').val();
	if(selectedColor == undefined) {
		layer.msg("请选择发送格式");
		return;
	}
	if(data.length < 1 || data.length > 200) {
		if(playerData == null || playerData == "" || !posPattern.test(playerData)) {
			layer.msg("ID与内容格式错误请重新输入");
			return;
		}
		layer.msg("请输入1~200个字之间的内容");
		return;
	}
	if(playerData == null || playerData == "" || !posPattern.test(playerData)) {
		layer.msg("请输入正确格式的ID");
		return;
	} else {
		$.ajax({
			url: "/gm/systemSendMessgaeByRoleId",
			type: 'get',
			data: {
				content: data,
				contentType: selectedColor,
				RoleId: playerData
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
					layer.msg("发送消息成功");
					setTimeout(function() {
						$(".layui-laypage-btn")[0].click();
					}, 500);
				} else {
					layer.msg("ID不存在");
				}
			}
		});
	}
});

$("#allPlayerData").click(function() {
	var data = document.getElementById('inData').innerText;
	var selectedColor = $('#selects input:radio:checked').val();
	if(data.length < 1){
		layer.msg("请输入内容");
		return;
	}
	if(selectedColor == undefined) {
		layer.msg("请选择发送格式");
		return;
	} else {
		$.ajax({
			url: "/gm/sendMessagePlayerAll",
			type: 'get',
			data: {
				content: data,
				contentType: selectedColor,
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				if(XMLHttpRequest.status == 401) {
					notLogin(XMLHttpRequest, textStatus, errorThrown);
				} else {
					layer.msg(XMLHttpRequest.responseJSON.message);
				}
			},
			success: function(result) {
				layer.msg("发送消息成功");
				setTimeout(function() {
					$(".layui-laypage-btn")[0].click();
				}, 500);
			},
			error: function(jqXHR, textStatus, errorThrown) {
				layer.msg("发送失败");
			}
		});
	}
});
insTell = function(id){
	var input1 = document.getElementById('msg_input').value;
	var playerId = "";
	if(id.value == undefined || id.value == null || id.value == "") {
		playerId = id
	} else {
		playerId = id.value;
		sendMsg("click");
	}
	var selectedColor = $('#selectdr input:radio:checked').val();
	if(selectedColor == undefined) {
		layer.msg("请选择发送格式");
		return;
	}
	if(input1.length < 1 || input1.length > 200) {
		layer.msg("请输入1~200个字");
		return;
	}
	if(selectedColor == 0) {
		$.ajax({
			url: "/gm/systemSendMessgae",
			type: 'get',
			data: {
				content: input1,
				contentType: 0,
				playerIds: playerId
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
					layer.msg("消息发送成功");
					setTimeout(function() {
						$(".layui-laypage-btn")[0].click();
						layer.closeAll();
					}, 500);
				} else {
					layer.msg("消息发送失败");
				}
			}
		});
	} else {
		$.ajax({
			url: "/gm/systemSendMessgae",
			type: 'get',
			data: {
				content: input1,
				contentType: 1,
				playerIds: playerId
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
					layer.msg("消息发送成功");
					setTimeout(function() {
						$(".layui-laypage-btn")[0].click();
						layer.closeAll();
					}, 500);
				} else {
					layer.msg("消息发送失败");
				}
			}
		});
	}
}

//快捷键 发送
document.onkeypress = function(event) {
	var e = event || window.event;
	var keycode = e.keyCode || e.which;
	if(keycode == 13) {
		event.preventDefault();
		var selectedColor = $('#selectdr input:radio:checked').val();
		var input1 = document.getElementById('msg_input').value;
		if(selectedColor == undefined) {
			layer.msg("请选择发送格式");
			return;
		}
		if(input1.length < 1 || input1.length > 200 || input1 == null || input1 == "") {
			layer.msg("请输入1~200个字");
			return;
		}
		sendMsg();
	}
}

function sendMsg(res) {
	var input = document.getElementById('msg_input'); //查找缓存
	var ul = document.getElementById('neirong');

	var newLi = document.createElement('li');
	newLi.innerHTML = input.value;
	newLi.className = 'msgContent right';
	ul.appendChild(newLi);

	if(res != "click"){
		insTell(pIds);
	}
	
	input.value = '';

	var div = document.createElement('div');
	div.style = 'clear:both';
	ul.appendChild(div);

	newLi.scrollIntoView(); //将元素滚动到可见位置
}
$(function(){
 var id_array = new Array();
$("#allReceive").click(function loadSearchCourse() {
	document.getElementById("receivePlayer").value = "请在表格选择玩家";
	layui.use('table', function(){
		var table = layui.table
		  ,form = layui.form;
		  
		  var count = selectDataCount();
		  var dataAll = "";
		  
		  table.render({
		    elem: '#information'
		    ,url:'/gm/getPlayerAllData'
		    ,cellMinWidth: 80
		    ,limit: 50 
		    ,request: {
		        pageName: 'page',   // 页码的参数名称，默认：page
		        limitName: 'count' ,// 每页数据量的参数名，默认：limit
		    }
		    ,parseData:function(res){
		    	dataAll = res;
		    	return{
		    		"code":0,
		    		"msg":"",
		    		"count":count,
		    		data:res
		    	}
		    }
		    ,cols: [[
		    	{type:'checkbox'}
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
		    ,page:{
		    	 layout: ['count', 'prev', 'page', 'next', 'limit', 'refresh', 'skip']
		    	 ,limits: [20,50,100] 
		    	 ,first: false 
		    	 ,last: false 
		    }
		  });
		  table.on('checkbox(information)', function(obj){
		        if(obj.type == "one"){
		        	if(obj.checked){
		        		id_array.push(obj.data.playerId);	
			        } else {
			        	for (var i = 0; i < id_array.length; i++) {
							if(obj.data.playerId == id_array[i]){
								id_array.splice(i,1)
							}
						}
			        }
		        } else {
		        	if(obj.checked){
		        		id_array.splice(0,id_array.length);
		        	   for (var i = 0; i < dataAll.length; i++) {
		        		   id_array.push(dataAll[i].playerId);
		        	   }
		        	} else {
		        		id_array.splice(0,id_array.length);
		        	}
		        }
		    });
	});
});

$("#selected").click(function(){
		var data = document.getElementById('inData').innerText;
		var selectedColor = $('#selects input:radio:checked').val();
		if(selectedColor == undefined){
			layer.msg("请选择发送格式");
			return;
		}
		if(data.length <= 0 || data.length >= 200){
			layer.msg("请输入1~200个字之间的内容");
			return;
		} 
		if(id_array == null || id_array == ""){
			layer.msg("请选择要接收的玩家");
			return;
		}
		$.ajax({
			url:"/gm/systemSendMessgae",
			type:'get',	
			data:{
				content:data,
				contentType:selectedColor,
				playerIds:id_array
			},
			traditional:true,
			error: function (XMLHttpRequest, textStatus, errorThrown) {
	            if(XMLHttpRequest.status == 401){
	            	notLogin(XMLHttpRequest, textStatus, errorThrown);
	            } else {
	            	layer.msg(XMLHttpRequest.responseJSON.message);
	            }
	        },
			success:function(result){
				if(result){
					layer.msg("发送消息成功");
					setTimeout(location.reload(),2000);
				} else {
					layer.msg("发送消息失败");
				}
			}
		});
});
});
function selectDataCount(){
	var num = "";
	$.ajax({
		url: "/gm/findPlayerCount",
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