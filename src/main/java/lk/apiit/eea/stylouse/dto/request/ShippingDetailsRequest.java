package lk.apiit.eea.stylouse.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShippingDetailsRequest {
    String address;
    String city;
    String postalCode;
    String paymentMethod;
}
