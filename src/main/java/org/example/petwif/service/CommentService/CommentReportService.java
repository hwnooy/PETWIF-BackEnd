package org.example.petwif.service.CommentService;

import org.example.petwif.web.dto.CommentDto.CommentReportRequestDto;

public interface CommentReportService {

    public Long ReportComment(CommentReportRequestDto commentReportRequestDto, Long commentId, Long memberId);
}

