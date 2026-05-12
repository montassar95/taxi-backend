package com.nck.taxi.b2b.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nck.taxi.model.Ride;

@Entity
@Table(name = "b2b_courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class B2BCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id", nullable = false)
    @JsonIgnoreProperties({"password", "courses"}) // évite Company → courses → Company
    private B2BCompany company;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id")
    @JsonIgnoreProperties({"courses", "company"})  // évite Group → courses → Group
    private B2BGroup group;

    @Column(nullable = false) private String nom;
    @Column(nullable = false) private String prenom;
    @Column(nullable = false) private String telephone;
    @Column(nullable = false) private String adresseDepart;
    @Column(nullable = false) private String destination;
    @Column(nullable = false) private String date;
    @Column(nullable = false) private String heure;
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ride_id")
    @JsonIgnore                                    // ← coupe B2BCourse → Ride → B2BCourse
    private Ride linkedRide;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) this.status = CourseStatus.EN_ATTENTE;
    }

    public enum CourseStatus {
        EN_ATTENTE, CONFIRMEE, EN_COURS, TERMINEE, ANNULEE
    }
}