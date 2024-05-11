package dev.aj.springreact.domain.model;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public final class Student {
    private final String name;
    private final int age;
    private final String grade;

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Student) obj;
        return Objects.equals(this.name, that.name) &&
                this.age == that.age &&
                Objects.equals(this.grade, that.grade);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, grade);
    }

    @Override
    public String toString() {
        return "Student[" +
                "studentName=" + name + ", " +
                "age=" + age + ", " +
                "grade=" + grade + ']';
    }

}
