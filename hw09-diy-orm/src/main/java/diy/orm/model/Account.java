package diy.orm.model;

import diy.orm.annotations.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Setter
public class Account {

    @Id
    private long no;
    private String type;
    private BigDecimal rest;

    public Account(String type, BigDecimal rest) {
        this.type = type;
        this.rest = rest;
    }

    public long getNo() {
        return no;
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Account account = (Account) object;
        return no == account.no &&
                type.equals(account.type) &&
                rest.equals(account.rest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(no, type, rest);
    }

    public BigDecimal getRest() {
        return rest;
    }

    @Override
    public String toString() {
        return "Account{" +
                "no=" + no +
                ", type='" + type + '\'' +
                ", rest=" + rest +
                '}';
    }
/*
    • no bigint(20) NOT NULL auto_increment
    • type varchar(255)
    • rest number
     */
}
