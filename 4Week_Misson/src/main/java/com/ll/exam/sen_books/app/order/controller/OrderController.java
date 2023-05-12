package com.ll.exam.sen_books.app.order.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.exam.sen_books.app.member.entity.Member;
import com.ll.exam.sen_books.app.member.service.MemberService;
import com.ll.exam.sen_books.app.order.entity.Order;
import com.ll.exam.sen_books.app.order.service.OrderService;
import com.ll.exam.sen_books.app.security.dto.MemberContext;
import com.ll.exam.sen_books.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper;
    private final MemberService memberService;

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String createOrder(@AuthenticationPrincipal MemberContext memberContext, String ids) {
        Member member = memberContext.getMember();
        Order order = orderService.createOrder(member, ids);

        return "redirect:/order/%d".formatted(order.getId()) + "?msg=" + Ut.url.encode("%d번 주문이 생성되었습니다.".formatted(order.getId()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public String showDetail(@AuthenticationPrincipal MemberContext memberContext, @PathVariable long id, Model model) {
        Order order = orderService.findForPrintById(id).get();

        Member member = memberContext.getMember();

        long restCash = memberService.getRestCash(member);

        if (orderService.memberCanSee(member, order) == false) {
            throw new RuntimeException();
        }

        model.addAttribute("order", order);
        model.addAttribute("actorRestCash", restCash);

        return "order/detail";
    }

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public String orderList(@AuthenticationPrincipal MemberContext memberContext, Model model) {
        Member buyer = memberContext.getMember();

        List<Order> orders = orderService.getOrders(buyer);

        model.addAttribute("orders", orders);

        return "order/list";
    }

    @GetMapping("/{id}/cancel")
    @PreAuthorize("isAuthenticated()")
    public String removeOrder(@AuthenticationPrincipal MemberContext memberContext, @PathVariable Long id) {
        Order order = orderService.findById(id).get();
        Member member = memberContext.getMember();

        if(orderService.canCancel(member, order) == false) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        orderService.removeOrder(order);

        return "redirect:/order/list?msg=" + Ut.url.encode("%d번 품목을 삭제하였습니다.".formatted(id));
    }

    @PostMapping("/{id}/payByRestCashOnly")
    @PreAuthorize("isAuthenticated()")
    public String payByRestCashOnly(@AuthenticationPrincipal MemberContext memberContext,@PathVariable Long id) {
        Member member = memberContext.getMember();
        Order order = orderService.findById(id).get();
        long restCash = memberService.getRestCash(member);

        if (!orderService.canPayment(member, order)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        orderService.payByRestCashOnly(order);

        return "redirect:/order/%d?msg=".formatted(order.getId()) + Ut.url.encode(("%d번 품목을 예치금으로 결제하였습니다.").formatted(id));
    }

    // Toss Payments 시작
    @PostConstruct
    private void init() {
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response) {
            }
        });
    }

    @Value("${custom.tossPayments.secretKey}")
    private String SECRET_KEY;

    @RequestMapping("/{id}/success")
    public String confirmPayment(
            @PathVariable long id,
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam int amount,
            Model model,
            @AuthenticationPrincipal MemberContext memberContext
    ) throws Exception {

        Order order = orderService.findForPrintById(id).get();

        long realOrderId = Long.parseLong(orderId.split("__")[1]);

        if (id != realOrderId) {
            return "redirect:/order/fail?msg=" + Ut.url.encode( "기존 주문과 일치하지 않습니다.");
        }

        HttpHeaders headers = new HttpHeaders();
        // headers.setBasicAuth(SECRET_KEY, ""); // spring framework 5.2 이상 버전에서 지원
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> payloadMap = new HashMap<>();
        payloadMap.put("orderId", orderId);
        payloadMap.put("amount", String.valueOf(amount));

        Member actor = memberContext.getMember();
        long restCash = memberService.getRestCash(actor);
        int payPriceRestCash = order.calculatePayPrice() - amount;

        if (payPriceRestCash > restCash) {
            return "redirect:/order/fail?msg=" + Ut.url.encode( "예치금이 부족합니다.");
        }

        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(payloadMap), headers);

        ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(
                "https://api.tosspayments.com/v1/payments/" + paymentKey, request, JsonNode.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {

            orderService.payByTossPayments(order, payPriceRestCash);

            return "redirect:/order/%d".formatted(order.getId());
        } else {
            JsonNode failNode = responseEntity.getBody();
            model.addAttribute("message", failNode.get("message").asText());
            model.addAttribute("code", failNode.get("code").asText());
            return "order/fail";
        }
    }

    @RequestMapping("/{id}/fail")
    public String failPayment(@RequestParam String message, @RequestParam String code, Model model) {
        model.addAttribute("message", message);
        model.addAttribute("code", code);
        return "order/fail";
    }

    @PostMapping("/{orderId}/cancel")
    @PreAuthorize("isAuthenticated()")
    public String cancel(@PathVariable Long orderId) {
        orderService.cancel(orderId);

        return "redirect:/order/%d?msg=".formatted(orderId) + Ut.url.encode("결제를 취소했습니다.");
    }

    @PostMapping("/{orderId}/refund")
    @PreAuthorize("isAuthenticated()")
    public String refund(@PathVariable Long orderId, @AuthenticationPrincipal MemberContext memberContext) {
        Order order = orderService.findById(orderId).orElse(null);
        Member member = memberContext.getMember();

        if(!order.isRefundable()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        if (!orderService.canRefund(member, order)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        orderService.refundByRestCashOnly(order);

        return "redirect:/order/%d?msg=".formatted(orderId) + Ut.url.encode("환불되었습니다.");
    }
}
