package agh.edu.pl.diet.services;

import agh.edu.pl.diet.entities.User;
import agh.edu.pl.diet.exceptions.UserNotFoundException;
import agh.edu.pl.diet.payloads.request.ResetPasswordRequest;
import agh.edu.pl.diet.payloads.request.UserLoginRequest;
import agh.edu.pl.diet.payloads.request.UserRequest;
import agh.edu.pl.diet.payloads.response.ResponseMessage;
import org.springframework.validation.BindingResult;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;


public interface UserService {

    User findByUsername(String username);

    ResponseMessage registerUser(UserRequest userRequest, BindingResult bindingResult);

    ResponseMessage loginUser(UserLoginRequest userLoginRequest);

    User getLoggedUser();

    Boolean existsUser(String username);

    void updateResetPasswordToken(String token, String email) throws UserNotFoundException;

    User getByResetPasswordToken(String token);

    ResponseMessage resetPassword(ResetPasswordRequest request, BindingResult bindingResult);

    ResponseMessage processForgotPassword(String email);

    void sendEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException;

    ResponseMessage checkTokenValidity(String token);

}