package agh.edu.pl.diet.services.impl;

import agh.edu.pl.diet.entities.Role;
import agh.edu.pl.diet.entities.User;
import agh.edu.pl.diet.repos.RoleRepo;
import agh.edu.pl.diet.repos.UserRepo;
import agh.edu.pl.diet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;

@Service
public class UserServiceImpl implements UserService {

//    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

//    private ResponseMessage verify(String mode, String type, Object item) {
//        switch (type) {
//            case "username":
//                String name = String.valueOf(item);
//                if (name == null) {
//                    return new ResponseMessage("Username name has to be given");
//                } else if (name.length() < 2 || name.length() > 40) {
//                    return new ResponseMessage("Username name has to have min 2 and max 40 characters");
//                } else if (!(name.matches("^[a-zA-Z ]+$"))) {
//                    return new ResponseMessage("Username name has to contain only letters and spaces");
//                } else if (!mode.equals("username") && userRepo.findByName(name) != null) {
//                    return new ResponseMessage("Username with this name exists yet");
//                } else {
//                    return new ResponseMessage("Username name is valid");
//                }
//            case "password":
//                if (item == null) {
//                    return new ResponseMessage("Password calories has to be given");
//                }
//
//                String password = String.valueOf(item);
//
//                if (password.toString().length() < 2) {
//                    return new ResponseMessage("Password has to have min 3 characters");
//                } else if (!(password.toString().matches("^0$") || password.toString().matches("^(-)?[1-9]\\d*$"))) {
//                    return new ResponseMessage("Password has to contain only digits");
//                } else if (password.length() < 0) {
//                    return new ResponseMessage("Password has to be greater or equal 0");
//                } else {
//                    return new ResponseMessage("Password are valid");
//                }
//        }
//
//        return new ResponseMessage("Invalid type");
//    }

    //@Override
//    public User createUser(User user, Set<UserRole> userRoles) {
//        User localUser = userRepo.findByUsername(user.getUsername());
//
//        if (localUser != null) {
//            LOG.info("user {} already exists. Nothing will be done.", user.getUsername());
//        } else {
//            for (UserRole ur : userRoles) {
//                roleRepo.save(ur.getRole());
//            }
//
//            user.getUserRoles().addAll(userRoles);
//
//            localUser = userRepo.save(user);
//        }
//
//        return localUser;
//    }

//    @Override
//    public User getCreateUser(User user, Set<UserRole> userRoles) throws Exception {
//        return null;
//    }

    @Override
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>((Collection<? extends Role>) roleRepo.findAll()));
        userRepo.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public User findByName(String email) {
        return userRepo.findByName(email);
    }


//    @Override
//    public ResponseMessage getCreateUser(ProductRequest userRequest) {
//        return null;
//    }
//
//    @Override
//    public ResponseMessage addNewUser(UserRequest userRequest) {
//        return null;
//    }

}