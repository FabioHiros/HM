package com.op.heroManager.user.projections;

import java.util.UUID;

public interface PhoneSummary {
    UUID getUserId(); // Needed to link back to the user
    String getNumber();
    String getAreaCode();
}
