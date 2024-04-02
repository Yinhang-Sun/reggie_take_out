package com.jonathan.reggie.common;


/**
 * Based on the ThreadLocal encapsulation tool class, to save and obtain the current logged-in user ID
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * Set value
     * @param id
     */
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    /**
     * Get value
     * @return
     */
    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
