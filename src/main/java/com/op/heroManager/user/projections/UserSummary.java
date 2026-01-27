package com.op.heroManager.user.projections;

import java.util.UUID;

public interface UserSummary {
    UUID getId();
    String getName();
    String getEmail();
    
    // Spring automatically joins and maps the Address entity to this interface
    AddressSummary getAddress();
}
