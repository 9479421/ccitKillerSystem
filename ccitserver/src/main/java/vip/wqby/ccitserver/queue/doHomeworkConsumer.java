package vip.wqby.ccitserver.queue;

import lombok.SneakyThrows;
import vip.wqby.ccitserver.pojo.task.homeworkTask;
import vip.wqby.ccitserver.util.IbsUtils;

public class doHomeworkConsumer extends Thread {

    @Override
    public void run() {
        for (; ; ) {
            homeworkTask poll = null;
            try {
                poll = AllQueue.homeworkQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (poll != null) {
                IbsUtils ibsUtils = new IbsUtils();
                ibsUtils.doHomeWork(poll.getUsername(), poll.getPassword(), poll.getBusinessID(), poll.getTime());
            }
        }
    }
}
