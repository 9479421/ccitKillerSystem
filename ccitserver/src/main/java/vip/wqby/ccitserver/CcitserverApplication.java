package vip.wqby.ccitserver;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import vip.wqby.ccitserver.pojo.task.emailTask;
import vip.wqby.ccitserver.pojo.task.noteTask;
import vip.wqby.ccitserver.pojo.task.qqMessageTask;
import vip.wqby.ccitserver.queue.*;
import vip.wqby.ccitserver.util.TimeUtils;
import vip.wqby.ccitserver.util.miraiBotUtils;
import vip.wqby.ccitserver.util.qndxxUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
public class CcitserverApplication {


    public static void main(String[] args) {

        SpringApplication.run(CcitserverApplication.class, args);

        miraiBotUtils.login();
        new qqBotConsumer().start();

        //今日校园疫情消费者
        new cpDailyConsumer().start();
        //邮箱消费者
        new emailConsumer().start();
        //短信消费者
        new noteConsumer().start();

//
//        //启动任务消费者
//        new doHomeworkConsumer().start();
//        //青年大学习消费者
//        new qndxxConsumer().start();
//        //i博思签到消费者




//        AllQueue.emailTaskQueue.add(new emailTask("今日校园打卡完成","尊敬的王国权先生，学号20091230427，您于10日04:00的今日校园打卡任务《疫情防控信息收集晚报》已经执行完毕","9479421@qq.com"));
//           AllQueue.noteTaskQueue.add(new noteTask("17301491801", "1376992", new String[]{"王国权先生", TimeUtils.getTimeStr("2022-04-22 11:00"),"《[疫情打卡早上]》"}));


//


//        AllQueue.qqMessageTaskQueue.add(new qqMessageTask(296897195,new MessageChainBuilder().append("消息测试\n").append("test").append("青年大学习已完成").build()));
//        AllQueue.qqMessageTaskQueue.add(new qqMessageTask(296897195,new MessageChainBuilder().append("测试，请忽略本消息").append(new At(94794211)).append(new At(9479421)).append(new At(1900364029)).append(new At(2200507849L)).append(new At(1143726428)).append("test").append("青年大学习已完成").build()));
//       AllQueue.qqMessageTaskQueue.add(new qqMessageTask(296897195,1223468464,new MessageChainBuilder().append("消息测试\n").append("今日校园打卡已完成").build()));
//       AllQueue.qqMessageTaskQueue.add(new qqMessageTask(296897195,Long.valueOf("3355253157"),new MessageChainBuilder().append("消息测试\n").append("今日校园打卡已完成").build()));
//       AllQueue.qqMessageTaskQueue.add(new qqMessageTask(296897195,Long.valueOf("2369110547"),new MessageChainBuilder().append("消息测试\n").append("今日校园打卡已完成").build()));
//       AllQueue.qqMessageTaskQueue.add(new qqMessageTask(296897195,Long.valueOf("1900364029"),new MessageChainBuilder().append("消息测试\n").append("今日校园打卡已完成").build()));
//        qndxxUtils.messageChainBuilder.append(new At(9479421));
//        System.out.println(qndxxUtils.messageChainBuilder.build().toString().equals(""));


    }
}
