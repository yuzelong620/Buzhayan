package com.kratos.game.herphone.tencent.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kratos.engine.framework.db.DbService;
import com.kratos.engine.framework.thread.TaskGroup;
import com.kratos.engine.framework.thread.TaskInterface;
import com.kratos.engine.framework.thread.WorkTask;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.event.WorkManager;
import com.kratos.game.herphone.game.chose.domain.ChosenOption;
import com.kratos.game.herphone.game.chose.service.ChosenOptionServiceImpl;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;

import lombok.extern.java.Log;
/**
 * 临时类，用于修改 玩家游戏选项
 * @author Administrator
 *
 */
@Log
@Deprecated
@Service
public class SaveService extends BaseService{
	static final String USER = "crxl2018";
	static final String PASS = "ASDasd19981018";
	static final String URL="jdbc:mysql://rm-m5e8930io3917v3nk5o.mysql.rds.aliyuncs.com:3306/herphone?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&characterEncoding=UTF-8";
	static ThreadLocal<Connection> local = new ThreadLocal<>();
    public static SaveService instance;
    public SaveService() {
		instance=this;
	}
    public void insertJubao(){
//    	Long playerId=2815133573419859780L;
//    	Player player=playerServiceImpl.get(playerId);
//    	PlayerContext.setPlayer(player);
//    	for(int i=0;i<500;i++){
//    		ReqReportBean reportBean=new ReqReportBean();
//	    	reportBean.setContent("举报内容。。"+i);
//	    	reportBean.setDiscussId("61c009ba0bb74ae7967ca910c69d3abc");
//	    	reportBean.setToPlayerId("2815133573419859780");
//	    	reportInfoService.reportInfo(reportBean);
//	    	log.info(i+"举报信息");
//    	}
    	
    }
   public void findAllPlayer(){
	  List<Player> list=playerServiceImpl.findAll();
	  log.info("一共"+list.size()+"个用户");
	  
	   int i=1;
	  for(Player p:list){
		  if(p.getId()<100000){
			  continue;
		  }
		  int j=i;
//		  PlayerContext.setPlayer(p);
//		  int oldValue=p.getExploration();
//		  updatePlayerExploration(p);
//		  i++;
//		  log.info(i+"个数据更新完毕."+p.getId()+"   " +oldValue+" 修改为"+p.getExploration());
		  Runnable run=new Runnable() {
			@Override
			public void run() {
               updatePlayerExp(p,j);
			}
			@Override
			public int hashCode() {
				return p.getId().hashCode();
			}
		};
		WorkManager.getInstance().putEvent(run);
		i++;
		if(i>1000&&i%1000==0){
			log.info("第"+i+"个更新完成");
		}
		
	  }
	  log.info("执行完毕！");
   }
   @Autowired
   ChosenOptionServiceImpl cosi;
   private void updatePlayerExploration(Player p) {
	   ChosenOption chosenOption=new ChosenOption();
	   chosenOption.setGameId(1);
	   cosi.editPlayerExploration(p, chosenOption);
	
    }
   
   public  void updatePlayerExp(Player player,int j){
	   Connection c = new SaveService().getConnection();
	   int num=0; 
	   try{
		   PreparedStatement p = c.prepareStatement("SELECT count(*)as num FROM `herphone`.`chosen_option` where `player_id` ="+player.getId());
		   ResultSet rs = p.executeQuery();
		   if (rs.next()) {
				num=rs.getInt("num");
		   } 
		   rs.close();
		   p.close();
		   
		   p=c.prepareStatement("UPDATE player SET exploration ="+num+" WHERE id="+player.getId());
		   p.executeUpdate();
		   p.close();
//		   if(num!=player.getExploration())
		   log.info("第"+j+"个用户。"+player.getId()+" 探索值："+player.getExploration()+",修改为："+num);
//		   else{
//			   System.out.print("0");
//			   
//		   }
		} 
	    catch (Exception e) {
			e.printStackTrace();
		}
   }
   
   public  List<Long> findPlayerIds(){
	   Connection c = new SaveService().getConnection();
	   List<Long> ids=new ArrayList<Long>();
	   String sql = "select id from player";
	   try{
		   PreparedStatement p = c.prepareStatement(sql);
		   ResultSet rs = p.executeQuery();
		   while (rs.next()) {
				ids.add(rs.getLong("id"));
		   } 
		   rs.close();
		   p.close();
		} 
	    catch (Exception e) {
			e.printStackTrace();
		}
	   return ids;
   }
	/**
     * 检查选项
     */
	public void checkDataOption() {
		
		
//		SaveService service = new SaveService();
//		Connection c = new SaveService().getConnection();
//				String sql = "select * from player where is_guest=1 " + skip + "," + limit;
//				PreparedStatement p = c.prepareStatement(sql);
//				ResultSet rs = p.executeQuery();
//				boolean readed = false;
//				while (rs.next()) {
//					readed = true;
//					ChosenOption obj = new ChosenOption();
//					obj.setId(rs.getLong("id"));
//					obj.setChatId(rs.getInt("chat_id"));
//					obj.setGameId(rs.getInt("game_id"));
//					obj.setOptionIndex(rs.getInt("option_index"));
//					obj.setPlayerId(rs.getLong("player_id"));
//					obj.setTalkId(rs.getInt("talk_id"));
//					service.syncSave(obj);
//				}
//				if (readed == false) {
//					break;// 没有读取到消息
//				}
//				rs.close();
//				p.close();
//				log.info("第" + page + "页");
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		List<UserEntity> users=new UserDao().find(null);
//		for(UserEntity entity:users){
//			updateIsGuest(entity.getPlayerId());
//		}
	}

	/** 本分库 */
	public Connection getConnection() {
		if (local.get() != null) {
			return local.get();
		}
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection(URL,USER,PASS);
			local.set(conn);
			return conn;
		} catch (Exception e) {
			return null;
		}
	}
	public void updateIsGuest(long playerId) {
		Runnable run = new Runnable() {
			public void run() {
				Runnable run = new Runnable() {
					public void run() {
						Connection c = getConnection();
						String sql = "UPDATE player SET is_guest =0 WHERE is_guest=1 and id="+playerId; 
						PreparedStatement p = null;
						try {
							p = c.prepareStatement(sql);
							if (p.executeUpdate() > 0) {
								log.info("更新成功----playerId:"+playerId);
							}
						} catch (Exception e) {
							log.info("报错：" + e.getMessage());
						} finally {
							if (p != null) {
								try {
									p.close();
								} catch (SQLException e) {
									e.printStackTrace();
								}
							}
						}

					}
				};
				group.hashPut(run);
			}
		};
		group.hashPut(run);
	}
	public void save(long playerId) {
		
	}

	public void syncSave(ChosenOption obj) {
		Runnable run = new Runnable() {
			public void run() {
				save(obj);
			}
		};
		group.hashPut(run);
	}

	private void save(ChosenOption obj) {
		Connection c = getConnection();
		String sql = "INSERT INTO `herphone`.`chosen_option` (`id`,`chat_id`,`game_id`,`option_index`, `player_id`,`talk_id`) VALUES("
				+ obj.getId() + "," + obj.getChatId() + "," + obj.getGameId() + "," + obj.getOptionIndex() + ","
				+ obj.getPlayerId() + "," + obj.getTalkId() + ")";
		PreparedStatement p = null;
		try {
			p = c.prepareStatement(sql);
			if (p.executeUpdate() > 0) {
				log.info("-");
			} else {
				log.info("执行失败：");
			}

		} catch (Exception e) {
			log.info("报错：" + e.getMessage());
		} finally {
			if (p != null) {
				try {
					p.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	public static TaskGroup<TaskInterface> group;

	public static void close() {
		group.close();
	}

	public synchronized static void init(int size) {
		if (group != null) {
			return;
		}
		String name = "db-save-service";
		TaskInterface[] threads = new TaskInterface[size];
		for (int i = 0; i < size; i++) {
			WorkTask t = new WorkTask(name + "_" + i);
			threads[i] = t;
		}
		group = new TaskGroup<>(threads);
		group.start();
	}

}
