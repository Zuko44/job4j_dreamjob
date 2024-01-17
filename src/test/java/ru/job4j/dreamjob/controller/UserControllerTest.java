package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.service.UserService;

import javax.servlet.http.HttpSession;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {
    private UserService userService;
    private UserController userController;

    @BeforeEach
    public void initServices() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    public void whenUserControllerGetRegistrationPageThanSuccessfully() {
        var model = new ConcurrentModel();
        var view = userController.getRegistrationPage(model);

        assertThat(view).isEqualTo("users/register");
    }

    @Test
    public void whenUserControllerRegisterPageThanSuccessfully() {
        User user = new User(1, "petiakoko@gmail.com", "Petia", "123;jgegjljnhb");
        Optional<User> result = Optional.of(user);
        when(userService.save(user)).thenReturn(result);

        var model = new ConcurrentModel();
        var view = userController.register(model, user);

        assertThat(view).isEqualTo("redirect:/vacancies");
    }

    @Test
    public void whenUserControllerRegisterPageThanFail() {
        User user = null;
        Optional<User> result = Optional.empty();
        String expected = "Пользователь с такой почтой уже существует";
        when(userService.save(user)).thenReturn(result);

        var model = new ConcurrentModel();
        var view = userController.register(model, user);
        var actualUser = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualUser).isEqualTo(expected);
    }

    @Test
    public void whenUserControllerLoginThanSuccessfully() {
        var model = new ConcurrentModel();
        var view = userController.getLoginPage(model);

        assertThat(view).isEqualTo("users/login");
    }

    @Test
    public void whenUserControllerLoginUserThanSuccessfully() {
        User user = new User(1, "petiakoko@gmail.com", "Petia", "123;jgegjljnhb");
        var httpServletRequest = new MockHttpServletRequest();
        var userOptional = Optional.of(user);
        when(userService.findByEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(userOptional);

        var model = new ConcurrentModel();
        var view = userController.loginUser(user, model, httpServletRequest);

        assertThat(view).isEqualTo("redirect:/vacancies");
    }

    @Test
    public void whenUserControllerLogoutThanSuccessfully() {
        HttpSession session = new MockHttpSession();
        var view = userController.logout(session);

        assertThat(view).isEqualTo("redirect:/users/login");
    }
}