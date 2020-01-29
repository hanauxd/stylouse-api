package lk.apiit.eea.stylouse.controllers;

import lk.apiit.eea.stylouse.dto.request.AddressRequest;
import lk.apiit.eea.stylouse.models.Address;
import lk.apiit.eea.stylouse.models.User;
import lk.apiit.eea.stylouse.services.AddressService;
import lk.apiit.eea.stylouse.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addresses")
public class AddressController {
    private AddressService addressService;
    private UserService userService;

    @Autowired
    public AddressController(AddressService addressService, UserService userService) {
        this.addressService = addressService;
        this.userService = userService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping
    public ResponseEntity<?> createAddress(@RequestBody AddressRequest request, Authentication auth) {
        User user = userService.getUserByEmail(auth.getName());
        Address address = addressService.createAddress(request, user);
        return new ResponseEntity<>(address, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable String id, @RequestBody AddressRequest request) {
        Address address = addressService.updateAddress(id, request);
        return ResponseEntity.ok(address);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable String id, Authentication auth) {
        User user = userService.getUserByEmail(auth.getName());
        addressService.deleteAddress(id);
        List<Address> addresses = addressService.getAddressesByUser(user);
        return ResponseEntity.ok(addresses);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    public ResponseEntity<?> getAddressesByUser(Authentication auth) {
        User user = userService.getUserByEmail(auth.getName());
        List<Address> addresses = addressService.getAddressesByUser(user);
        return ResponseEntity.ok(addresses);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getAddressById(@PathVariable String id) {
        Address address = addressService.getAddressById(id);
        return ResponseEntity.ok(address);
    }
}
