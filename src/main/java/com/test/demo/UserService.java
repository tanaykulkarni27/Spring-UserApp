package com.test.demo;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;

@Service
public class UserService {
    @Value("${app.secret}")
    String JWT_SECRET;
    private final UserRepository userRepository;
    public UserService(UserRepository usrrepo){
        this.userRepository = usrrepo;
    }
    public HashMap getUsers(){
        HashMap m = new HashMap();
        m.put("status",true);
        m.put("data",userRepository.findAll());
        return m;
    }
    boolean login(String name,String password){
        String hashedpassword = userRepository.getHashedPassword(name);
        if( BCrypt.verifyer().verify(password.toCharArray(),hashedpassword).verified)
            return false;
        return true;
    }
    boolean createUser(String name,String password,int salary){
        if(salary <= 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid salary amount");

        User usr = new User();
        usr.setName(name);
        String HashedPassword = BCrypt.withDefaults().hashToString(12,password.toCharArray());

        usr.setPassword(HashedPassword);
        usr.setSalary(salary);
        userRepository.save(usr);
        return true;
    }
}
