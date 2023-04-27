package com.ll.exam.sen_books.app.cart.service;

import com.ll.exam.sen_books.app.cart.entity.CartItem;
import com.ll.exam.sen_books.app.cart.exception.CartItemNotFoundException;
import com.ll.exam.sen_books.app.cart.repository.CartItemRepository;
import com.ll.exam.sen_books.app.member.entity.Member;
import com.ll.exam.sen_books.app.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {
    private final CartItemRepository cartItemRepository;

    @Transactional
    public CartItem addItem(Member buyer, Product product) {
        CartItem oldCartItem = cartItemRepository.findByBuyerIdAndProductId(buyer.getId(), product.getId()).orElse(null);

        if (oldCartItem != null) {
            return oldCartItem;
        }

        CartItem cartItem = CartItem.builder()
                .buyer(buyer)
                .product(product)
                .build();

        cartItemRepository.save(cartItem);

        return cartItem;
    }

    public boolean hasItem(Member buyer, Product product) {
        return cartItemRepository.existsByBuyerIdAndProductId(buyer.getId(), product.getId());
    }

    @Transactional
    public void removeItem(Member member, Product product) {
        CartItem cartItem = findByMemberIdAndProductId(member.getId(), product.getId());

        cartItemRepository.delete(cartItem);
    }

    private CartItem findByMemberIdAndProductId(Long id, Long productId) {
        return cartItemRepository.findByBuyerIdAndProductId(id, productId).orElseThrow(
                () -> {throw new CartItemNotFoundException("장바구니 품목이 존재하지 않습니다.");
                }
        );
    }


    public List<CartItem> findByMemberIdOrderByIdDesc(Member buyer, List<Long> cartItemIds) {
        return cartItemRepository.findByBuyerIdAndIdInOrderByIdDesc(buyer.getId(), cartItemIds);
    }

    public List<CartItem> findAllByMemberIdOrderByIdDesc(long buyerId) {
        return cartItemRepository.findAllByMemberIdOrderByIdDesc(buyerId);
    }

    public Optional<CartItem> findItemById(long id) {
        return cartItemRepository.findById(id);
    }

}
