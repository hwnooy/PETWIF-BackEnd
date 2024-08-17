package org.example.petwif.service.CommentService;

import lombok.RequiredArgsConstructor;
import org.example.petwif.apiPayload.code.status.ErrorStatus;
import org.example.petwif.apiPayload.exception.GeneralException;
import org.example.petwif.domain.entity.Comment;
import org.example.petwif.domain.entity.CommentReport;
import org.example.petwif.domain.entity.Member;
import org.example.petwif.repository.CommentReportRepository;
import org.example.petwif.repository.CommentRepository;
import org.example.petwif.repository.MemberRepository;
import org.example.petwif.web.dto.CommentDto.CommentReportRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentReportServiceImpl implements CommentReportService {

    private final CommentReportRepository commentReportRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Long ReportComment(CommentReportRequestDto commentReportRequestDto,Long commentId,Long memberId){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new GeneralException(ErrorStatus.COMMENT_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        CommentReport commentReport = CommentReport.builder()
                .member(member)
                .comment(comment)
                .content(commentReportRequestDto.getContent())
                .build();
        commentReportRepository.save(commentReport);

        comment.incrementReportCount();
        commentRepository.save(comment);

        return commentReport.getId();
    }
}

