package com.op.heroManager.user.enums;

public enum Role {
    ADMIN("Admin"),// the enum can have multiple parameters or none ADMIN(1,"admin","lots of access") , it's equivalent to new ADMIN(parameters)
    USER("User"),
    MODERATOR("Moderator"),
    GUEST("Guest");
    
    // final is like const in javascript
    private final String displayName;
    
    Role(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    // this is done to display only the string when you call the displayName
    @Override
    public String toString() {
        return displayName;
    }
}