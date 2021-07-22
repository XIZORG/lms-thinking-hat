package com.lms.hat.thinking.model.material;

import com.lms.hat.thinking.model.user.UserEntity;
import com.lms.hat.thinking.model.course.Course;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@Table(name = "materials")
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private String creator;

    @Column
    private String link;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "material_to_course",
            joinColumns = @JoinColumn(name = "material_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id")
    )
    private final Set<Course> courses = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "material_to_user",
            joinColumns = @JoinColumn(name = "material_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
    )
    private final Set<UserEntity> userEntities = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Material material = (Material) o;
        return Objects.equals(id, material.id) &&
                Objects.equals(name, material.name) &&
                Objects.equals(creator, material.creator) &&
                Objects.equals(link, material.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, creator, link);
    }
}
