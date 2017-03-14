package com.cs246.plantapp;

import org.junit.Test;

import static com.cs246.plantapp.LoginActivity.isEmailValid;
import static com.cs246.plantapp.LoginActivity.isPasswordValid;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by austingolding on 2/22/17.
 */
public class LoginActivityTest {
    /**
     * Is email valid returns true.
     */
    @Test
    public void isEmailValid_returnsTrue() {
        assertThat(isEmailValid("name@email.com"), is(true));
    }

    /**
     * Is email valid returns false.
     */
    @Test
    public void isEmailValid_returnsFalse() {
        assertThat(isEmailValid("nameemail.com"), is(false));
    }

    /**
     * Is passwordl valid returns true.
     */
    @Test
    public void isPasswordlValid_returnsTrue() {
        assertThat(isPasswordValid("12345"), is(true));
    }

    /**
     * Is passwordl valid returns false.
     */
    @Test
    public void isPasswordlValid_returnsFalse() {
        assertThat(isPasswordValid("1234"), is(false));
    }
}