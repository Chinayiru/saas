package com.example.saassystem.tenant;

/**
 * 多租户上下文：线程级保存当前请求的租户 ID
 *
 * @author cxt
 * @date 2026-10-09
 */
public class TenantContext {

    private static final ThreadLocal<Long> HOLDER = new ThreadLocal<>();

    public static void set(Long tenantId) {
        HOLDER.set(tenantId);
    }

    public static Long get() {
        return HOLDER.get();
    }

    public static void clear() {
        HOLDER.remove();
    }
}
