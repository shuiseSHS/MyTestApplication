package test;

/**
 * Created by shisong on 2018/1/25.
 */

public class ClassNameTest {

    public static void main(String[] s) {
        Fruit fruit = new Fruit();
        Apple apple = new Apple();

        System.out.println(instanceOfFruit(fruit));
        System.out.println(instanceOfFruit(apple));
    }

    private static boolean instanceOfFruit(Object o) {
        Class oc = o.getClass();
        while (oc != Object.class) {
            if (oc.getName().equals("test.Fruit")) {
                return true;
            } else {
                oc = oc.getSuperclass();
            }
        }
        return false;
    }
}

class Fruit {

}

class Apple extends Fruit {

}
