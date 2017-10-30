package com.example;

import rx.Observable;

/**
 * Created by shisong on 2016/11/8.
 */

public class RxTest {

    public static void main(String[] args) {
        Observable<Person> values = Observable.just(new Person("Will", 25), new Person("Will", 40), new Person("Will", 35));
        values.toMultimap(person -> person.name, person -> person.age).subscribe(new PrintSubscriber("toMap"));
    }

    static class Person {
        public final String name;
        public final Integer age;
        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }
}
