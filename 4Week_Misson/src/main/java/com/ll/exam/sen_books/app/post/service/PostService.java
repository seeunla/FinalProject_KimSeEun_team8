package com.ll.exam.sen_books.app.post.service;

import com.ll.exam.sen_books.app.hashTag.entity.HashTag;
import com.ll.exam.sen_books.app.hashTag.service.HashTagService;
import com.ll.exam.sen_books.app.member.entity.Member;
import com.ll.exam.sen_books.app.post.entity.Post;
import com.ll.exam.sen_books.app.post.form.PostForm;
import com.ll.exam.sen_books.app.post.repository.PostRepository;
import com.ll.exam.sen_books.util.Ut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final HashTagService hashTagService;

    public Post write(Member author, PostForm postForm) {
        Post post = Post.builder()
                .subject(postForm.getSubject())
                .author(author)
                .contentHtml(Ut.markdown(postForm.getContent()))
                .content(postForm.getContent())
                .build();

        postRepository.save(post);

        String keywords = postForm.getKeywords();
        log.info("키워드: "+keywords);
        if(keywords != null) {
            hashTagService.applyHashTags(post, postForm.getKeywords());
        }

        return post;
    }

    public Optional<Post> findById(long id) {
        return postRepository.findById(id);
    }

    public void modify(Post post, PostForm postForm) {
        post.modify(postForm.getSubject(), post.getContent(), postForm.getContentHtml());

        String keywords = postForm.getKeywords();
        if(keywords != null) {
            hashTagService.applyHashTags(post, keywords);
        }
        postRepository.save(post);
    }

    public Post findForPrintById(long id) {
        Post post = findById(id).orElse(null);

        List<HashTag> hashTags = hashTagService.getHashTags(post);

        return post;
    }

    @Transactional
    public void delete(Post post) {
        postRepository.delete(post);
    }

    public List<Post> findAllByAuthorId(Long id) {
        return postRepository.findAllByAuthorId(id);
    }

    public List<Post> search(Member author, String kwType, String kw) {
        return postRepository.searchQsl(author, kwType, kw);
    }
}
