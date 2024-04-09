package vip.wqby.ccitserver.queue;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import vip.wqby.ccitserver.pojo.task.homeworkTask;
import vip.wqby.ccitserver.pojo.task.qqMessageTask;
import vip.wqby.ccitserver.util.IbsUtils;
import vip.wqby.ccitserver.util.cpDailyUtils;
import vip.wqby.ccitserver.util.miraiBotUtils;

public class qqBotConsumer extends Thread {
    @Override
    public void run() {
        for (; ; ) {
            qqMessageTask poll = null;
            try {
                poll = AllQueue.qqMessageTaskQueue.take();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (poll != null) {
                try{
                    System.out.println("发送私人消息" + poll.toString());
                    if (poll.getType() == 1) {
                        miraiBotUtils.sendGroup(poll.getGroup(), poll.getData());
                    }
                    if (poll.getType() == 2) {
                        miraiBotUtils.sendNormal(poll.getGroup(), poll.getQq(), poll.getData());
                    }
                    try {
                        //随机延时15-20秒
                        int i = (int)(15+Math.random()*(20-15+1));
                        Thread.sleep(i*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }catch (Exception e){
                    System.out.println("=====================发送私人消息异常啊啊啊啊啊啊啊啊啊啊啊啊啊");
                    e.printStackTrace();
                }
            }
        }
    }
}
