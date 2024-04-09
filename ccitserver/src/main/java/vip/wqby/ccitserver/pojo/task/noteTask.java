package vip.wqby.ccitserver.pojo.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class noteTask {
    private String phone;
    private String templateId;
    private String[] args;
}
