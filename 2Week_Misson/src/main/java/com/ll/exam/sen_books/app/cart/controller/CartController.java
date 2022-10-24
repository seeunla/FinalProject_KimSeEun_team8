package com.ll.exam.sen_books.app.cart.controller;

import com.ll.exam.sen_books.app.cart.entity.CartItem;
import com.ll.exam.sen_books.app.cart.service.CartService;
import com.ll.exam.sen_books.app.member.entity.Member;
import com.ll.exam.sen_books.app.security.dto.MemberContext;
import com.ll.exam.sen_books.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    @GetMapping("/items")
    @PreAuthorize("isAuthenticated()")
    public String showItems(@AuthenticationPrincipal MemberContext memberContext, Model model) {
        Member buyer = memberContext.getMember();

        List<CartItem> items = cartService.getItemsByBuyer(buyer);

        model.addAttribute("items", items);

        return "cart/items";
    }

    @PostMapping("/remove/{productId}")
    @PreAuthorize("isAuthenticated()")
    public String removeItems(@AuthenticationPrincipal MemberContext memberContext, @PathVariable Long productId) {
        Member buyer = memberContext.getMember();

        CartItem cartItem = cartService.findItemById(productId).orElse(null);

        if (cartService.actorCanDelete(buyer, cartItem)) {
            cartService.removeItem(cartItem);
        }

        return "redirect:/cart/items?msg=" + Ut.url.encode("%d번 품목을 삭제하였습니다.".formatted(productId));
    }
}
