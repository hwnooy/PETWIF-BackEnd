package org.example.petwif.service.CommentService;

import lombok.RequiredArgsConstructor;
import org.example.petwif.apiPayload.code.status.ErrorStatus;
import org.example.petwif.apiPayload.exception.GeneralException;
import org.example.petwif.domain.entity.Album;
import org.example.petwif.domain.entity.Comment;
import org.example.petwif.domain.entity.Member;
import org.example.petwif.repository.AlbumRepository;
import org.example.petwif.repository.CommentRepository;
import org.example.petwif.repository.MemberRepository;
import org.example.petwif.web.dto.CommentDto.CommentRequestDto;
import org.example.petwif.web.dto.CommentDto.CommentResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    //예외처리!!!!!

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final AlbumRepository albumRepository;


    @Override
    public Long writeComment(CommentRequestDto commentRequestDto, Long albumId,String name){
        Member member = memberRepository.findByName(name)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ALBUM_NOT_FOUND));
        Comment toComment=Comment.builder()
                .content(commentRequestDto.getContent())
                .album(album)
                .member(member)
                .build();

        commentRepository.save(toComment);

        return toComment.getId();
    }


    @Override
    public List<CommentResponseDto> commentList(Long id){
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ALBUM_NOT_FOUND));
        List<Comment> commentList=commentRepository.findByAlbum(album);

        return commentList.stream()
                .map(comment-> CommentResponseDto.builder()
                        .comment(comment)
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void updateComment(CommentRequestDto commentRequestDto, Long CommentId){
        Comment comment = commentRepository.findById(CommentId).orElse(null);
        comment.update(commentRequestDto.getContent());
        commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Long commentId){
        commentRepository.deleteById(commentId);
    }
}
