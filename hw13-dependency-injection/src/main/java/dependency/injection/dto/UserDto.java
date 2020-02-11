package dependency.injection.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    @NonNull
    @JsonProperty("name")
    private String name;
    @JsonProperty("age")
    private int age;
    private AddressDto address;
    @JsonProperty("phones")
    private List<PhoneJson> phones;
    @NonNull
    private String login;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    UserDto(@NonNull String name, int age, AddressDto address, List<PhoneJson> phones, @NonNull String login, String password) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.phones = phones;
        this.login = login;
        this.password = password;
    }

    public static UserDtoBuilder builder() {
        return new UserDtoBuilder();
    }

    @JsonSetter("phones")
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
    @Setter
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PhoneJson {

        @JsonProperty("number")
        private String number;

        public PhoneJson(String number) {
            this.number = number;
        }
    }

    public static class UserDtoBuilder {
        private @NonNull String name;
        private int age;
        private AddressDto address;
        private List<PhoneJson> phones;
        private @NonNull String login;
        private @NonNull String password;

        UserDtoBuilder() {
        }

        public UserDtoBuilder name(@NonNull String name) {
            this.name = name;
            return this;
        }

        public UserDtoBuilder age(int age) {
            this.age = age;
            return this;
        }

        public UserDtoBuilder address(AddressDto address) {
            this.address = address;
            return this;
        }

        public UserDtoBuilder phones(List<PhoneJson> phones) {
            this.phones = phones;
            return this;
        }

        public UserDtoBuilder login(@NonNull String login) {
            this.login = login;
            return this;
        }

        public UserDtoBuilder password(@NonNull String password) {
            this.password = password;
            return this;
        }

        public UserDto build() {
            return new UserDto(name, age, address, phones, login, password);
        }

        public String toString() {
            return "UserDto.UserDtoBuilder(name=" + this.name + ", age=" + this.age + ", address=" + this.address + ", phones=" + this.phones + ", login=" + this.login + ", password=" + this.password + ")";
        }
    }
}

