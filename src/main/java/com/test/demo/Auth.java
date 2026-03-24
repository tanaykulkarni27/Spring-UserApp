package com.test.demo;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class Auth{
    UserService userservice;
    JWTAuth jwtService;

    Auth(UserService usrsrv,JWTAuth jwtSrv){
        this.userservice = usrsrv;
        this.jwtService = jwtSrv;
    }

    @GetMapping("/")
    public HashMap home(){
        return userservice.getUsers();
    }

    @PostMapping("/login")
    public HashMap Login(HttpServletRequest req, @RequestBody HashMap<String,String>data) {
        String name = data.get("name");
        String password =  data.get("password");
        if(userservice.login(name,password)) {
            HashMap response = new HashMap();
            String token = jwtService.generateToken(name);
            response.put("status", true);
            response.put("token", token);
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
        userservice.createUser(name,password,Integer.parseInt(salary));
        response.put("status", true);
        return response;
    }
}