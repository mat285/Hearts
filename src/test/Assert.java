package test;

import test.AssertionException;

public class Assert {

    public static void Equal(Object o1, Object o2, String message) {
        if (!o1.equals(o2)) throw new AssertionException("Expected equality but objects were not equal.\n" + message);
    }

    public static void Equal(Object o1, Object o2) {
        Equal(o1, o2, o1.toString() + "   " + o2.toString());
    }

    public static void NotEqual(Object o1, Object o2, String message) {
        if (o1.equals(o2)) throw new AssertionException("Expected objects to not be equal but objects were.\n" + message);
    }

    public static void NotEqual(Object o1, Object o2) {
        NotEqual(o1, o2, o1.toString() + "   " + o2.toString());
    }

    public static void True(boolean b, String message) {
        if (!b) throw new AssertionException("Expected true but found false\n" + message);
    }

    public static void True(boolean b) {
        True(b,"");
    }

    public static void False(boolean b, String message) {
        if (b) throw new AssertionException("Expected false but found true\n" + message);
    }

    public static void False(boolean b) {
        False(b,"");
    }
}
