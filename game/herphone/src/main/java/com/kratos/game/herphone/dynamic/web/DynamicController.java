package com.kratos.game.herphone.dynamic.web;

import org.springframework.context.annotation.Description;
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
import com.kratos.game.herphone.dynamic.bean.DynamicReplyReq;
import com.kratos.game.herphone.dynamic.bean.ReplyListReq;
import com.kratos.game.herphone.dynamic.bean.SendDynamicReq;
import com.kratos.game.herphone.playerDynamic.bean.ReqTagsDynamic;
@Deprecated
@RequestMapping("/dynamic")
@PrePermissions
@RestController // ：@RestController注解相当于@ResponseBody ＋ @Controller合在一起的作用。
public class DynamicController extends BaseController {
	// 推荐神评
	@Deprecated
	@GetMapping("/nominateBest/{dynamicId}")
	@PrePermissions
	public ResponseEntity<?> nominateBest(@PathVariable String dynamicId){
		dynamicService.nominateBest(dynamicId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	// 发送
	@Deprecated
	@PostMapping("/sendDynamic")
	@PrePermissions
	public ResponseEntity<?> sendDynamic(@RequestBody SendDynamicReq req) {
		dynamicService.sendDynamic(req.getResList(),req.getTags());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// 回复
	@Deprecated
	@PostMapping("/replyDynamic")
	@PrePermissions
	public ResponseEntity<?> replyDiscuss(@RequestBody DynamicReplyReq req) {
		dynamicService.replyDynamic(req.getToDynamicId(), req.getResList());
		return new ResponseEntity<>(HttpStatus.OK);
	}
	@Deprecated
	// 根据页数 查找朋友圈动态--- 
	@GetMapping("/findFriendsDynamic/{page}")
	@PrePermissions
	public ResponseEntity<?> fiendHoneDynamic(@PathVariable String page) {
		return new ResponseEntity<>(dynamicService.fiendHoneDynamic(Integer.parseInt(page)), HttpStatus.OK);
	}
	@Deprecated
	// 根据页数 查询我的动态
	@GetMapping("/findMyDynamic/{page}")
	@PrePermissions
	public ResponseEntity<?> findMyDynamic(@PathVariable String page) {
		return new ResponseEntity<>(dynamicService.findMyDynamic(Integer.parseInt(page)), HttpStatus.OK);
	}

	// ---------------------------------------------------------------------------------------------------------------
	@Deprecated
	// 分组查询 回复
	@PostMapping("/findReplyByGroup")
	@PrePermissions
	public ResponseEntity<?> findDiscussByGroupId(@RequestBody DiscussFindByGroupIdReq req) {
		return new ResponseEntity<>(dynamicService.findReplyByGroupId(req.getGroupId(), req.getPage()), HttpStatus.OK);
	}

	@Deprecated
	// 查詢回复
	@PostMapping("/findReply")
	@PrePermissions
	public ResponseEntity<?> replyList(@RequestBody ReplyListReq req) {
		return new ResponseEntity<>(dynamicService.replyListByPage(req.getDynamicId(), req.getPage()), HttpStatus.OK);
	}
	@Deprecated
	// 点赞
	@GetMapping("/addPaise/{dynamicId}")
	@PrePermissions
	public ResponseEntity<?> addLike(@PathVariable String dynamicId) {
		dynamicLikeService.addLike(dynamicId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	@Deprecated
	/**
    * 查找 回复我的评论，根据page 页数
    */
    @GetMapping("/seachMyReplyList/{page}")
    @PrePermissions
    public ResponseEntity<?> myReplyListByPage(@PathVariable String page){
    	return new ResponseEntity<>(dynamicService.myReplyListByPage(Integer.parseInt(page)), HttpStatus.OK);
    }
	
	@Deprecated
    /**
     * 查找 回复我的评论，根据page 页数
     */
     @GetMapping("/findDynamciInfo/{id}")
     @PrePermissions
     public ResponseEntity<?> findDynamicInfo(@PathVariable String id){
     	return new ResponseEntity<>(dynamicService.findDynamicInfo(id), HttpStatus.OK);
     }
	 @Deprecated
     /**
      * 查找未回复动态 
      */
     @GetMapping("/seachUnreadDynamicReply")
     @PrePermissions
     public ResponseEntity<?> seachUnreadReply(){
     	int count=(int)dynamicService.findUnreadReplyNum();
     	return new ResponseEntity<>(count,HttpStatus.OK);
     } 
     /**
      * 用户查询标签
      */
     @GetMapping("/findDynamciTags")
     @PrePermissions
     public ResponseEntity<?> findDynamciTags(){
     	return new ResponseEntity<>(playerTagsService.findDynamciTags(),HttpStatus.OK);
     } 
	 @Deprecated
     /**
      * 用户通过标签查询广场内容
      */
     @PostMapping("/findByTagsDynamic")
     @PrePermissions
     public ResponseEntity<?> findByTagsDynamic(@RequestBody ReqTagsDynamic req){
     	return new ResponseEntity<>(dynamicService.findByTagsDynamic(req.getPage(),req.getTags()),HttpStatus.OK);
     } 
}