package message.system.converter;

import message.system.dto.AddressDto;
import message.system.dto.UserDto;
import message.system.repository.db.api.model.Address;
import message.system.repository.db.api.model.Phone;
import message.system.repository.db.api.model.RoleMapping;
import message.system.repository.db.api.model.User;
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
