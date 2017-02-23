package com.cs246.plantapp;

import org.junit.Test;

import static com.cs246.plantapp.LoginActivity.*;
import static com.cs246.plantapp.LoginActivity.isPasswordValid;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by austingolding on 2/22/17.
 */
public class LoginActivityTest {
    @Test
    public void isEmailValid_returnsTrue() {
        assertThat(isEmailValid("name@email.com"), is(true));
    }

    @Test
    public void isEmailValid_returnsFalse() {
        assertThat(isEmailValid("nameemail.com"), is(false));
    }

    @Test
    public void isPasswordlValid_returnsTrue() {
        assertThat(isPasswordValid("12345"), is(true));
    }

    @Test
    public void isPasswordlValid_returnsFalse() {
        assertThat(isPasswordValid("1234"), is(false));
    }
}