package ru.zhuravlev.Task2.entitys;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Entity
@Table (name = "users")
public class User {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @Column (unique = true)
    private String email;

    private int age;

    private LocalDateTime created_at;

    public User() {
        this.created_at = LocalDateTime.now();
    }

    public User(String name,String email,int age) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.created_at = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public String toString() {
        return "User{" + "id=" + id + ", name=" + name + ", email=" + email + ", age=" + age + ", created_at=" + created_at + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && age == user.age && Objects.equals(name,user.name) && Objects.equals(email,user.email) && created_at.truncatedTo(ChronoUnit.MILLIS).isEqual(user.created_at.truncatedTo(ChronoUnit.MILLIS));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,name,email,age,created_at.truncatedTo(ChronoUnit.MICROS));
    }
}
