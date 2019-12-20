window.onload = function() {
	var new_element = document.createElement("script");
	new_element.setAttribute("type", "text/javascript");
	new_element.setAttribute("src", "layer/layer.js");
	document.body.appendChild(new_element);
	loadSearchCourse();
}
function loadSearchCourse(page){
	if(page == undefined) {
		page = 1;
	}
	str = "";
	$('#messageBox').html("");
	$.ajax({
		url: "/gm/getPlayerCount",
		type: 'get',
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			if(XMLHttpRequest.status == 401) {
				notLogin(XMLHttpRequest, textStatus, errorThrown);
			} else {
				layer.msg(XMLHttpRequest.responseJSON.message);
			}
		},
		success: function(result) {
			var countNum = result;
			if(result == 0) {
				str += "<tr>";
				str += '<p style="text-align:center"><b>成就排行榜为空</b></p>';
				str += "</tr>";
				messageBox.innerHTML = str;
			} else {
				$.ajax({
					url: "/gm/getAchievement",
					type: 'get',
					data: {
						start: page,
						page: 50
					},
					error: function(XMLHttpRequest, textStatus, errorThrown) {
						if(XMLHttpRequest.status == 401) {
							notLogin(XMLHttpRequest, textStatus, errorThrown);
						} else {
							layer.msg(XMLHttpRequest.responseJSON.message);
						}
					},
					success: function(result) {
						if(result == null) {
							str += "<tr>";
							str += '<p style="text-align:center"><b>成就排行榜为空</b></p>';
							str += "</tr>";
							messageBox.innerHTML = str;
						} else {
							str += '<table class="table table-hover">';
							str += '<thead>';
							str += '<tr>';
							str += "<td align='center'>序号</td>";
							str += "<td align='center'>头像</td>";
							str += "<td align='center'>角色ID</td>";
							str += "<td align='center'>角色昵称</td>";
							str += "<td align='center'>排行分数</td>";
							str += "<td align='center'>手机号码</td>";
							str += '</tr>';
							str += '</thead>';
							str += "<tbody>";
							var number = 1;
							var numberPage = page * 50 - 50 + 1;
							number = numberPage;
							for(i in result) {
								str += "<tr>";
								str += "<td align='center'style='display:table-cell; vertical-align:middle'>" + number + "</td>";
								if(result[i].avatar_url == "玩家没有上传头像") {
									str += "<td align='center'style='display:table-cell; vertical-align:middle'>" + result[i].avatar_url + "</td>";
								} else {
									str += "<td align='center'style='display:table-cell; vertical-align:middle'><img class='img-circle' src=" + result[i].avatar_url + "></td>";
								}
								str += "<td align='center'style='display:table-cell; vertical-align:middle'>" + result[i].role_id + "</td>";
								str += "<td align='center'style='display:table-cell; vertical-align:middle'>" + result[i].nick_name + "</td>";
								str += "<td align='center'style='display:table-cell; vertical-align:middle'>" + result[i].topScore + "</td>";
								str += "<td align='center'style='display:table-cell; vertical-align:middle'>" + result[i].phone + "</td>";
								str += "</tr>";
								number++;
							}
							str += "</tbody>";
							str += "</table>";
							str += '<div id="myPage" class="Cpage"></div><br/><br/><br/>';
							str += "<div class='top'>";
							str += "<a href='#top' target='_self' id='topArrow'></a>";
							str += "</div>";
							$(document).ready(function() {
								$("#myPage").sPage({
									page: page, //当前页码，必填
									total: countNum, //数据总条数，必填
									pageSize: 50, //每页显示多少条数据，默认10条
									totalTxt: "共{total}条", //数据总条数文字描述，{total}为占位符，默认"共{total}条"
									showTotal: true, //是否显示总条数，默认关闭：false
									showSkip: true, //是否显示跳页，默认关闭：false
									showPN: true, //是否显示上下翻页，默认开启：true
									prevPage: "上一页", //上翻页文字描述，默认“上一页”
									nextPage: "下一页", //下翻页文字描述，默认“下一页”
									backFun: function(page) {
										//点击分页按钮回调函数，返回当前页码
										loadSearchCourse(page);
									}
								});
							})
						}
						messageBox.innerHTML = str;
					}
				})
			}
		}
	});
}