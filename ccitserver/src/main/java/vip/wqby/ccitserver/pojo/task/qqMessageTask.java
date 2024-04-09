package vip.wqby.ccitserver.pojo.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.mamoe.mirai.message.data.MessageChain;

@Data
@NoArgsConstructor
public class qqMessageTask {
    private int type;
    private long group;
    private long qq;
    private MessageChain data;
    //群消息
    public qqMessageTask(long group,MessageChain data){
        type = 1;
        this.group = group;
        this.data = data;
    }
    //临时消息
    public qqMessageTask(long group,long qq,MessageChain data){
        type = 2;
        this.group = group;
        this.qq = qq;
        this.data = data;
    }

}
