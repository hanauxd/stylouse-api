package lk.apiit.eea.stylouse.controllers;

import lk.apiit.eea.stylouse.dto.request.WishlistRequest;
import lk.apiit.eea.stylouse.models.User;
import lk.apiit.eea.stylouse.models.Wishlist;
import lk.apiit.eea.stylouse.services.UserService;
import lk.apiit.eea.stylouse.services.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/wishlist")
public class WishlistController {
    private WishlistService wishlistService;
    private UserService userService;

    @Autowired
    public WishlistController(WishlistService wishlistService, UserService userService) {
        this.wishlistService = wishlistService;
        this.userService = userService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping
    public ResponseEntity<?> createWishList(@RequestBody WishlistRequest request, Authentication auth) {
        User user = userService.getUserByEmail(auth.getName());
        List<Wishlist> wishlists = wishlistService.createWishList(request, user);
        return ResponseEntity.ok(wishlists);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeWishList(@PathVariable String id, Authentication auth) {
        wishlistService.removeWishList(id);
        return getWishListsByUser(auth);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    public ResponseEntity<?> getWishListsByUser(Authentication auth) {
        User user = userService.getUserByEmail(auth.getName());
        List<Wishlist> wishlists = wishlistService.getWishListsByUser(user);
        return ResponseEntity.ok(wishlists);
    }
}