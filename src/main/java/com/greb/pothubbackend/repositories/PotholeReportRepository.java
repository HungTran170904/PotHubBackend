package com.greb.pothubbackend.repositories;

import com.greb.pothubbackend.models.PotholeReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PotholeReportRepository extends JpaRepository<PotholeReport, String> {
}
