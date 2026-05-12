package com.nck.taxi.b2b.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "b2b_companies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class B2BCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @JsonIgnore                                    // ← ne jamais exposer le mot de passe
    private String password;

    private String phone;
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CompanyStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) this.status = CompanyStatus.ACTIVE;
    }

    public enum CompanyStatus {
        ACTIVE, SUSPENDED, PENDING
    }
}