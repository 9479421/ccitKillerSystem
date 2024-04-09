package vip.wqby.ccitserver.queue;

import com.alibaba.fastjson.JSONObject;
import org.springframework.data.mongodb.core.MongoTemplate;
import vip.wqby.ccitserver.config.SpringUtil;
import vip.wqby.ccitserver.pojo.CpDailyUser;
import vip.wqby.ccitserver.pojo.task.cpDailyTask;
import vip.wqby.ccitserver.pojo.task.qqMessageTask;
import vip.wqby.ccitserver.util.cpDailyUtils;
import vip.wqby.ccitserver.util.http.Http;
import vip.wqby.ccitserver.util.http.HttpResponse;
import vip.wqby.ccitserver.util.miraiBotUtils;

import java.util.*;

public class cpDailyConsumer extends Thread {
    public List<CpDailyUser> getCpDailyUserList() {
        MongoTemplate mongoTemplate = (MongoTemplate) SpringUtil.getBean("mongoTemplate");
        List<CpDailyUser> cpDailyUserList = mongoTemplate.findAll(CpDailyUser.class, "cpDailyUserList");
        System.out.println("cpDailyUserList:" + cpDailyUserList);
        return cpDailyUserList;
    }

    public static HashSet<String> hasSignUserList = new HashSet<>();

    @Override
    public void run() {
        //首次运行，先获取已有的用户进去
        List<CpDailyUser> cpDailyUserList = getCpDailyUserList();
        for (CpDailyUser cdt : cpDailyUserList) {
            AllQueue.cpDailyTasksVector.add(new cpDailyTask(cdt.getUsername(), cdt.getPassword(), cdt.getQq()));
        }
        //跑一次先
        //置入代理ip
//        cpDailyUtils.setProxy();
        Vector<cpDailyTask> cpDailyTasksVector1 = (Vector<cpDailyTask>)AllQueue.cpDailyTasksVector.clone();
        for (cpDailyTask cdt: cpDailyTasksVector1){
            cpDailyUtils cpDailyUtils = new cpDailyUtils();
            cpDailyUtils.executeTask(cdt.getUsername(),cdt.getPassword(),cdt.getQq());
        }
        if (!cpDailyUtils.messageChainBuilder.build().toString().equals("")){
            //发送QQ群消息
            miraiBotUtils.sendGroup(296897195,cpDailyUtils.messageChainBuilder.append("\n今日校园疫情打卡已完成").build());
            cpDailyUtils.messageChainBuilder.clear();
        }


        for (; ; ) {
            Date date = new Date();
            int hours = date.getHours();
            int minutes = date.getMinutes();
            System.out.println("现在时间是：" + hours + ":" + minutes);
            //每小时的10分执行一次
            if (hours==0&&minutes==0){
                hasSignUserList.clear();
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if ( (hours==0 && minutes==30) ||  (hours==7 && minutes==0) ||  (hours==8 && minutes==0) || (hours==9 && minutes==0)) {
                //置入代理ip
//                cpDailyUtils.setProxy();
                //拷贝一份出来
                Vector<cpDailyTask> cpDailyTasksVector = (Vector<cpDailyTask>) AllQueue.cpDailyTasksVector.clone();
                for (cpDailyTask cdt : cpDailyTasksVector) {
                    //已经签到完直接跳过
                    if(hasSignUserList.contains(cdt.getUsername())){
                        continue;
                    }
                    cpDailyUtils cpDailyUtils = new cpDailyUtils();
                    cpDailyUtils.executeTask(cdt.getUsername(), cdt.getPassword(),cdt.getQq());
                }
                if (!cpDailyUtils.messageChainBuilder.build().toString().equals("")){
                    //发送QQ群消息
                    System.out.println("发送QQ群消息=="+cpDailyUtils.messageChainBuilder.build().toString());
                    miraiBotUtils.sendGroup(296897195,cpDailyUtils.messageChainBuilder.append("\n今日校园疫情打卡已完成").build());
                    cpDailyUtils.messageChainBuilder.clear();
                }
                //执行完等1分钟 防止重复执行
                try {
                    Thread.sleep(61000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
