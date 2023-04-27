package com.ll.exam.sen_books.app.cart.controller;

import com.ll.exam.sen_books.app.cart.entity.CartItem;
import com.ll.exam.sen_books.app.cart.service.CartService;
import com.ll.exam.sen_books.app.member.entity.Member;
import com.ll.exam.sen_books.app.product.entity.Product;
import com.ll.exam.sen_books.app.product.service.ProductService;
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
    private final ProductService productService;

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public String cartList(@AuthenticationPrincipal MemberContext memberContext, Model model) {

        List<CartItem> cartItems = cartService.findAllByMemberIdOrderByIdDesc(memberContext.getId());

        model.addAttribute("cartItems", cartItems);

        return "cart/list";
    }

    @GetMapping("/remove/{productId}")
    @PreAuthorize("isAuthenticated()")
    public String removeCartItem(@AuthenticationPrincipal MemberContext memberContext, @PathVariable Long productId) {
        Member member = memberContext.getMember();
        Product product = productService.findById(productId).get();

        cartService.removeItem(member, product);

        return "redirect:/cart/list?msg=" + Ut.url.encode("%d번 품목을 삭제하였습니다.".formatted(productId));
    }

    @PostMapping("/removeItems")
    @PreAuthorize("isAuthenticated()")
    public String removeItems(@AuthenticationPrincipal MemberContext memberContext,String ids) {
        Member member = memberContext.getMember();
        String[] array = ids.split(",");

        Arrays.stream(array)
                .mapToLong(Long::parseLong)
                .forEach(id -> {
                    CartItem cartItem = cartService.findItemById(id).orElse(null);

                    cartService.removeItem(member, cartItem);

                });

        return "redirect:/cart/items?msg=" + Ut.url.encode("%d건의 품목을 삭제하였습니다.".formatted(array.length));
    }

    @PostMapping("/add/{productId}")
    @PreAuthorize("isAuthenticated()")
    public String addItem(@AuthenticationPrincipal MemberContext memberContext, @PathVariable Long productId) {
        Member buyer = memberContext.getMember();
        Product product = productService.findById(productId).get();

        cartService.addItem(buyer, product);

        return "redirect:/cart/list?msg=" + Ut.url.encode("%d번 품목을 추가하였습니다.".formatted(productId));
    }
}
