package com.ll.exam.sen_books.app.post.service;

import com.ll.exam.sen_books.app.member.entity.Member;
import com.ll.exam.sen_books.app.post.entity.Post;
import com.ll.exam.sen_books.app.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;

    @Transactional
    public Post write(Member author, String subject, String content) {
        Post product = Post.builder()
                .subject(subject)
                .author(author)
                .content(content)
                .build();

        postRepository.save(product);

        return product;
    }

    public Optional<Post> findById(long id) {
        return postRepository.findById(id);
    }

    @Transactional
    public void modify(Post product, String subject, String content) {
        product.setSubject(subject);
        product.setContent(content);
    }

    public Optional<Post> findForPrintById(long id) {
        Optional<Post> post = findById(id);

        if (post.isEmpty()) return post;

        return post;
    }

    public boolean authorCanModify(Member author, Post post) {
        return author.getId().equals(post.getAuthor().getId());
    }

    public void delete(Post post) {
        postRepository.delete(post);
    }

    public List<Post> findAllByAuthorId(Long id) {
        return postRepository.findAllByAuthorId(id);
    }
}
