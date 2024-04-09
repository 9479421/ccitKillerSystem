package vip.wqby.ccitserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import vip.wqby.ccitserver.pojo.Card;
import vip.wqby.ccitserver.pojo.CommonResult;
import vip.wqby.ccitserver.pojo.CpDailyUser;
import vip.wqby.ccitserver.pojo.task.cpDailyTask;
import vip.wqby.ccitserver.queue.AllQueue;
import vip.wqby.ccitserver.service.CpDailyService;
import vip.wqby.ccitserver.util.cpDailyUtils;

import java.util.Iterator;
import java.util.Vector;

@Service
public class CpDailyServiceImpl implements CpDailyService {
    @Autowired
    MongoTemplate mongoTemplate;


    @Override
    public CommonResult addTask(String username, String password,String qq, String cardID) {
        Criteria criteria = Criteria.where("cardId").is(cardID);
        Query query = Query.query(criteria);
        Card card = mongoTemplate.findOne(query, Card.class, "card");
        System.out.println(card);
        if (card == null) {
            return new CommonResult(444, "该卡密不存在！", null);
        }
        if (card.isUse() == true) {
            return new CommonResult(443, "该卡密已经被使用过！", null);
        }

        Vector<cpDailyTask> cpDailyTasksVector = (Vector<cpDailyTask>) AllQueue.cpDailyTasksVector.clone();
        Iterator<cpDailyTask> iterator = cpDailyTasksVector.iterator();
        while (iterator.hasNext()){
            cpDailyTask task = iterator.next();
            System.out.println(task.getUsername());
            if (task.getUsername().equals(username)){
                return new CommonResult(888,"该今日校园账号已经存在任务队列，请勿重复添加");
            }
        }

        //每次添加前都要消耗一个代理ip呜呜呜呜！！ 这里要注意执行队列的时候不能添加 会引起冲突
        cpDailyUtils.setProxy();
        cpDailyUtils cpDailyUtils = new cpDailyUtils();
        try {
            cpDailyUtils.login(username,password);
        }catch (Exception e) {
            e.printStackTrace();
            switch (e.getMessage()) {
                case "账号或密码错误":
                    return new CommonResult(888,"账号或密码错误",null);
                default:
                    //代理ip异常应该是
                    return new CommonResult(888,"未知异常，请重试",null);
            }
        }

        AllQueue.cpDailyTasksVector.add(new cpDailyTask(username, password,qq));
        //添加用户
        CpDailyUser cpDailyUser = new CpDailyUser();
        cpDailyUser.setUsername(username);
        cpDailyUser.setPassword(password);
        cpDailyUser.setQq(qq);
        mongoTemplate.save(cpDailyUser,"cpDailyUserList");
        //卡密已经用过
        Card newCard = new Card();
        newCard.set_id(card.get_id());
        newCard.setCardId(card.getCardId());
        newCard.setUse(true);
        mongoTemplate.save(newCard,"card");

        return new CommonResult(200, "添加任务队列成功！", null);
    }
}
