package com.nck.taxi.b2b.repository;

import com.nck.taxi.b2b.model.B2BCourse;
import com.nck.taxi.b2b.model.B2BCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface B2BCourseRepository extends JpaRepository<B2BCourse, Long> {

    @Query("SELECT c FROM B2BCourse c JOIN FETCH c.company WHERE c.company = :company ORDER BY c.createdAt DESC")
    List<B2BCourse> findByCompanyOrderByCreatedAtDesc(B2BCompany company);

    @Query("SELECT c FROM B2BCourse c JOIN FETCH c.company WHERE c.group IS NULL AND c.company = :company ORDER BY c.createdAt DESC")
    List<B2BCourse> findByGroupIsNullAndCompanyOrderByCreatedAtDesc(B2BCompany company);

    @Query("SELECT c FROM B2BCourse c JOIN FETCH c.company ORDER BY c.createdAt DESC")
    List<B2BCourse> findAllByOrderByCreatedAtDesc();

    long countByCompany(B2BCompany company);
}