package ProiectMPP.Model;

import java.util.Objects;


public class Child extends Entity {
    private String firstName;
    private String lastName;
    private int age;

    private int noTrials = 0;

    public Child(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public Child(String firstName, String lastName, int age, int noTrials) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.noTrials = noTrials;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Child child = (Child) o;
        return age == child.age && Objects.equals(firstName, child.firstName) && Objects.equals(lastName, child.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, age);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setNoTrials(int noTrials){this.noTrials = noTrials;}

    public int getNoTrials(){return this.noTrials;}
}
