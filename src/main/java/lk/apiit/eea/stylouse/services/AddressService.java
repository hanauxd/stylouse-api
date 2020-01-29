package lk.apiit.eea.stylouse.services;

import lk.apiit.eea.stylouse.dto.request.AddressRequest;
import lk.apiit.eea.stylouse.exceptions.CustomException;
import lk.apiit.eea.stylouse.models.Address;
import lk.apiit.eea.stylouse.models.User;
import lk.apiit.eea.stylouse.repositories.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class AddressService {
    private AddressRepository addressRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Address getAddressById(String id) {
        return addressRepository.findById(id).orElseThrow(
                () -> new CustomException("Address not found.", HttpStatus.BAD_REQUEST)
        );
    }

    public List<Address> getAddressesByUser(User user) {
        return addressRepository.findByUser(user);
    }

    public Address createAddress(AddressRequest request, User user) {
        Address address = request.getShippingAddress(user);
        user.getAddresses().add(address);
        return addressRepository.save(address);
    }

    @Transactional
    public Address updateAddress(String id, AddressRequest request) {
        Address address = getAddressById(id);
        return request.setAddressDetails(address);
    }

    public void deleteAddress(String id) {
        Address address = getAddressById(id);
        address.getUser().removeAddress(address);
        addressRepository.delete(address);
    }
}
