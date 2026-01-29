package com.op.heroManager.user.projections;

import java.util.UUID;

public interface UserSummary {
    UUID getId();
    String getName();
    String getEmail();
    String getRole();
    
    // Spring automatically joins and maps the Address entity to this interface
    AddressSummary getAddress();
}
