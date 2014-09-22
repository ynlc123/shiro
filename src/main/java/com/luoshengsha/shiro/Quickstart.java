package com.luoshengsha.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Quickstart {
	private static final transient Logger log = LoggerFactory.getLogger(Quickstart.class);
	
	public static void main(String[] args) { 
		
		Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		session.setAttribute( "someKey", "aValue" );
		
		System.out.println(session.getAttribute("someKey"));
		
		System.out.println(currentUser.isAuthenticated());
		
		if ( !currentUser.isAuthenticated() ) {
		    //collect user principals and credentials in a gui specific manner 
		    //such as username/password html form, X509 certificate, OpenID, etc.
		    //We'll use the username/password example here since it is the most common.
		    //(do you know what movie this is from? ;)
		    UsernamePasswordToken token = new UsernamePasswordToken("lonestarr", "vespa");
		    //this is all you have to do to support 'remember me' (no config - built in!):
		    token.setRememberMe(true);
		    currentUser.login(token);
		    
		    try {
                currentUser.login(token);
            } catch (UnknownAccountException uae) {
                log.info("There is no user with username of " + token.getPrincipal());
                System.out.println("There is no user with username of " + token.getPrincipal());
            } catch (IncorrectCredentialsException ice) {
                log.info("Password for account " + token.getPrincipal() + " was incorrect!");
                System.out.println("Password for account " + token.getPrincipal() + " was incorrect!");
            } catch (LockedAccountException lae) {
                log.info("The account for username " + token.getPrincipal() + " is locked.  " +
                        "Please contact your administrator to unlock it.");
                System.out.println("The account for username " + token.getPrincipal() + " is locked.  " +
                        "Please contact your administrator to unlock it.");
            }
            // ... catch more exceptions here (maybe custom ones specific to your application?
            catch (AuthenticationException ae) {
                //unexpected condition?  error?
            }
		}
		    
	    log.info("User [" + currentUser.getPrincipal() + "] logged in successfully.");
	    System.out.println("User [" + currentUser.getPrincipal() + "] logged in successfully.");

        //test a role:
        if (currentUser.hasRole("schwartz")) {
            log.info("May the Schwartz be with you!");
            System.out.println("May the Schwartz be with you!");
        } else {
            log.info("Hello, mere mortal.");
            System.out.println("Hello, mere mortal.");
        }
        
        if ( currentUser.isPermitted( "lightsaber:weild" ) ) {
            log.info("You may use a lightsaber ring.  Use it wisely.");
            
            System.out.println("You may use a lightsaber ring.  Use it wisely.");
        } else {
            log.info("Sorry, lightsaber rings are for schwartz masters only.");
            
            System.out.println("Sorry, lightsaber rings are for schwartz masters only.");
        }
		
		//currentUser.logout();
		
		System.out.println(session.getAttribute("someKey"));
	}
	//≤‚ ‘
}
