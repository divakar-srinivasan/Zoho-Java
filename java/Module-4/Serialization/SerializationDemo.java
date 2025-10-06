package Serialization;

import java.io.*;

class Student implements Serializable {
    private static final long serialVersionUID = 1L; 
    final String name;
    int age;

    Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

}

public class SerializationDemo {
    public static void main(String[] args) {
        Student s1 = new Student("Divakar", 21);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Serialization/student.ser"))) {
            oos.writeObject(s1);
            System.out.println("✅ Object Serialized Successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Serialization/student.ser"))) {
            Student s2 = (Student) ois.readObject();
            System.out.println("✅ Object Deserialized Successfully!");
            System.out.println("Name: " + s2.name + ", Age: " + s2.age);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

