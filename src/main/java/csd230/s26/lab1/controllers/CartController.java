package csd230.s26.lab1.controllers;

import csd230.s26.lab1.entities.CartEntity;
import csd230.s26.lab1.entities.ProductEntity;
import csd230.s26.lab1.repositories.CartRepository;
import csd230.s26.lab1.repositories.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartController(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    // 1. VIEW CART
    @GetMapping
    public String viewCart(Model model) {
        Long defaultCartId = 1L;
        CartEntity cart = cartRepository.findById(defaultCartId)
                .orElseGet(() -> {
                    CartEntity newCart = new CartEntity();
                    newCart.setId(defaultCartId);
                    return cartRepository.save(newCart);
                });
        model.addAttribute("cart", cart);
        return "cartDetails";
    }

    // 2. ADD TO CART
    @GetMapping("/add/{productId}")
    public String addToCart(@PathVariable Long productId) {
        Long defaultCartId = 1L;
        CartEntity cart = cartRepository.findById(defaultCartId).orElse(null);
        ProductEntity product = productRepository.findById(productId).orElse(null);

        if (cart != null && product != null) {
            cart.addProduct(product); // Uses the helper method from Lab 1
            cartRepository.save(cart); // Persists the link in the join table
        }
        return "redirect:/books";
    }

    // 3. REMOVE FROM CART
    @GetMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId) {
        Long defaultCartId = 1L;
        CartEntity cart = cartRepository.findById(defaultCartId).orElse(null);
        ProductEntity product = productRepository.findById(productId).orElse(null);

        if (cart != null && product != null) {
            cart.getProducts().remove(product);
            cartRepository.save(cart);
        }
        return "redirect:/cart";
    }
}

