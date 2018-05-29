package gr.uoa.di.madgik.publicfood.security.service;

import gr.uoa.di.madgik.publicfood.model.UserEntity;

import gr.uoa.di.madgik.publicfood.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class JwtUserDetailsServiceImpl  {

    @Autowired
    private UserRepository userRepository;

    public UserEntity loadUserByID(String email, int providerId, String domain) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByProviderUuidAndProvideridAndDomainName(email, providerId, domain);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", email));
        } else {
            return user;
        }
    }
}
