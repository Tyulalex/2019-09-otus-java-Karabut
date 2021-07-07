package message.system.repository.db.api.model;

import lombok.*;
import message.system.utils.Encryption;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Table(name = "Users", uniqueConstraints = @UniqueConstraint(columnNames = "login"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idusers")
    @Column(name = "id")
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "age")
    private int age;

    @OneToOne(targetEntity = Address.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Phone> phones;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private RoleMapping role;

    @NaturalId
    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    public User(String name, int age, Address address, RoleMapping role, String password) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.role = role;
        this.phones = new ArrayList<>();
        this.password = Encryption.encrypt(password);
    }

    public void addPhone(Phone phone) {
        if (this.phones == null) {
            this.phones = new ArrayList<>();
        }
        phones.add(phone);
        phone.setUser(this);
    }

    public void removeComment(Phone phone) {
        phones.remove(phone);
        phone.setUser(null);
    }

    public static class UserBuilder {

        private String password;

        public UserBuilder password(String password) {
            this.password = Encryption.encrypt(password);
            return this;
        }
    }
}
