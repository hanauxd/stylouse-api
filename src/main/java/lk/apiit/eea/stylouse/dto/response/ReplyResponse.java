package lk.apiit.eea.stylouse.dto.response;

import lk.apiit.eea.stylouse.models.Reply;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyResponse {
    private List<Reply> replies = new ArrayList<>();
}
