package com.zosh.service;

import com.zosh.exception.ProductException;
import com.zosh.model.Cart;
import com.zosh.model.CartItem;
import com.zosh.model.Product;
import com.zosh.model.User;
import com.zosh.repository.CartRepository;
import com.zosh.request.AdditemRequest;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImplementation  implements CartService {

    private CartRepository cartRepository;
    private CartItemService cartItemService;
    private ProductService productService;

    public CartServiceImplementation( CartRepository cartRepository,
                                      CartItemService cartItemService,
                                      ProductService productService)
    {
        this.cartRepository=cartRepository;
        this.cartItemService=cartItemService;
        this.productService=productService;

    }

    @Override
    public Cart createCart(User user) {
        Cart cart=new Cart();
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    @Override
    public String addCartItem(Long userId, AdditemRequest req) throws ProductException {
        Cart cart=cartRepository.findByUserId(userId);

        // CRITICAL FIX: Create a new cart if one doesn't exist
        if(cart == null) {
            User user = new User();
            user.setId(userId);
            cart = createCart(user);
        }

        Product product=productService.findProductById(req.getProductId());

        CartItem isPresent=cartItemService.isCartItemExist(cart,product, req.getSize(), userId);


        if(isPresent==null)
        {
            CartItem cartItem=new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(cart);
            cartItem.setQuantity(req.getQuantity());
            cartItem.setUserId(userId);

            int price = req.getQuantity() * product.getPrice();
            int discountedPrice = req.getQuantity() * product.getDiscountedPrice();
            cartItem.setPrice(price);
            cartItem.setDiscountedPrice(discountedPrice);

            cartItem.setSize(req.getSize());

            CartItem createdCartItem=cartItemService.createCartItem(cartItem);
            cart.getCartItems().add(createdCartItem);
        }

        cartRepository.save(cart);
        findUserCart(userId);

        return "Item ADDED to Cart";
    }

    @Override
    public Cart findUserCart(Long userId) {
        Cart cart=cartRepository.findByUserId(userId);

        // CRITICAL FIX: Create a new cart if one doesn't exist
        if(cart == null) {
            User user = new User();
            user.setId(userId);
            cart = createCart(user);
        }

        int totalPrice=0;
        int totalDiscountedPrice=0;
        int totalItem=0;

        for(CartItem cartItem: cart.getCartItems())
        {
            totalPrice += cartItem.getPrice();
            totalDiscountedPrice += cartItem.getDiscountedPrice();
            totalItem += cartItem.getQuantity();

        }
        cart.setTotalDiscountedPrice(totalDiscountedPrice);
        cart.setTotalItem(totalItem);
        cart.setTotalPrice(totalPrice);
        cart.setDiscount(totalPrice-totalDiscountedPrice);

        return cartRepository.save(cart);
    }
}