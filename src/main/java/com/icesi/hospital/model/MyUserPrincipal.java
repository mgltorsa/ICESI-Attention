package com.icesi.hospital.model;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


public class MyUserPrincipal implements UserDetails {
   /**
	 * 
	 */
	private static final long serialVersionUID = 4337165151698648402L;
	private User user;

   public MyUserPrincipal(User user) {
       this.user = user;
   }
   //...

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
       final List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("User"));
       return authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return new String(  user.getPassword()  ) ;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getLogin();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
}