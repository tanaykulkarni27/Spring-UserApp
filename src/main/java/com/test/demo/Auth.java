package com.test.demo;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class Auth{
    @Value("${app.secret}")
    String JWT_SECRET;
    private UserRepository userRepository;
    public Auth(UserRepository usrrepo){
        this.userRepository = usrrepo;
    }

    @GetMapping("/")
    public HashMap home(){
        HashMap m = new HashMap();
        m.put("status",true);
        m.put("data",userRepository.findAll());
        return m;
    }

    @PostMapping("/login")
    public HashMap Login(HttpServletRequest req, @RequestBody HashMap<String,String>data) {
        String name = data.get("name");
        String password =  data.get("password");
        String hashedpassword = userRepository.getHashedPassword(name);
        if( BCrypt.verifyer().verify(password.toCharArray(),hashedpassword).verified) {
            HashMap response = new HashMap();
            response.put("status", true);
            response.put("token", true);
            return response;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid username or password");
    }

    @PostMapping("/createuser")
    public HashMap Createuser(HttpServletRequest req, @RequestBody HashMap<String,String>data) {
        String name = data.get("name");
        String password =  data.get("password");
        String salary =  data.get("salary");
        HashMap response = new HashMap();

        if(salary == null || Integer.parseInt(salary) == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid salary amount");
        }

        User usr = new User();
        usr.setName(name);
        String HashedPassword = BCrypt.withDefaults().hashToString(12,password.toCharArray());

        usr.setPassword(HashedPassword);
        usr.setSalary(salary);
        userRepository.save(usr);
        response.put("status", true);
        return response;
    }
}