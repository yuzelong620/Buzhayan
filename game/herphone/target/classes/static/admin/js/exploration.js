window.onload=function loadSearchCourse(page){
var new_element=document.createElement("script");
	 new_element.setAttribute("type","text/javascript");
	 new_element.setAttribute("src","layer/layer.js");
	 document.body.appendChild(new_element);
var explorationAtPage = 1;
	$('#explorationBox').html("");
	$.ajax({
		url:"/gm/getPlayerCount",
		type:'get',	
		error: function (XMLHttpRequest, textStatus, errorThrown) {
            if(XMLHttpRequest.status == 401){
            	layer.msg(XMLHttpRequest.responseJSON.message);
            	setTimeout(function() {
            		layer.open({
  					  type: 1,
  					  skin: 'layui-layer-rim',
  					  area: ['910px', '600px'], 
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
  				});
	            }, 2000);
            } else {
            	layer.msg(XMLHttpRequest.responseJSON.message);
            }
        },
		success:function(result){
			var countNum = result;
			if(result == 0){
				$('#commentBox').html("");
				str ="";
				str += "<tr>";
				str += '<p style="text-align:center"><b>未找到评论信息</b></p>';                  
				str += "</tr>";
				explorationBox.innerHTML = str;  
			} else {
				var sendPage = 1;
				if(page > sendPage){
					sendPage = page;
					explorationAtPage = page;
				} 
				if(page == 1){
					explorationAtPage = 1;
				}
				$.ajax({
					url:"/gm/getExploration",
					type:'get',	
					data:{
						start:sendPage,
						page:50
					},
					error: function (XMLHttpRequest, textStatus, errorThrown) {
			            if(XMLHttpRequest.status == 401){
			            	layer.msg(XMLHttpRequest.responseJSON.message);
			            	setTimeout(function() {
			            		layer.open({
			  					  type: 1,
			  					  skin: 'layui-layer-rim',
			  					  area: ['910px', '600px'], 
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
			  				});
				            }, 2000);
			            } else {
			            	layer.msg(XMLHttpRequest.responseJSON.message);
			            }
			        },
					success:function(result){
						$('#explorationBox').html("");
						var str = "";
						if(result==null){
							str += "<tr>" ; 
							str += '<p style="text-align:center"><b>探索排行榜为空</b></p>';               
							str +="</tr>";
							explorationBox.innerHTML = str;  
						} else {
							str +=  '<table class="table table-hover">';
							str +=  '<thead>';
							str +=  '<tr>';
							str +=  "<td align='center'>序号</td>";
							str +=  "<td align='center'>头像</td>";
							str +=  "<td align='center'>角色ID</td>";
							str +=  "<td align='center'>角色昵称</td>";
							str +=  "<td align='center'>排行分数</td>";
							str +=  "<td align='center'>手机号码</td>";
							str +=  '</tr>';
							str +=  '</thead>';
							str += "<tbody>";
							var number = 1;
							var numberPage = explorationAtPage * 50 - 50 + 1;
							number = numberPage;
							for (i in result) {  
						    str += "<tr>";
							str +="<td align='center'style='display:table-cell; vertical-align:middle'>"+number+"</td>" ;
							if(result[i].avatar_url == "玩家没有上传头像"){
								str +="<td align='center'style='display:table-cell; vertical-align:middle'>"+result[i].avatar_url+"</td>" ;
							} else {
								str +="<td align='center'style='display:table-cell; vertical-align:middle'><img class='img-circle' src="+result[i].avatar_url+"></td>" ;
							}
							str +="<td align='center'style='display:table-cell; vertical-align:middle'>"+result[i].role_id+"</td>" ;
							str +="<td align='center'style='display:table-cell; vertical-align:middle'>"+result[i].nick_name+"</td>" ;
							str +="<td align='center'style='display:table-cell; vertical-align:middle'>"+result[i].topScore+"</td>" ;
							str +="<td align='center'style='display:table-cell; vertical-align:middle'>"+result[i].phone+"</td>" ;
							str += "</tr>";
							number++;
							}
							str += "</tbody>";
							str += "</table>";
							str +='<div id="myPage" class="Cpage"></div><br/><br/><br/>';
							str += "<div class='top'>";
							str += 	"<a href='#top' target='_self' id='topArrow'></a>";
							str += "</div>";
							$(document).ready(function(){
								$("#myPage").sPage({
								    page:explorationAtPage,//当前页码，必填
								    total:countNum,//数据总条数，必填
								    pageSize:50,//每页显示多少条数据，默认10条
								    totalTxt:"共{total}条",//数据总条数文字描述，{total}为占位符，默认"共{total}条"
								    showTotal:true,//是否显示总条数，默认关闭：false
								    showSkip:true,//是否显示跳页，默认关闭：false
								    showPN:true,//是否显示上下翻页，默认开启：true
								    prevPage:"上一页",//上翻页文字描述，默认“上一页”
								    nextPage:"下一页",//下翻页文字描述，默认“下一页”
								    backFun:function(page){
								        //点击分页按钮回调函数，返回当前页码
								       loadSearchCourse(page);
								    }
								});
							})
						}
						explorationBox.innerHTML = str;  
					}
				})
			}
		}
	});
 }