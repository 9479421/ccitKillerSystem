package vip.wqby.ccitserver.queue;

import org.springframework.data.mongodb.core.MongoTemplate;
import vip.wqby.ccitserver.config.SpringUtil;
import vip.wqby.ccitserver.pojo.CpDailyUser;
import vip.wqby.ccitserver.pojo.QndxxUser;
import vip.wqby.ccitserver.pojo.task.cpDailyTask;
import vip.wqby.ccitserver.pojo.task.qndxxTask;
import vip.wqby.ccitserver.util.LogUtils;
import vip.wqby.ccitserver.util.cpDailyUtils;
import vip.wqby.ccitserver.util.miraiBotUtils;
import vip.wqby.ccitserver.util.qndxxUtils;

import java.util.Date;
import java.util.List;
import java.util.Vector;

public class qndxxConsumer extends Thread{
    public List<QndxxUser> getQndxxUserList() {
        MongoTemplate mongoTemplate = (MongoTemplate) SpringUtil.getBean("mongoTemplate");
        List<QndxxUser> qndxxUserList = mongoTemplate.findAll(QndxxUser.class, "qndxxUserList");
        System.out.println("qndxxUserList:"+qndxxUserList);
        return qndxxUserList;
    }
    @Override
    public void run() {
        //首次运行，先获取已有的用户进去
        List<QndxxUser> qndxxUserList = getQndxxUserList();
        for (QndxxUser qu: qndxxUserList){
            AllQueue.qndxxTasksVector.add(new qndxxTask(qu.getSession(),qu.getQq(),qu.getPhone()));
        }
//        跑一次先
        Vector<qndxxTask> qndxxTasksVector1 = (Vector<qndxxTask>)AllQueue.qndxxTasksVector.clone();
        for (qndxxTask qt: qndxxTasksVector1){
            qndxxUtils qndxxUtils = new qndxxUtils();
            qndxxUtils.executeTask(qt.getSession().replace("\n",""),qt.getQq(),qt.getPhone());
        }
        if (!qndxxUtils.messageChainBuilder.build().toString().equals("")){
            miraiBotUtils.sendGroup(296897195,qndxxUtils.messageChainBuilder.append("\n青年大学习已完成").build());
            qndxxUtils.messageChainBuilder.clear();
        }


        for (; ; ) {
            Date date = new Date();
            int hours = date.getHours();
            int minutes = date.getMinutes();
            //每小时的10分运行一次
            if (minutes==10) {
                //拷贝一份出来
                Vector<qndxxTask> qndxxTasksVector = (Vector<qndxxTask>)AllQueue.qndxxTasksVector.clone();
                for (qndxxTask qt: qndxxTasksVector){
                    qndxxUtils qndxxUtils = new qndxxUtils();
                    //替换掉换行符的BUG
                    qndxxUtils.executeTask(qt.getSession().replace("\n",""),qt.getQq(),qt.getPhone());
                }
                if (!qndxxUtils.messageChainBuilder.build().toString().equals("")){
                    miraiBotUtils.sendGroup(296897195,qndxxUtils.messageChainBuilder.append("\n青年大学习已完成").build());
                    qndxxUtils.messageChainBuilder.clear();
                }
                //执行完等1分钟 防止重复执行
                try {
                    Thread.sleep(61000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
