package org.ssa.ironyard.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.ssa.ironyard.crypto.BCryptSecurePassword;
import org.ssa.ironyard.model.Playlist;

import org.ssa.ironyard.model.User.UserBuilder;

public class UserTest {

    String email;
    Password password;
    final BCryptSecurePassword security = new BCryptSecurePassword();
    Playlist list1;
    Playlist list2;
    Playlist list3;

    @Before
    public void setup() {
	email = "user@email.com";
	password = security.secureHash("test");
	list1 = new Playlist();
	list2 = new Playlist();
	list3 = new Playlist();

    }

    @Test
    public void getIdTest() {
	User testUser = new UserBuilder().email(email).password(password).build();
	assertEquals(null, testUser.getId());

    }

    @Test
    public void getLoadedTest() {
	User testUser = new UserBuilder().email(email).password(password).build();
	assertFalse(testUser.isLoaded());

    }

    @Test
    public void setIdTest() {
	User testUser = new UserBuilder().email(email).password(password).id(3).build();
	assertEquals(new Integer(3), testUser.getId());
    }

    @Test
    public void setLoadedTest() {
	User testUser = new UserBuilder().email(email).password(password).loaded(true).build();
	assertTrue(testUser.isLoaded());

    }

}
