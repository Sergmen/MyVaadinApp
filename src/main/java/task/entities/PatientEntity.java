package task.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity(name = "Patient")
@Table(name = "patient")
@NamedQuery(name = "findAllPatients", query="select p from Patient p")
//@NamedQuery(name = "findByName", query="select p from Patient p where concat(p.name, ' ', p.surname, ' ', p.patronymic) like concat('%', :name, '%')")
public class PatientEntity implements Serializable, Cloneable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "patronymic", nullable = false)
    private String patronymic;

    @Column(name = "phone", nullable = true)
    private String phone;

    @OneToMany(mappedBy = "patient")
    private Set<RecipeEntity> recipes = new HashSet<>();

    @Override
    public String toString() {
        return String.format("%s %s %s(%d)",surname,name,patronymic,id);
    }
}

















