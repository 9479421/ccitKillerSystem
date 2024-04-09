package vip.wqby.ccitserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import vip.wqby.ccitserver.pojo.Card;
import vip.wqby.ccitserver.pojo.CommonResult;
import vip.wqby.ccitserver.pojo.CpDailyUser;
import vip.wqby.ccitserver.pojo.QndxxUser;
import vip.wqby.ccitserver.pojo.task.cpDailyTask;
import vip.wqby.ccitserver.pojo.task.qndxxTask;
import vip.wqby.ccitserver.queue.AllQueue;
import vip.wqby.ccitserver.service.QndxxService;
import vip.wqby.ccitserver.util.cpDailyUtils;
import vip.wqby.ccitserver.util.qndxxUtils;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.Vector;

@Service
public class QndxxServiceImpl implements QndxxService {
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public CommonResult testSession(String session) {
        session = session.replace("\n","");

        try {
            qndxxUtils qndxxUtils = new qndxxUtils();
            return new CommonResult<>(200,"获取成功",qndxxUtils.testSession(session));
        }catch (Exception e){
            return new CommonResult<>(404,"Session无效",null);
        }
    }

    @Override
    public CommonResult addTask_qndxx(String session, String qq, String phone, String cardID) {
        session = session.replace("\n","");

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

        Vector<qndxxTask> qndxxTasksVector = (Vector<qndxxTask>) AllQueue.qndxxTasksVector.clone();
        Iterator<qndxxTask> iterator = qndxxTasksVector.iterator();
        while (iterator.hasNext()){
            qndxxTask task = iterator.next();
            System.out.println(task.getSession());
            if (task.getSession().equals(session)){
                return new CommonResult(888,"该Session已经存在任务队列，请勿重复添加");
            }
        }

        AllQueue.qndxxTasksVector.add(new qndxxTask(session,qq,phone));
        //添加用户
        QndxxUser qndxxUser = new QndxxUser();
        qndxxUser.setSession(session);
        qndxxUser.setQq(qq);
        qndxxUser.setPhone(phone);
        mongoTemplate.save(qndxxUser,"qndxxUserList");
        //卡密已经用过
        Card newCard = new Card();
        newCard.set_id(card.get_id());
        newCard.setCardId(card.getCardId());
        newCard.setUse(true);
        mongoTemplate.save(newCard,"card");

        return new CommonResult(200, "添加任务队列成功！", null);
    }
}
