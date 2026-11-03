package com.company.salestracker.util;

/**
 * Fixed string constants that are NOT modeled as enums, because they refer to
 * dynamic/admin-configurable data (role names, permission codes) rather than a
 * closed set of values. Lifecycle statuses (lead/deal/user/payment/activity)
 * now live in the com.company.salestracker.enums package instead.
 */
public final class Constants {

    private Constants() {}

    // Permission codes (seeded in database/schema.sql)
    public static final String PERM_USER_CREATE = "USER_CREATE";
    public static final String PERM_USER_READ = "USER_READ";
    public static final String PERM_USER_UPDATE = "USER_UPDATE";
    public static final String PERM_USER_DELETE = "USER_DELETE";
    public static final String PERM_ROLE_MANAGE = "ROLE_MANAGE";
    public static final String PERM_LEAD_CREATE = "LEAD_CREATE";
    public static final String PERM_LEAD_READ = "LEAD_READ";
    public static final String PERM_LEAD_UPDATE = "LEAD_UPDATE";
    public static final String PERM_LEAD_DELETE = "LEAD_DELETE";
    public static final String PERM_DEAL_CREATE = "DEAL_CREATE";
    public static final String PERM_DEAL_READ = "DEAL_READ";
    public static final String PERM_DEAL_UPDATE = "DEAL_UPDATE";
    public static final String PERM_SALE_CREATE = "SALE_CREATE";
    public static final String PERM_SALE_READ = "SALE_READ";
    public static final String PERM_TARGET_MANAGE = "TARGET_MANAGE";
    public static final String PERM_REPORT_READ = "REPORT_READ";
    public static final String PERM_AUDIT_READ = "AUDIT_READ";
}
