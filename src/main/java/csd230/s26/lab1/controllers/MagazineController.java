package csd230.s26.lab1.controllers;

import csd230.s26.lab1.entities.MagazineEntity;
import csd230.s26.lab1.entities.CartEntity;
import csd230.s26.lab1.repositories.MagazineRepository;
import csd230.s26.lab1.repositories.CartRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/magazines")
public class MagazineController {
    private final MagazineRepository magazineRepository;
    private final CartRepository cartRepository;

    public MagazineController(MagazineRepository magazineRepository, CartRepository cartRepository) {
        this.magazineRepository = magazineRepository;
        this.cartRepository = cartRepository;
    }

    @GetMapping
    public String getAllMagazines(Model model) {
        model.addAttribute("magazines", magazineRepository.findAll());
        return "magazineList";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("magazine", new MagazineEntity());
        return "addMagazine";
    }

    @PostMapping("/save")
    public String saveMagazine(@ModelAttribute MagazineEntity magazine) {
        magazineRepository.save(magazine);
        return "redirect:/magazines";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        MagazineEntity magazine = magazineRepository.findById(id).orElse(null);
        if (magazine != null) {
            model.addAttribute("magazine", magazine);
            return "addMagazine";
        }
        return "redirect:/magazines";
    }

    @GetMapping("/delete/{id}")
    public String deleteMagazine(@PathVariable Long id) {
        MagazineEntity magazine = magazineRepository.findById(id).orElse(null);
        if (magazine != null) {
            for (CartEntity cart : magazine.getCarts()) {
                cart.getProducts().remove(magazine);
                cartRepository.save(cart);
            }
            magazineRepository.deleteById(id);
        }
        return "redirect:/magazines";
    }
}