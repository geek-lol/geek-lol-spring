package com.nat.geeklolspring.report.repository;

import com.nat.geeklolspring.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findAllByUserId(String userId);
}
