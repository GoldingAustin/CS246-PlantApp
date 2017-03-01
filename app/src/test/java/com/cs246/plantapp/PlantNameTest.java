package com.cs246.plantapp;

import org.junit.Test;

import static com.cs246.plantapp.PlantsObject.isNameValid;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by simpl on 2/23/2017.
 */

public class PlantNameTest {

    @Test
    public void isNameValid_returnsTrue() {

        assertThat(isNameValid("Albacarious"), is(true));

    }

    @Test
    public void isNameValid_returnsFalse() {

        assertThat(isNameValid("1 2 3 @"), is(false));

    }

}
