package me.hao0.jbs.common.util;

import sun.misc.Unsafe;
import java.lang.reflect.Field;


public abstract class Fields {

    private static final Unsafe unsafe = getUnsafe();

    private static Unsafe getUnsafe() {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            return (Unsafe) f.get(null);
        } catch (Exception e) {
            throw new RuntimeException("failed to get unsafe instance, cause");
        }
    }


    public static void put(Object target, String name, Object value){
        try {
            Field field = target.getClass().getField(name);
            field.setAccessible(true);
            long fieldOffset = unsafe.objectFieldOffset(field);
            unsafe.putObject(target, fieldOffset, value);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }


    public static void put(Object target, Field field, Object value) {
        try {
            field.setAccessible(true);
            long fieldOffset = unsafe.objectFieldOffset(field);
            unsafe.putObject(target, fieldOffset, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static <T> T get(Object target, String name) {
        try {
            return get(target, target.getClass().getDeclaredField(name));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }


    @SuppressWarnings("unchecked")
    public static <T> T get(Object target, Field field) {
        try {
            long fieldOffset = unsafe.objectFieldOffset(field);
            return (T)unsafe.getObject(target, fieldOffset);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}