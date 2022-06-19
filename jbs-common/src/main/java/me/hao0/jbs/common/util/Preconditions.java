package me.hao0.jbs.common.util;

import java.util.Collection;

public final class Preconditions {

    private static final String CAN_NOT_NULL_EMPTY = " can't be null or empty";

    private static final String MUST_GT_0 = " must > 0";

    private static final String MUST_LT_0 = " must < 0";

    private Preconditions() {
    }


    public static void checkArgument(boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException();
        }
    }


    public static void checkArgument(boolean expression, Object errorMessage) {
        if (!expression) {
            throw new IllegalArgumentException(String.valueOf(errorMessage));
        }
    }


    public static void checkArgument(
            boolean expression,
            String errorMessageTemplate,
            Object... errorMessageArgs) {
        if (!expression) {
            throw new IllegalArgumentException(format(errorMessageTemplate, errorMessageArgs));
        }
    }


    public static void checkState(boolean expression) {
        if (!expression) {
            throw new IllegalStateException();
        }
    }


    public static void checkState(boolean expression, Object errorMessage) {
        if (!expression) {
            throw new IllegalStateException(String.valueOf(errorMessage));
        }
    }


    public static void checkState(
            boolean expression,
            String errorMessageTemplate,
            Object... errorMessageArgs) {
        if (!expression) {
            throw new IllegalStateException(format(errorMessageTemplate, errorMessageArgs));
        }
    }


    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }


    public static <T> T checkNotNull(T reference, Object errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
        return reference;
    }


    public static <T> T checkNotNull(
            T reference, String errorMessageTemplate, Object... errorMessageArgs) {
        if (reference == null) {

            throw new NullPointerException(format(errorMessageTemplate, errorMessageArgs));
        }
        return reference;
    }




    public static int checkElementIndex(int index, int size) {
        return checkElementIndex(index, size, "index");
    }


    public static int checkElementIndex(int index, int size, String desc) {

        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(badElementIndex(index, size, desc));
        }
        return index;
    }

    private static String badElementIndex(int index, int size, String desc) {
        if (index < 0) {
            return format("%s (%s) must not be negative", desc, index);
        } else if (size < 0) {
            throw new IllegalArgumentException("negative size: " + size);
        } else {
            return format("%s (%s) must be less than size (%s)", desc, index, size);
        }
    }


    public static int checkPositionIndex(int index, int size) {
        return checkPositionIndex(index, size, "index");
    }


    public static int checkPositionIndex(int index, int size, String desc) {

        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(badPositionIndex(index, size, desc));
        }
        return index;
    }

    private static String badPositionIndex(int index, int size, String desc) {
        if (index < 0) {
            return format("%s (%s) must not be negative", desc, index);
        } else if (size < 0) {
            throw new IllegalArgumentException("negative size: " + size);
        } else {
            return format("%s (%s) must not be greater than size (%s)", desc, index, size);
        }
    }


    public static void checkPositionIndexes(int start, int end, int size) {

        if (start < 0 || end < start || end > size) {
            throw new IndexOutOfBoundsException(badPositionIndexes(start, end, size));
        }
    }

    private static String badPositionIndexes(int start, int end, int size) {
        if (start < 0 || start > size) {
            return badPositionIndex(start, size, "start index");
        }
        if (end < 0 || end > size) {
            return badPositionIndex(end, size, "end index");
        }

        return format("end index (%s) must not be less than start index (%s)", end, start);
    }



    static String format(String template, Object... args) {
        template = String.valueOf(template);


        StringBuilder builder = new StringBuilder(template.length() + 16 * args.length);
        int templateStart = 0;
        int i = 0;
        while (i < args.length) {
            int placeholderStart = template.indexOf("%s", templateStart);
            if (placeholderStart == -1) {
                break;
            }
            builder.append(template.substring(templateStart, placeholderStart));
            builder.append(args[i++]);
            templateStart = placeholderStart + 2;
        }
        builder.append(template.substring(templateStart));


        if (i < args.length) {
            builder.append(" [");
            builder.append(args[i++]);
            while (i < args.length) {
                builder.append(", ");
                builder.append(args[i++]);
            }
            builder.append(']');
        }

        return builder.toString();
    }

    public static void checkNotNullAndEmpty(String checking, String field){
        if (checking == null || "".equals(checking)){
            throw new IllegalArgumentException(field + CAN_NOT_NULL_EMPTY);
        }
    }

    public static void checkNotNullAndEmpty(Collection<?> checking, String field){
        if (checking == null || checking.isEmpty()){
            throw new IllegalArgumentException(field + CAN_NOT_NULL_EMPTY);
        }
    }

    public static void checkPositive(Number number, String field){
        if (number == null || number.intValue() < 0){
            throw new IllegalArgumentException(field + MUST_GT_0);
        }
    }

    public static void checkNegative(Number number, String field){
        if (number == null || number.intValue() > 0){
            throw new IllegalArgumentException(field + MUST_LT_0);
        }
    }
}