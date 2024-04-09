package vip.wqby.ccitserver.pojo;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Card {
    private String _id;
    private String cardId;
    private boolean isUse;
}
