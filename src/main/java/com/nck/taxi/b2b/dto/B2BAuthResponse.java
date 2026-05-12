package com.nck.taxi.b2b.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class B2BAuthResponse {
    private String token;
    private B2BCompanyDTO company;
}
