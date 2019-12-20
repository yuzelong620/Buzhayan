//用户登录
function login(td) {
	var user = document.getElementById("username").value;
	var pass = document.getElementById("password").value;
	if(user == "" || pass == "" || user == null || pass == null) {
		layer.msg("用户名或密码格式错误");
	} else {
		$.ajax({
			url: "/gamemanager/login",
			type: 'get',
			data: {
				username: user,
				password: pass
			},
			success: function(result) {
				if(result) {
					layer.msg("登录成功");
					setTimeout(function() {
						window.location.reload();
					}, 1000);
				} else {
					layer.msg("账号或密码不正确");
				}
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				layer.msg("错误信息：" + XMLHttpRequest.responseJSON.message);
			},
		});
	}
}
//用户没有登录
function notLogin(XMLHttpRequest, textStatus, errorThrown){
	if(XMLHttpRequest.status == 401){
    	layer.msg(XMLHttpRequest.responseJSON.message);
    	setTimeout(function() {
    		layer.open({
    			 type: 1,
				 title:'',
				 skin: 'layui-layer-molv', //加上边框
				 area: ['600px', '300px'], //宽高             
				 content:"<link rel='stylesheet' type='text/css' href='css/login.css'>" +
				 "<link rel='stylesheet' type='text/css' href='css/bootstrap.min.css'>" +
			     "<script type='text/javascript' src='js/bootstrap.min.js'></script>" +
				 "<div class='poptop'>" +
		         "<h3>登录</h3>" +
				 "</div>" +
			     "<div style='padding: 35px 35px 10px;'>" +
				 "<div class='input-group'>" +
		             "<span class='input-group-addon'><i class='layui-icon layui-icon-username'></i></span>" +
		             "<input type='text' id='username' class='form-control' placeholder='请输入用户名'>" +
		         "</div><br>" +
				 "<div class='input-group'>" +
		             "<span class='input-group-addon'><i class='layui-icon layui-icon-password'></i></span>" +
		             "<input type='password' id='password' class='form-control' placeholder='请输入密码'>" +
		         "</div><br>" +
				 "<button type='button' style='width:100%' class='btn btn-primary' onclick='login(this)' >登录</button>" +
				 "</div><br>" 
			});
			
			/*layer.open({
    			  type: 1,
				  skin: 'layui-layer-nobg',
				  area: ['500px', '550px'], 
				  shadeClose: true,
				  title:false,
				  content:'<div class="lowin"><link rel="stylesheet" type="text/css" href="css/auth.css">'
					+'<div class="lowin-brand">'
					+'<img src="img/kodinger.jpg" alt="logo"></div>'
					+'<div class="lowin-wrapper">'
					+'<div class="lowin-box lowin-login">'
					+'<div class="lowin-box-inner">'
					+'<p>不眨眼后台管理系统</p>'
					+'<div class="lowin-group">'
					+'<label>用户名</label>'
					+'<input type="text" id="username" class="lowin-input"></div>'
					+'<div class="lowin-group password-group">'
					+'<label>密码 </label>'
					+'<input type="password" id="password" name="password" autocomplete="current-password" class="lowin-input"></div>'
					+'<button class="lowin-btn login-btn" onclick="login(this)">登录</button></div></div></div></div>'
			});*/
			
        }, 800);
    } else {
    	layer.msg(XMLHttpRequest.responseJSON.message);
    }
}