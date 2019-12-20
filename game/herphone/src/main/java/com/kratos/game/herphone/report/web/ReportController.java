package com.kratos.game.herphone.report.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kratos.game.herphone.aop.PrePermissions;
import com.kratos.game.herphone.common.BaseController;
import com.kratos.game.herphone.report.bean.ReqDealReportInfoDistinct;
import com.kratos.game.herphone.report.bean.ReqReportBean;
import com.kratos.game.herphone.report.bean.ReqReportInfoDistinct;

@RestController
@RequestMapping("/report")
@PrePermissions
public class ReportController extends BaseController{
	
	/**
	 * 举报信息
	 * @param playerId
	 * @return
	 */
	@PrePermissions(required = true)
    @PostMapping("/Info")
    public ResponseEntity<?> reportInfo(@RequestBody ReqReportBean reqReportBean) {
		reportInfoService.reportInfo(reqReportBean);
    	return new ResponseEntity<>(HttpStatus.OK); 
    }
	/**
	 * 举报玩家
	 * @param playerId
	 * @return
	 */
	@PrePermissions(required = true)
    @PostMapping("/Player")
    public ResponseEntity<?> reportPlayer(@RequestBody ReqReportBean reqReportBean) {
		reportPlayerService.reportPlayer(reqReportBean);
    	return new ResponseEntity<>(HttpStatus.OK); 
    }
	/**护眼先锋获取举报评论列表*/
	@PrePermissions(required = true)
    @PostMapping("/listReportInfoDistinct")
    public ResponseEntity<?> listReportInfoDistinct(@RequestBody ReqReportInfoDistinct reqReportInfoDistinct) {
    	return new ResponseEntity<>(reportInfoDistinctService.listReportInfoDistinctByType(reqReportInfoDistinct.getPage(), reqReportInfoDistinct.getType()),HttpStatus.OK); 
    }
	/**护眼先锋处理举报信息*/
	@PrePermissions(required = true)
    @PostMapping("/dealReportInfoDistinct")
    public ResponseEntity<?> dealReportInfoDistinct(@RequestBody ReqDealReportInfoDistinct reqDealReportInfoDistinct) {
		reportInfoDistinctService.dealReportInfoDistinct(reqDealReportInfoDistinct);
    	return new ResponseEntity<>(HttpStatus.OK); 
    }
}
