package hw11.mycache.hibernateimpl.service;

import hw11.mycache.api.model.Address;
import hw11.mycache.api.model.Phone;
import hw11.mycache.api.model.User;
import hw11.mycache.api.service.DbServiceUser;
import hw11.mycache.cache.HwCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class CacheDbServiceUserImplTest {

    @InjectMocks
    CacheDbServiceUserImpl cacheDbServiceUser;

    @Mock
    DbServiceUser dbServiceUser;

    @Mock
    HwCache<String, User> cache;

    @BeforeEach
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testDbServiceCacheSaveUser() throws Exception {
        User user = getDefaultUser("John");
        this.cacheDbServiceUser.saveUser(user);
        verify(dbServiceUser, times(1)).saveUser(user);
        verify(cache, times(1))
                .put(anyString(), any(User.class));
    }

    @Test
    void testDbServiceGetFromCache() throws Exception {
        User user = getDefaultUser("John");
        long id = 1L;
        String idStr = String.valueOf(id);
        when(cache.get(idStr)).thenReturn(user);
        Optional<User> loadedFromCache = this.cacheDbServiceUser.getUser(id);
        assertThat(loadedFromCache).isPresent().get().isEqualTo(user);
        verify(cache, times(1)).get(idStr);
        verify(dbServiceUser, never()).getUser(anyLong());
    }

    @Test
    void testDbServiceGetFromDBWhenNoInCache() throws Exception {
        User user = getDefaultUser("John");
        long id = 1L;
        String idStr = String.valueOf(id);
        when(cache.get(idStr)).thenReturn(null);
        when(dbServiceUser.getUser(id)).thenReturn(Optional.of(user));
        Optional<User> loadedFromDb = this.cacheDbServiceUser.getUser(id);
        assertThat(loadedFromDb).isPresent().get().isEqualTo(user);
        verify(cache, times(1)).get(idStr);
        verify(dbServiceUser, times(1)).getUser(id);
    }

    private User getDefaultUser(String name) {
        Phone phone = new Phone("+7(123) 123-12-12");
        Address address = new Address();
        User user = new User(name, 12, address);
        user.addPhone(phone);
        address.setId(0);
        address.setStreet("Vavilova street 12-34");

        return user;
    }
}
