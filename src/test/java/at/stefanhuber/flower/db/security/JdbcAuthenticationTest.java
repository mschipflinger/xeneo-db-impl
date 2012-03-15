/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.stefanhuber.flower.db.security;

import at.stefanhuber.flower.core.security.User;
import at.stefanhuber.flower.core.security.UserServices;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Stefan Huber
 */
public class JdbcAuthenticationTest {
    
    private static Logger logger = Logger.getLogger(JdbcAuthenticationTest.class);
    private AuthenticationManager am;
    private UserServices us;
    
    public JdbcAuthenticationTest() {
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"test-config.xml"});
        am = context.getBean("authenticationManager", AuthenticationManager.class);
        us = context.getBean("userServices", UserServices.class);
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    /*
    @Test
    public void testUserServices() {
        
        us.getCurrentUserURI();
        
        
    }
    */

    
    @Test
    public void testAuthentication() {
        
        Authentication request = new UsernamePasswordAuthenticationToken("Stefan.Huber", "blub");
        Authentication result = am.authenticate(request);
        SecurityContextHolder.getContext().setAuthentication(result);
        
        Object principe = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        JdbcUser user = null;
        if (principe instanceof JdbcUser) {
            user = (JdbcUser) principe;
            logger.info("Successfully logged in User with Username: " + user.getUsername() + ", Fullname: " + user.getFirstName() + " " + user.getLastName());
        }
        
        assertTrue(user != null);
        assertTrue(user.getFirstName().equalsIgnoreCase("stefan") && user.getLastName().equalsIgnoreCase("huber"));   
        
        User u = us.getCurrentUser();
        assertTrue(u.getUserURI().equals(user.getUserURI()));
        assertTrue(us.getCurrentUserURI().equals(user.getUserURI()));
        assertTrue(us.getCurrentUsername().equals(user.getUsername()));
        
        // Logger.info(user.getClass().);
    }
    

}
