function selectAll(selectStatus){//传入参数（全选框的选中状态）
	//根据name属性获取到单选框的input，使用each方法循环设置所有单选框的选中状态
	var num = 0;
		if(selectStatus){
			$("input[name='check']").each(function(i,n){
 			 n.checked = true;
		});
		}else{
		  $("input[name='check']").each(function(i,n){
			   n.checked = false;
		});
	}
}
$(function(){
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
		var id_array =new Array();
		$("input[name='check']").each(function(i,n){
			if(n.checked){
				id_array.push(this.id);//向数组中添加元素
			}
		}); 
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
				} else {
					layer.msg("发送消息失败");
				}
			}
		});
});
});