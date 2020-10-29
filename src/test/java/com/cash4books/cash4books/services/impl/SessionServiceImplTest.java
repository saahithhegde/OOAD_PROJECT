package com.cash4books.cash4books.services.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@WithMockUser
public class SessionServiceImplTest {
    @InjectMocks
    SessionServiceImpl sessionServiceImpl;

    @Test
    public void createSessionTest(){

    }
}
