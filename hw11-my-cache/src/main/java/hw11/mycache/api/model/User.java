package hw11.mycache.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idusers")
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private int age;

    @OneToOne(targetEntity = Address.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    Address address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<Phone> phones;

    public User(String name, int age, Address address) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.phones = new ArrayList<>();
    }

    public void addPhone(Phone phone) {
        phones.add(phone);
        phone.setUser(this);
    }

    public void removeComment(Phone phone) {
        phones.remove(phone);
        phone.setUser(null);
    }
}
