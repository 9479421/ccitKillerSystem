package vip.wqby.ccitserver.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QndxxUser {
    private String _id;
    private String session;
    private String qq;
    private String phone;
}
