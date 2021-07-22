package com.lms.hat.thinking.model.course;

import com.lms.hat.thinking.model.comment.Comment;
import com.lms.hat.thinking.model.material.Material;
import com.lms.hat.thinking.model.skill.Skill;
import com.lms.hat.thinking.model.user.UserEntity;
import com.lms.hat.thinking.model.linked.SkillToCourse;
import com.lms.hat.thinking.model.task.Task;
import lombok.Data;

import javax.persistence.*;
import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "courses")
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String creator;

    @ManyToMany(mappedBy = "courses")
    private Set<UserEntity> users = new HashSet<>();

    @Column(nullable = false)
    private Instant beginDate;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SkillToCourse> availableSkills = new HashSet<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    @Column(nullable = false)
    private Instant endDate;

    @Column
    private String description;

    @Column(nullable = false)
    private Integer passingScore;

    @ManyToMany(mappedBy = "courses", fetch = FetchType.LAZY)
    private Set<Material> materials = new HashSet<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

    public void addMaterial(Material material) {
        materials.add(material);
        material.getCourses().add(this);
    }

    public void addTask(Task task) {
        tasks.add(task);
        task.setCourse(this);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setCourse(this);
    }

    public void addSkill(Skill skill, Integer level) {
        SkillToCourse stc = new SkillToCourse();
        stc.setLevel(level);
        stc.setSkill(skill);
        stc.setCourse(this);
        availableSkills.add(stc);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(id, course.id) &&
                Objects.equals(name, course.name) &&
                Objects.equals(creator, course.creator) &&
                Objects.equals(beginDate, course.beginDate) &&
                Objects.equals(endDate, course.endDate) &&
                Objects.equals(description, course.description) &&
                Objects.equals(passingScore, course.passingScore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, creator, beginDate, endDate, description, passingScore);
    }
}
