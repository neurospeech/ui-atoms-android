package com.neurospeech.uiatomsdemo;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class LoginViewModelTest {
    @Test
    public void loginCommandTest() throws Exception {
        LoginViewModel viewModel = new LoginViewModel();

        assertNull(viewModel.error.get());

        viewModel.loginCommand.execute(null);

        assertTrue(viewModel.error.get().equals("Username is required"));

        viewModel.userName.set("test");
        viewModel.loginCommand.execute(null);

        assertTrue(viewModel.error.get().equals("Password is required"));

        viewModel.password.set("password");
        viewModel.loginCommand.execute(null);

        assertNull(viewModel.error.get());
    }
}