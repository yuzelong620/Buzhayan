//时间戳日期格式化方法
function datetimeFormat(longTypeDate){ 
	 var dateTypeDate = "";  
	 var date = new Date();  
	 date.setTime(longTypeDate);  
	 dateTypeDate += date.getFullYear(); //年  
	 var month = date.getMonth()+1;
	 dateTypeDate += "-" + month; //月 		
	 dateTypeDate += "-" + date.getDate(); //日  
	 dateTypeDate += " " + (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()); //时  
	 dateTypeDate += ":" + (date.getMinutes() <10 ? '0' + date.getMinutes() : date.getMinutes());  //分 
	 dateTypeDate += ":" + (date.getSeconds() <10 ? '0' + date.getSeconds() : date.getSeconds());  //秒
	 return dateTypeDate; 
	} 
//时间戳换算成分钟
function timeFormat(longTypeDate){
	return parseInt(longTypeDate / 1000 / 60) ; 
}
