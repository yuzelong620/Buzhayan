window.onload=function loadSearchCourse(page){
var new_element=document.createElement("script");
	 new_element.setAttribute("type","text/javascript");
	 new_element.setAttribute("src","layer/layer.js");
	 document.body.appendChild(new_element);
	$('#scenarioAvgBox').html("");
	$.ajax({
		url:"/gm/getAverageScore",
		type:'get',
		dataType:"json",
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
			$('#scenarioAvgBox').html("");
			var str = "";
			if(result==null){
				str += "<tr>" ;
				str += '<p style="text-align:center"><b>未找到剧本</b></p>';         
				str += "</tr>";
				scenarioNumBox.innerHTML = str;  
			} else {
				str +=  '<table class="table table-hover">';
				str +=  '<thead>';
				str +=  '<tr>';
				str +=  "<td align='center'>剧本名称</td>";
				str +=  "<td align='center'>剧本平均分</td>";
				str +=  "<td align='center'>剧本已评论人数</td>";
				str +=  '</tr>';
				str +=  '</thead>';
				str += "<tbody>";
				for (i in result) {  
			    str += "<tr>";
			    if(result[i].score == 0){
					str +="<td align='center'style='display:table-cell; vertical-align:middle'>" + result[i].scoreName + "</td>" ;
					str +="<td align='center'style='display:table-cell; vertical-align:middle'>没有玩家进行评分</td>" ;
					str +="<td align='center'style='display:table-cell; vertical-align:middle'>没有玩家进行评分</td>" ;
				} else {
					str +="<td align='center'style='display:table-cell; vertical-align:middle'>" + result[i].scoreName + "</td>";    
					str +="<td align='center'style='display:table-cell; vertical-align:middle'> " +  result[i].score.toFixed(2)  + "</td>"; 
					str += "<td align='center'style='display:table-cell; vertical-align:middle'> " +  result[i].playerNum  + "</td>"; 
				}
				str += "</tr>";
				}
				str += "</tbody>";
				str += "</table>";
				str += "<div class='top'>";
				str += 	"<a href='#top' target='_self' id='topArrow'></a>";
				str += "</div>";
			}
			scenarioAvgBox.innerHTML = str;  
		}
	})
}