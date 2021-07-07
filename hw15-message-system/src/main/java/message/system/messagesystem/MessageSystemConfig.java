package message.system.messagesystem;

import com.google.gson.Gson;
import message.system.frontendservice.CreateUserDataResponseHandler;
import message.system.frontendservice.FrontendService;
import message.system.frontendservice.FrontendServiceImpl;
import message.system.frontendservice.GetUsersDataResponseHandler;
import message.system.repository.handlers.CreateUserRequestHandler;
import message.system.repository.handlers.GetUsersRequestHandler;
import message.system.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class MessageSystemConfig {

    private static final String USER_SERVICE_MS_CLIENT_NAME = "usersService";
    private static final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";

    @Bean
    public MsClient userServiceMsClient(MessageSystem messageSystem, UserService userService) {
        MsClient userServiceMsClient = new MsClientImpl(USER_SERVICE_MS_CLIENT_NAME, messageSystem);
        userServiceMsClient.addHandler(MessageType.GET_USERS_DATA, new GetUsersRequestHandler(userService));
        userServiceMsClient.addHandler(MessageType.CREATE_USERS_DATA, new CreateUserRequestHandler(userService));
        return userServiceMsClient;
    }

    @Bean
    public FrontendService frontendService(@Lazy @Qualifier("frontendMsClient") MsClient frontendMsClient) {
        return new FrontendServiceImpl(frontendMsClient, USER_SERVICE_MS_CLIENT_NAME);
    }

    @Bean
    public MsClient frontendMsClient(MessageSystem messageSystem, FrontendService frontendService, Gson gson) {
        MsClient frontendMsClient = new MsClientImpl(FRONTEND_SERVICE_CLIENT_NAME, messageSystem);
        frontendMsClient.addHandler(MessageType.GET_USERS_DATA, new GetUsersDataResponseHandler(frontendService, gson));
        frontendMsClient.addHandler(MessageType.CREATE_USERS_DATA, new CreateUserDataResponseHandler(frontendService, gson));
        return frontendMsClient;
    }
}
