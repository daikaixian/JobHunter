package org.codingwater.service;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by water on 4/18/16.
 */
public class Java8Test {

  @Test
  public void testLambda() {

    System.out.println("=== RunnableTest ===");

    // Anonymous Runnable
    Runnable r1 = new Runnable() {
      public void run() {
        System.out.println("Hello world one!");
      }
    };

    // Lambda Runnable
    Runnable r2 = () -> System.out.println("Hello world two!");

    // Run em!
    r1.run();
    r2.run();
  }

  @Test
  public void testComparatorWithLambda() {
    List<Person> personList = new ArrayList<>();
    Person person1 = new Person("dai");
    Person person2 = new Person("kaixian");
    personList.add(person1);
    personList.add(person2);

    // Sort with Inner Class
    Collections.sort(personList, new Comparator<Person>() {
      public int compare(Person p1, Person p2) {
        return p1.getSurName().compareTo(p2.getSurName());
      }
    });

    System.out.println("=== Sorted Asc SurName ===");
    for (Person p : personList) {
      p.printName();
    }

    // Use Lambda instead

    // Print Asc
    System.out.println("=== Sorted Asc SurName ===");
    Collections.sort(personList, (Person p1, Person p2) -> p1.getSurName().compareTo(p2.getSurName()));

    for (Person p : personList) {
      p.printName();
    }

    // Print Desc
    System.out.println("=== Sorted Desc SurName ===");
    Collections.sort(personList, (p1, p2) -> p2.getSurName().compareTo(p1.getSurName()));
    //Optional Feature

    for (Person p : personList) {
      p.printName();
    }

  }

  class Person {

    public Person(String surName){
      this.setSurName(surName);
    }


    public String getSurName() {
      return surName;
    }

    public void setSurName(String surName) {
      this.surName = surName;
    }

    private String surName;

    public void printName() {
      System.out.println(this.getSurName());
    }

  }

  @Test
  public void testForEach() {
    List<Integer> list = Arrays.asList(1, 2, 3, 4);
//    list.forEach(l -> System.out.println(l.intValue()));

    Predicate<Integer> predicate = p -> p.intValue() > 2;

    list.stream().filter(predicate).forEach(Integer :: intValue);
    list.stream().filter(predicate).forEach(l -> System.out.println(l.intValue()));
  }

}
