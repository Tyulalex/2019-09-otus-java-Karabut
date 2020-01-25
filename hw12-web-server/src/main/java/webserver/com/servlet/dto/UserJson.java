package webserver.com.servlet.dto;


import com.google.gson.annotations.Expose;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@Data
public class UserJson {

    @Expose
    private String name;
    @Expose
    private int age;
    @Expose
    private AddressJson address;
    @Expose
    private List<PhoneJson> phones;
    @Expose
    private String login;
    @Expose(serialize = false)
    private String password;

    public void addPhone(PhoneJson phoneJson) {
        if (this.phones == null) {
            this.phones = new ArrayList<>();
        }
        this.phones.add(phoneJson);
    }

    public void removePhone(PhoneJson phoneJson) {
        this.phones.remove(phoneJson);
    }

    @Getter
    @AllArgsConstructor
    public static class AddressJson {
        @Expose
        private String street;
    }

    @Getter
    public static class PhoneJson {
        @Expose
        private String number;

        public PhoneJson(String number) {
            this.number = number;
        }
    }
}
