package lk.apiit.eea.stylouse.services;

import lk.apiit.eea.stylouse.dto.request.WishlistRequest;
import lk.apiit.eea.stylouse.exceptions.CustomException;
import lk.apiit.eea.stylouse.models.Product;
import lk.apiit.eea.stylouse.models.User;
import lk.apiit.eea.stylouse.models.Wishlist;
import lk.apiit.eea.stylouse.repositories.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistService {
    private WishlistRepository wishlistRepository;
    private ProductService productService;

    @Autowired
    public WishlistService(WishlistRepository wishlistRepository, ProductService productService) {
        this.wishlistRepository = wishlistRepository;
        this.productService = productService;
    }

    public List<Wishlist> getWishListsByUser(User user) {
        return wishlistRepository.findByUser(user);
    }

    public Wishlist getWishListById(String id) {
        return wishlistRepository.findById(id).orElseThrow(
                () -> new CustomException("Wishlist item not found.", HttpStatus.NOT_FOUND)
        );
    }

    public Wishlist getWishListByUserAndProduct(User user, Product product) {
        return wishlistRepository.findByUserAndProduct(user, product);
    }

    public List<Wishlist> createWishList(WishlistRequest request, User user) {
        Product product = productService.getProductById(request.getProductId());
        if (getWishListByUserAndProduct(user, product) != null) {
            throw new CustomException("Product already exist in wishlist.", HttpStatus.BAD_REQUEST);
        } else {
            Wishlist wishList = new Wishlist(user, product);
            wishlistRepository.save(wishList);
            return getWishListsByUser(user);
        }
    }

    public void removeWishList(String id) {
        Wishlist wishList = getWishListById(id);
        wishList.setProduct(null);
        wishList.setUser(null);
        wishlistRepository.delete(wishList);
    }
}