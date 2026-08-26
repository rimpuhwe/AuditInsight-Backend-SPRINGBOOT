package com.diana.auditinsightbackendspringboot.Enum;

/** Countries supported at organisation registration, restricted to where PawaPay MoMo operates today. */
public enum CountryCode {

    RW("Rwanda"),
    UG("Uganda"),
    KE("Kenya"),
    TZ("Tanzania");

    private final String displayName;

    CountryCode(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
