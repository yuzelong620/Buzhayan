package com.kratos.game.herphone.discuss.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kratos.game.herphone.aop.PrePermissions;
import com.kratos.game.herphone.common.BaseController;
import com.kratos.game.herphone.discuss.bean.DiscussFindByGroupIdReq;
import com.kratos.game.herphone.discuss.bean.DiscussListByPageReq;
import com.kratos.game.herphone.discuss.bean.DiscussListByPageRes;
import com.kratos.game.herphone.discuss.bean.DiscussListReq;
import com.kratos.game.herphone.discuss.bean.DiscussListRes;
import com.kratos.game.herphone.discuss.bean.DiscussReplyListReq;
import com.kratos.game.herphone.discuss.bean.DiscussReplyReq;
import com.kratos.game.herphone.discuss.bean.DiscussSendReq;
import com.kratos.game.herphone.discuss.bean.ReqChangeStateList;
 
@RequestMapping("/discuss")
@PrePermissions
@RestController//：@RestController注解相当于@ResponseBody ＋ @Controller合在一起的作用。
public class DiscussController extends BaseController { 
	//护眼大队送 神评
	@PrePermissions
	@GetMapping("/recommendBest/{discussId}")
	public ResponseEntity<?> recommendBest(@PathVariable String discussId) {
		discussService.recommendBest(discussId);
	    return new ResponseEntity<>(HttpStatus.OK);
	}
	@PostMapping("/findDiscussCounts")
    @PrePermissions
    public ResponseEntity<?> findDiscussCount(@RequestBody List<DiscussListReq> list) {
        return new ResponseEntity<>(discussOptionService.findDiscussCounts(list), HttpStatus.OK);
    }	
	@PostMapping("/findDiscussCount1")
    @PrePermissions
    public ResponseEntity<?> findDiscussCount(@RequestBody DiscussListReq req) {
        return new ResponseEntity<>(discussOptionService.findDiscussCount(req.getGameId(),req.getChatId(), req.getTalkId(), req.getOptionIndex()), HttpStatus.OK);
    }
	
	@PostMapping("/findDiscussCount")
    @PrePermissions
    public ResponseEntity<?> findDiscussCountById(@RequestBody DiscussListReq req) {
        return new ResponseEntity<>(discussOptionService.findDiscussCount(req.getGameId(),req.getChatId()), HttpStatus.OK);
    }
	/**
	 * 查询剧本下评论数量
	 * @param req
	 * @return
	 */
	@GetMapping("/gameDiscussCount/{gameId}")
    @PrePermissions
    public ResponseEntity<?> findDiscussCountByGameId(@PathVariable int gameId) {
        return new ResponseEntity<>(discussOptionService.findDiscussCount(gameId), HttpStatus.OK);
    }
	//分组查询--玩家回复系统的消息作为第一条评论
	//根据评论ID查询二级评论
	@PostMapping("/findDiscussByGroupId")
    @PrePermissions
    public ResponseEntity<?> findDiscussByGroupId(@RequestBody DiscussFindByGroupIdReq req) {
        return new ResponseEntity<>(discussService.findReplyByGroup(req.getGroupId(),req.getPage()), HttpStatus.OK);
    }
    //获取   选项的评论信息，包括最新评论和 精选评论
    @PostMapping("/listByIndex")
    @PrePermissions
    public ResponseEntity<DiscussListRes> listByIndex(@RequestBody DiscussListReq req) {
        return new ResponseEntity<>(discussService.listByIndex(req.getGameId(),req.getChatId(),req.getTalkId(),req.getOptionIndex()), HttpStatus.OK);
    }
    //根据页数 查找 最新评论
    @PostMapping("/listByPage2")
    @PrePermissions
    public ResponseEntity<DiscussListByPageRes> listByPage(@RequestBody DiscussListByPageReq req) {
    	return new ResponseEntity<>(discussService.listByPage(req.getGameId(),req.getChatId(),req.getTalkId(),req.getOptionIndex(),req.getPage()), HttpStatus.OK);
    }
    
    /**
     * 根据游戏ID和章节ID查询对应评论
     * @param req
     * @return
     */
    @PostMapping("/findDiscuss")
    @PrePermissions
    public ResponseEntity<DiscussListByPageRes> findDiscussByPage(@RequestBody DiscussListByPageReq req) {
    	return new ResponseEntity<>(discussService.listByPage(req.getGameId(),req.getChatId(),req.getPage()), HttpStatus.OK);
    }
    //发送评论
    @PostMapping("/sendDiscuss1")
    @PrePermissions
    public ResponseEntity<?> sendDiscuss(@RequestBody DiscussSendReq req) {
    	discussService.sendDiscuss(req.getGameId(),req.getChatId(),req.getTalkId(),req.getOptionIndex(),req.getContent());
    	return new ResponseEntity<>(HttpStatus.OK);
    }
    
    //发表评论
    @PostMapping("/sendDiscuss")
    @PrePermissions
    public ResponseEntity<?> sendDiscuss1(@RequestBody DiscussSendReq req) {
    	discussService.sendDiscuss(req.getGameId(),req.getChatId(),req.getContent());
    	return new ResponseEntity<>(HttpStatus.OK);
    }
    //回复评论
    @PostMapping("/replyDiscuss")
    @PrePermissions
    public ResponseEntity<?> replyDiscuss(@RequestBody DiscussReplyReq req) {
    	discussService.replyDiscuss(req.getDiscussId(),req.getContent());
    	return new ResponseEntity<>(HttpStatus.OK);
    }    
    //根据页数 查找 最新评论
    @PostMapping("/replylistByPage")
    @PrePermissions
    public ResponseEntity<?> replyList(@RequestBody DiscussReplyListReq req) {
    	return new ResponseEntity<>(discussService.replyListByPage(req.getDiscussId(),req.getPage()), HttpStatus.OK);
    }
    
    /**
     *查找 未读回复
     */
    @GetMapping("/seachUnreadReply")
    @PrePermissions
    public ResponseEntity<?> seachUnreadReply(){
    	int count=discussService.findUnreadReply()+dynamicService.findUnreadReplyNum(); 	
    	return new ResponseEntity<>(count ,HttpStatus.OK);
    } 
    /**
     * 查找 回复我的评论，根据page 页数
     */
    @GetMapping("/seachMyReplyList/{page}")
    @PrePermissions
    public ResponseEntity<?> myReplyListByPage(@PathVariable String page){
    	return new ResponseEntity<>(discussService.myReplyListByPage(Integer.parseInt(page)), HttpStatus.OK);
    }
    
    @GetMapping("/addPaiseDiscuss/{discussId}")
    @PrePermissions
    public ResponseEntity<?> addLikeDiscuss(@PathVariable String discussId){
    	discussLikeService.addLikeDiscuss(discussId);
    	return new ResponseEntity<>( HttpStatus.OK);
    }
    
    //查找  我的最新评论
    @GetMapping("/findMyDiscusse/{page}")
    @PrePermissions
    public ResponseEntity<?> findMyDiscusse(@PathVariable String page){    	 
    	return new ResponseEntity<>(discussService.findMyDiscussByPage(Integer.parseInt(page)),HttpStatus.OK);
    }
    
    //点赞评论
    @GetMapping("/likeDiscuss/{discussId}")
    @PrePermissions
    public ResponseEntity<?> likeDiscusse(@PathVariable String discussId){ 
    	discussLikeService.addLikeDiscuss(discussId);
    	return new ResponseEntity<>(HttpStatus.OK);
    }
    
    //取消点赞
    @GetMapping("/deLikeDiscuss/{discussId}")
    @PrePermissions
    public ResponseEntity<?> deLikeDiscusse(@PathVariable String discussId){ 
    	discussLikeService.deLikeDiscuss(discussId);
    	return new ResponseEntity<>(HttpStatus.OK);
    }
    
    /**
     * 查询对我的点赞信息
     */
    @GetMapping("/findLikeMyDiscuss/{page}")
    @PrePermissions
    public ResponseEntity<?> findLikeMyDiscuss(@PathVariable int page){ 
    	
    	return new ResponseEntity<>(discussLikeService.findLikeMyDiscuss(page),HttpStatus.OK);
    }
    
    /**
     * 设置点赞信息已查看
     */
    @PostMapping("/setReadState")
    @PrePermissions
    public ResponseEntity<?> setReadState(@RequestBody ReqChangeStateList req ){ 
    	discussLikeService.setReadState(req.getReqChangeStateList());
    	return new ResponseEntity<>(HttpStatus.OK);
    }
    
    /**
     	* 查询对我评论点赞但未读的点赞数量
     */
    
    @GetMapping("/findLikeCount")
    @PrePermissions
    public ResponseEntity<?> findLikeCount(){ 
    	return new ResponseEntity<>(discussLikeService.findLikeCount(),HttpStatus.OK);
    }
    /**
	 * 查询对我的评论的回复消息
	 * @param page
	 * @return
	 */
    @GetMapping("/findReplyMyDiscuss/{page}")
    @PrePermissions
    public ResponseEntity<?> findReplyMyDiscuss(@PathVariable String page){ 
    	
    	return new ResponseEntity<>(discussService.findReplyMyDiscuss(Integer.parseInt(page)),HttpStatus.OK);
    }
    
	/**
	 * 查询未读评论信息数量
	 * @param page
	 * @return
	 */
    @GetMapping("/findReplyMyDiscussCount")
    @PrePermissions
    public ResponseEntity<?> findReplyMyDiscussCount(){
    	return new ResponseEntity<>(discussService.findReplyMyDiscussCount(),HttpStatus.OK);
    }
}