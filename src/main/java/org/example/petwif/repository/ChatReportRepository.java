package org.example.petwif.repository;

import org.example.petwif.domain.entity.ChatReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatReportRepository extends JpaRepository<ChatReport, Long> {
}
