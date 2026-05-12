package com.nck.taxi.b2b.repository; 
 

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nck.taxi.b2b.model.B2BCompany;
import com.nck.taxi.b2b.model.B2BGroup;

@Repository
public interface B2BGroupRepository extends JpaRepository<B2BGroup, Long> {
    List<B2BGroup> findByCompanyOrderByCreatedAtDesc(B2BCompany company);
    
    // ── Admin ──
    long countByCompany(B2BCompany company);
}