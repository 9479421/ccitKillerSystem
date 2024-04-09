package vip.wqby.ccitserver.queue;

import org.springframework.data.mongodb.core.MongoTemplate;
import vip.wqby.ccitserver.config.SpringUtil;
import vip.wqby.ccitserver.pojo.QndxxUser;
import vip.wqby.ccitserver.pojo.task.rollCallTask;
import vip.wqby.ccitserver.util.IbsUtils;
import vip.wqby.ccitserver.util.miraiBotUtils;
import vip.wqby.ccitserver.util.qndxxUtils;

import java.util.ArrayList;
import java.util.List;

public class rollCallConsumer extends Thread{
    public List<rollCallTask> getRollCallUserList() {
        MongoTemplate mongoTemplate = (MongoTemplate) SpringUtil.getBean("mongoTemplate");
        List<rollCallTask> rollCallUserList = mongoTemplate.findAll(rollCallTask.class, "ibsqdUserList");
//        System.out.println("rollCallUserList:"+rollCallUserList);
        return rollCallUserList;
    }
    @Override
    public void run() {
        for (;;){
            List<rollCallTask> rollCallUserList= getRollCallUserList();
//            for (rollCallTask rct : rollCallUserList){
//                //循环遍历
//                IbsUtils ibsUtils = new IbsUtils();
//                ibsUtils.doRollCall(rct.getUsername(),rct.getPassword(),rct.getCourseId(),rct.getToken(),rct.getUserId());
//            }

            IbsUtils ibsUtils = new IbsUtils();
            ibsUtils.doRollCallList(rollCallUserList);
            if (!IbsUtils.messageChainBuilder.build().toString().equals("")){
                miraiBotUtils.sendGroup(296897195,IbsUtils.messageChainBuilder.append("\n懒B们，i博思上课签到已完成").build());
                ibsUtils.messageChainBuilder.clear();
            }


            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
