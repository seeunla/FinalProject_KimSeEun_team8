package com.ll.exam.sen_books.app.product.controller;

import com.ll.exam.sen_books.app.member.entity.Member;
import com.ll.exam.sen_books.app.post.entity.Post;
import com.ll.exam.sen_books.app.post.service.PostService;
import com.ll.exam.sen_books.app.postKeyword.dto.PostKeywordDto;
import com.ll.exam.sen_books.app.postKeyword.service.PostKeywordService;
import com.ll.exam.sen_books.app.product.entity.Product;
import com.ll.exam.sen_books.app.product.form.ProductForm;
import com.ll.exam.sen_books.app.product.form.ProductModifyForm;
import com.ll.exam.sen_books.app.product.service.ProductService;
import com.ll.exam.sen_books.app.security.dto.MemberContext;
import com.ll.exam.sen_books.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final PostService postService;
    private final PostKeywordService postKeywordService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String create(@AuthenticationPrincipal MemberContext memberContext, Model model) {

        List<PostKeywordDto> postKeywords = postKeywordService.findByMemberId(memberContext.getId());

        model.addAttribute("postKeywords", postKeywords);

        return "product/create";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String create(@AuthenticationPrincipal MemberContext memberContext, @Valid ProductForm productForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "/product/create";
        }
        Member author = memberContext.getMember();

        Product product = productService.create(author, productForm);
        return "redirect:/product/" + product.getId() + "?msg=" + Ut.url.encode("%d번 상품이 생성되었습니다.".formatted(product.getId()));
    }


    @GetMapping("/list")
    public String showList(Model model) {

        List<Product> products = productService.findAllByOrderByIdDesc();

        model.addAttribute("products", products);

        return "product/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public String detail(@PathVariable long id, Model model) {
        Product product = productService.findForPrintById(id).get();

        model.addAttribute("product", product);

        return "product/detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/modify")
    public String showModify(@AuthenticationPrincipal MemberContext memberContext, @PathVariable long id, Model model) {
        Product product = productService.findForPrintById(id).get();
        Member member = memberContext.getMember();

        // 수정 권한 검사
        if(productService.canModify(member, product) == false) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        model.addAttribute("product", product);

        return "product/modify";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/modify")
    public String modifyProduct(@Valid ProductModifyForm productModifyForm, @AuthenticationPrincipal MemberContext memberContext, @PathVariable long id, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "product/modify";
        }

        Product product = productService.findById(id).get();
        Member member = memberContext.getMember();

        if(productService.canModify(member, product) == false) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        if (product == null) {
            return "redirect:/product/" + product.getId() + "?msg=" + Ut.url.encode("%d번 상품은 존재하지 않습니다.".formatted(id));
        }

        productService.modify(product, productModifyForm);
        return "redirect:/product/" + product.getId() + "?msg=" + Ut.url.encode("%d번 상품이 수정되었습니다.".formatted(product.getId()));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/delete")
    public String deleteProduct (@PathVariable long id, @AuthenticationPrincipal MemberContext memberContext) {
        Product product = productService.findById(id).get();
        Member member = memberContext.getMember();

        // 삭제 권한 검사
        if(productService.canDelete(member, product) == false) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        if (product == null) {
            return "redirect:/product/" + product.getId() + "?msg=" + Ut.url.encode("%d번 상품은 존재하지 않습니다.".formatted(id));
        }

        productService.delete(product);

        return "redirect:/?" + Ut.url.encode("%d번 글이 삭제되었습니다.".formatted(product.getId()));
    }
}
