package dependency.injection.converter;

import dependency.injection.dto.AddressDto;
import dependency.injection.dto.UserDto;
import dependency.injection.repository.db.api.model.Address;
import dependency.injection.repository.db.api.model.Phone;
import dependency.injection.repository.db.api.model.RoleMapping;
import dependency.injection.repository.db.api.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public User fromUserJson(UserDto userDto) {
        Address address = Address.builder().street(userDto.getAddress().getStreet()).build();
        User user = User.builder()
                .address(address)
                .age(userDto.getAge())
                .name(userDto.getName())
                .role(RoleMapping.WEBUSER)
                .login(userDto.getLogin())
                .password(userDto.getPassword())
                .build();
        for (UserDto.PhoneJson phoneJson : userDto.getPhones()) {
            Phone phone = Phone.builder().number(phoneJson.getNumber()).build();
            user.addPhone(phone);
        }
        return user;
    }

    public UserDto toUserJson(User user) {
        UserDto userJson = UserDto
                .builder()
                .name(user.getName())
                .age(user.getAge())
                .login(user.getLogin())
                .build();
        AddressDto addressJson = null;
        if (user.getAddress() != null) {
            addressJson = new AddressDto(user.getAddress().getStreet());
        }
        userJson.setAddress(addressJson);
        for (Phone phone : user.getPhones()) {
            UserDto.PhoneJson phoneJson = new UserDto.PhoneJson(phone.getNumber());
            userJson.addPhone(phoneJson);
        }
        return userJson;
    }
}
