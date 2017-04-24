package com.example.tesla.yandextranslator.Utils;

public class ArrayUtils {
    public static long[] toPrimitives(Long... objects) {
        long[] primitives = new long[objects.length];
        for (int i = 0; i < objects.length; i++)
            primitives[i] = objects[i];

        return primitives;
    }
    public static Long[] toObject(long... primitives) {
        Long[] objects = new Long[primitives.length];
        for (int i = 0; i < primitives.length; i++)
            objects[i] = primitives[i];

        return objects;
    }
}
