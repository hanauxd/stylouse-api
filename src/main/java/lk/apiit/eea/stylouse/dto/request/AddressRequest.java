package lk.apiit.eea.stylouse.dto.request;

import lk.apiit.eea.stylouse.models.Address;
import lk.apiit.eea.stylouse.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequest {
    String name;
    String phone;
    String address;
    String country;
    String city;
    String postalCode;

    public Address getShippingAddress(User user) {
        Address address = new Address();
        setAddressDetails(address);
        address.setUser(user);
        return address;
    }

    public Address setAddressDetails(Address address) {
        address.setName(this.name);
        address.setPhone(this.phone);
        address.setAddress(this.address);
        address.setCountry(this.country);
        address.setCity(this.city);
        address.setPostalCode(this.postalCode);
        return address;
    }
}
