package task.entities;

import lombok.*;
import lombok.Builder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity(name = "Doctor")
@Table(name = "doctor")
@NamedQuery(name = "findAllDoctors", query="select d from Doctor d")
public class DoctorEntity implements Serializable, Cloneable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "patronymic", nullable = false)
    private String patronymic;

    @Column(name = "specialization", nullable = true)
    private String specialization;

    @OneToMany(mappedBy = "doctor")
    private Set<RecipeEntity> recipes = new HashSet<>();

    @Override
    public String toString() {
        return String.format("%s %s %s(%d)",surname,name,patronymic,id);
    }

}


