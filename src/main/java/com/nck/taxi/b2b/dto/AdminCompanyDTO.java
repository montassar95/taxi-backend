package com.nck.taxi.b2b.dto;

 

import com.nck.taxi.b2b.model.B2BCompany.CompanyStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class AdminCompanyDTO {
    private Long          id;
    private String        companyName;
    private String        email;
    private String        phone;
    private String        address;
    private CompanyStatus status;
    private LocalDateTime createdAt;
    private long          coursesCount;
    private long          groupsCount;
}