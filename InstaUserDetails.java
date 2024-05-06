package ng.optisoft.rosabon.dto;

import ng.optisoft.rosabon.model.Useraccount;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public class InstaUserDetails extends Useraccount implements UserDetails {

    public InstaUserDetails(final Useraccount user) {
        super(user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {


        Set<GrantedAuthority> grantedAuthorities = new HashSet();
        grantedAuthorities.add(new SimpleGrantedAuthority(getRole().getName().name()));

        return grantedAuthorities;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}

