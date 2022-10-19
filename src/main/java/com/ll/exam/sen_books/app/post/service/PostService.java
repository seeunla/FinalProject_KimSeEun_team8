package com.ll.exam.sen_books.app.post.service;

import com.ll.exam.sen_books.app.member.entity.Member;
import com.ll.exam.sen_books.app.post.entity.Post;
import com.ll.exam.sen_books.app.post.form.PostForm;
import com.ll.exam.sen_books.app.post.repository.PostRepository;
import com.ll.exam.sen_books.util.Ut;
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
    public Post write(Member author, PostForm postForm) {
        Post post = Post.builder()
                .subject(postForm.getSubject())
                .author(author)
                .contentHtml(Ut.markdown(postForm.getContent()))
                .content(postForm.getContent())
                .build();

        postRepository.save(post);

        return post;
    }

    public Optional<Post> findById(long id) {
        return postRepository.findById(id);
    }

    @Transactional
    public void modify(Post post, String subject, String content) {
        post.modify(subject, content, Ut.markdown(content));
    }

    public Optional<Post> findForPrintById(long id) {
        Optional<Post> post = findById(id);

        if (post.isEmpty()) return post;

        return post;
    }

    @Transactional
    public void delete(Post post) {
        postRepository.delete(post);
    }

    public List<Post> findAllByAuthorId(Long id) {
        return postRepository.findAllByAuthorId(id);
    }
}
