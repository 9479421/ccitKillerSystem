package vip.wqby.ccitserver.pojo.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class emailTask {
    private String subject;
    private String body;
    private String to;

}
