package engine.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CurrentUser {

    public UserDetailsImpl get() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        log.info("get(+) authentication={}",authentication);
        if(authentication==null
           ||authentication.getPrincipal().toString().equals("anonymousUser")
        ){
            return null;
        }else {
            return (UserDetailsImpl) authentication.getPrincipal();
        }
    }

    public Boolean Authenticated(){
        return get() != null;
    }
}
