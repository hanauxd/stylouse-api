package lk.apiit.eea.stylouse.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InquiryRequest {
    private String productIdOrInquiryId;
    private String message;
}
