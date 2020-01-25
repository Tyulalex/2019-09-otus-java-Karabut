package webserver.com.db.api.model;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "phones")
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idphone")
    @Column(name = "id")
    private long id;

    @Column(name = "number")
    private String number;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    public Phone(String number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Phone phone = (Phone) object;
        return id == phone.id &&
                number.equals(phone.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number);
    }
}
