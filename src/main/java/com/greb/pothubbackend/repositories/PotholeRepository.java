package com.greb.pothubbackend.repositories;

import com.greb.pothubbackend.models.Pothole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PotholeRepository extends JpaRepository<Pothole, String> {
}
