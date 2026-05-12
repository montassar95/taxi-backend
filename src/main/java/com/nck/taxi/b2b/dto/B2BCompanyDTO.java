package com.nck.taxi.b2b.dto;
 

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class B2BCompanyDTO {
    private Long   id;
    private String companyName;
    private String email;
    private String phone;
    private String address;
}