package com.ll.exam.sen_books.app.post.repository;

import com.ll.exam.sen_books.app.member.entity.Member;
import com.ll.exam.sen_books.app.post.entity.Post;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> searchQsl(Member author, String kwType, String kw);
}
