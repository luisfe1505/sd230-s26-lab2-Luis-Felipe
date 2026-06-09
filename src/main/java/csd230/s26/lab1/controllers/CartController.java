package csd230.s26.lab1.controllers;

import csd230.s26.lab1.entities.*;
import csd230.s26.lab1.repositories.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/cart")
public class CartController {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public CartController(CartRepository cartRepository, ProductRepository productRepository, OrderRepository orderRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

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

        double total = 0;
        for (ProductEntity p : cart.getProducts()) {
            total += p.getPrice();
        }
        model.addAttribute("total", total);

        return "cartDetails";
    }

    @GetMapping("/add/{productId}")
    public String addToCart(@PathVariable Long productId, HttpServletRequest request) {
        Long defaultCartId = 1L;
        CartEntity cart = cartRepository.findById(defaultCartId).orElse(null);
        ProductEntity product = productRepository.findById(productId).orElse(null);

        if (cart != null && product != null) {
            cart.addProduct(product);
            cartRepository.save(cart);
        }

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/books");
    }

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

    @Transactional
    @PostMapping("/checkout")
    public String checkout(Model model) {
        Long defaultCartId = 1L;
        CartEntity cart = cartRepository.findById(defaultCartId).orElse(null);

        if (cart == null || cart.getProducts().isEmpty()) {
            return "redirect:/cart";
        }

        OrderEntity order = new OrderEntity();
        double total = 0;

        for (ProductEntity product : cart.getProducts()) {
            order.getProducts().add(product);
            total += product.getPrice();

            if (product instanceof PublicationEntity) {
                PublicationEntity publication = (PublicationEntity) product;
                publication.setCopies(publication.getCopies() - 1);
                productRepository.save(publication);
            }
        }

        order.setTotalAmount(total);
        orderRepository.save(order);

        cart.getProducts().clear();
        cartRepository.save(cart);

        model.addAttribute("order", order);
        return "orderDetails";
    }
}