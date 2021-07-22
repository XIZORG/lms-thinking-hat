package com.lms.hat.thinking.model.user;

import com.lms.hat.thinking.model.skill.Skill;
import com.lms.hat.thinking.model.course.Course;
import com.lms.hat.thinking.model.course.CourseResult;
import com.lms.hat.thinking.model.linked.SkillToUser;
import com.lms.hat.thinking.model.material.Material;
import com.lms.hat.thinking.model.task.TaskResult;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(unique = true)
    private String phone;

    @OneToOne(mappedBy = "user")
    private RefreshToken refreshToken;

    @Column
    private String fullName;

    @Column
    private Integer age;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity roleEntity;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_to_courses",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id")
    )
    private Set<Course> courses = new HashSet<>();

    @ManyToMany(mappedBy = "userEntities", fetch = FetchType.LAZY)
    private Set<Material> materials = new HashSet<>();

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SkillToUser> availableSkills = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TaskResult> taskResults = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CourseResult> courseResults = new HashSet<>();

    public void addCourse(Course course) {
        courses.add(course);
        course.getUsers().add(this);
    }

    public void removeCourse(Course course) {
        courses.remove(course);
        course.getUsers().remove(this);
    }

    public void addTaskResult(TaskResult taskResult) {
        taskResults.add(taskResult);
        taskResult.setUser(this);
    }

    public void addCourseResult(CourseResult courseResult) {
        courseResults.add(courseResult);
        courseResult.setUser(this);
    }

    public void addMaterial(Material material) {
        materials.add(material);
    }

    public void addSkill(Skill skill, Integer level) {
        SkillToUser stu = new SkillToUser();
        stu.setUserEntity(this);
        stu.setSkill(skill);
        stu.setLevel(level);
        availableSkills.add(stu);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(login, that.login) &&
                Objects.equals(password, that.password) &&
                Objects.equals(email, that.email) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(fullName, that.fullName) &&
                Objects.equals(age, that.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, email, phone, fullName, age);
    }
}
