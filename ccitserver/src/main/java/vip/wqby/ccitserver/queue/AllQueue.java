package vip.wqby.ccitserver.queue;

import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.SingleMessage;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import vip.wqby.ccitserver.config.SpringUtil;
import vip.wqby.ccitserver.pojo.CpDailyUser;
import vip.wqby.ccitserver.pojo.QndxxUser;
import vip.wqby.ccitserver.pojo.task.*;

import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;


public class AllQueue {
    public static LinkedBlockingQueue<homeworkTask> homeworkQueue =new LinkedBlockingQueue<homeworkTask>();
    public static LinkedBlockingQueue<emailTask> emailTaskQueue =new LinkedBlockingQueue<emailTask>();
    public static LinkedBlockingQueue<noteTask> noteTaskQueue =new LinkedBlockingQueue<noteTask>();
    public static Vector<cpDailyTask> cpDailyTasksVector = new Vector<>();
    public static Vector<qndxxTask> qndxxTasksVector = new Vector<>();
    public static LinkedBlockingQueue<qqMessageTask> qqMessageTaskQueue =new LinkedBlockingQueue<qqMessageTask>();


    public static void removeCpDailyTask(String username){
        //克隆一份
        Vector<cpDailyTask> cpDailyTasksVector1 = (Vector<cpDailyTask>)cpDailyTasksVector.clone();
        Iterator<cpDailyTask> iterator = cpDailyTasksVector1.iterator();
        while (iterator.hasNext()){
            cpDailyTask cell = iterator.next();
            if (cell.getUsername().equals(username)){
                cpDailyTasksVector.remove(cell);
                //删除数据库中的用户
                MongoTemplate mongoTemplate = (MongoTemplate) SpringUtil.getBean("mongoTemplate");
                Criteria criteria = Criteria.where("username").is(username);
                mongoTemplate.remove(new Query(criteria),CpDailyUser.class,"cpDailyUserList");
            }
        }
    }

    public static void removeQndxxTask(String session){
        Vector<qndxxTask> qndxxTasksVector1 = (Vector<qndxxTask>)qndxxTasksVector.clone();
        Iterator<qndxxTask> iterator = qndxxTasksVector1.iterator();
        while (iterator.hasNext()){
            qndxxTask cell = iterator.next();
            if (cell.getSession().equals(session)){
                qndxxTasksVector.remove(cell);
                //删除数据库中的用户
                MongoTemplate mongoTemplate = (MongoTemplate) SpringUtil.getBean("mongoTemplate");
                Criteria criteria = Criteria.where("session").is(session);
                mongoTemplate.remove(new Query(criteria), QndxxUser.class,"qndxxUserList");
            }
        }
    }
}
