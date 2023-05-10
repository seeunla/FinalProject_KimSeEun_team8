package com.ll.exam.sen_books.app.mybook.service;

import com.ll.exam.sen_books.app.member.entity.Member;
import com.ll.exam.sen_books.app.mybook.entity.MyBook;
import com.ll.exam.sen_books.app.mybook.repository.MyBookRepository;
import com.ll.exam.sen_books.app.order.entity.Order;
import com.ll.exam.sen_books.app.order.entity.OrderItem;
import com.ll.exam.sen_books.app.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyBookService {
    private final MyBookRepository myBookRepository;

    @Transactional
    public void add(Order order) {
        for(OrderItem orderItem : order.getOrderItems()) {
            Product product = orderItem.getProduct();
            Member buyer = order.getBuyer();

            MyBook oldMyBook = myBookRepository.findByProductIdAndOwnerId(product.getId(), buyer.getId()).orElse(null);
            // (ownerId + productId) DB에 없을 때만 저장(중복 저장 막기)
            if(oldMyBook == null) {
                MyBook myBook = MyBook.builder()
                        .owner(buyer)
                        .product(product)
                        .build();
                myBookRepository.save(myBook);
            }
        }
    }

    @Transactional
    public void remove(Order order) {
        for(OrderItem orderItem : order.getOrderItems()) {
            Product product = orderItem.getProduct();
            Member buyer = order.getBuyer();

            myBookRepository.deleteByProductIdAndOwnerId(product.getId(), buyer.getId());
        }
    }
}
