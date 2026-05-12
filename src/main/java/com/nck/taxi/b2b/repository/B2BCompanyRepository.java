package com.nck.taxi.b2b.repository;
 

 

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nck.taxi.b2b.model.B2BCompany;

@Repository
public interface B2BCompanyRepository extends JpaRepository<B2BCompany, Long> {
    Optional<B2BCompany> findByEmail(String email);
    boolean existsByEmail(String email);
}