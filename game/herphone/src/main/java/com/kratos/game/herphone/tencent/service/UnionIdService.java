package com.kratos.game.herphone.tencent.service;

import java.util.ArrayList;
import java.util.UUID; 
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;
import com.github.kevinsawicki.http.HttpRequest;
import com.kratos.engine.framework.thread.TaskGroup;
import com.kratos.engine.framework.thread.TaskInterface;
import com.kratos.engine.framework.thread.WorkTask;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.tencent.bean.Item;
import com.kratos.game.herphone.tencent.entity.UnionIdEntity;

import lombok.extern.log4j.Log4j;
@Log4j
@Service
public class UnionIdService extends BaseService {
	
    static UnionIdService instance;
    
    public static UnionIdService getInstance(){
    	return instance;
    }
	
	public UnionIdService() {
		 instance=this;
	}

	public void save(String unionid,long playerId){
		String id=UUID.randomUUID().toString().replace("-", "");
		long cteateTime=System.currentTimeMillis();
		UnionIdEntity entity = new UnionIdEntity(id, unionid, playerId, cteateTime);
		unionIdDao.save(entity);
	}
	/**
	 * 注册
	 * @param playerId
	 * @param tencentUnionId
	 * @return
	 */
	private UnionIdEntity register(UnionIdEntity entity) {
		String tencentUnionId=entity.getTencentUnionId();
		UnionIdEntity temp=unionIdDao.findByUnionId(tencentUnionId);
		if(temp!=null){
			return temp;
		}
		unionIdDao.insert(entity);
		return entity;
		 
	}

	public void checkPlayerTencentOpenId() {
		ArrayList<Item> array = JdbcTemp.findOpenidAndPlayerId();
		int i=0;
		log.info(array.size()+" 个 开始执行 ");
		for (Item item : array) { 
			i++;
			final int j=i;
			Runnable run=new Runnable() {
				public void run() {
					String unionid = getUnionID(item.getOpenid());
					if(unionid==null){
						log.info("无法找到unionid,  openid:"+item.getOpenid());
						return;
					}
					item.setUnionid(unionid);
					long cteateTime = System.currentTimeMillis();
					String id = UUID.randomUUID().toString().replace("-", "");
					UnionIdEntity entity = new UnionIdEntity(id, unionid, item.getPlayerId(), cteateTime);
					register(entity);
				}
				@Override
				public int hashCode() {
					return j;
				}
			};
			group.hashPut(run);
			
		}		
		group.close(); 
		
		log.info("----------------------->執行完畢");
	}

 
 
    static TaskGroup<TaskInterface> group;
     
	public synchronized static void init(int size){
		if(group!=null){
			return;
		}
		String name= "unionid_thread_";
		TaskInterface[] threads=new TaskInterface[size]; 
		for(int  i=0;i<size;i++){
			WorkTask t=new WorkTask(name+"_"+i);
			threads[i]=t;
		}
		group=new TaskGroup<>(threads); 
		group.start();
	}
	private String getUnionID(String openId) {
		try {
			//測試接口
			String url = "https://graph.qq.com/oauth2.0/get_unionid?openid=" + openId + "&client_id=1109515925";
			String result = HttpRequest.get(url).accept("application/json").body();
			result=result.replace("callback( ", "");
			result=result.replace(" );", "");
			JSONObject json = JSONObject.parseObject(result);
			if(json==null){
				log.error("报错，openid:"+openId+"  "+json);
				return null;
			}
			Object obj=json.get("unionid");
			if(obj==null){
				log.error("报错，openid:"+openId+"  "+json);
				return null;
			}
			return obj.toString();
		}
		catch (Exception e) {
			e.printStackTrace();
			log.error(openId+"報錯了-----------------~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			return null;
		}
	}

}
