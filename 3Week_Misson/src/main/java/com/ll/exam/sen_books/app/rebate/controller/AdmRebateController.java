package com.ll.exam.sen_books.app.rebate.controller;

import com.ll.exam.sen_books.app.order.entity.OrderItem;
import com.ll.exam.sen_books.app.order.service.OrderService;
import com.ll.exam.sen_books.app.rebate.entity.RebateOrderItem;
import com.ll.exam.sen_books.app.rebate.service.RebateService;
import com.ll.exam.sen_books.util.Ut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/adm/rebate")
@Slf4j
public class AdmRebateController {
    private final RebateService rebateService;

    @PostMapping("/makeData")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseBody
    public String makeData(String yearMonth) {
        rebateService.makeDate(yearMonth);

        return "redirect:/adm/rebate/rebateOrderItemList?msg=" + Ut.url.encode("정산데이터가 성공적으로 생성되었습니다.");
    }

    @GetMapping("/rebateOrderItemList")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showRebateOrderItemList(String yearMonth, Model model) {
        if (yearMonth == null) {
            yearMonth = "2022-10";
        }

        List<RebateOrderItem> items = rebateService.findRebateOrderItemsByPayDateIn(yearMonth);

        model.addAttribute("items", items);

        return "adm/rebate/rebateOrderItemList";
    }

    @PostMapping("/rebateOne/{orderItemId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseBody
    public String rebateOne(@PathVariable long orderItemId, HttpServletRequest req) {
        rebateService.rebate(orderItemId);

        String referer = req.getHeader("Referer");
        log.debug("referer : " + referer);

        RebateOrderItem orderItem = rebateService.findByOrderItemId(orderItemId);

        int calculateRebatePrice = orderItem.calculateRebatePrice();

        String yearMonth = Ut.url.getQueryParamValue(referer, "yearMonth", "");

        return "/adm/rebate/rebateOrderItemList?yearMonth="
                + yearMonth
                + "?msg=" + Ut.url.encode("주문품목번호 %d번에 대해서 판매자에게 %s원 정산을 완료하였습니다.".formatted(orderItemId, calculateRebatePrice));
    }
}
