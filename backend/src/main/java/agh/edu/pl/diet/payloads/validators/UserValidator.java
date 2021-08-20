package agh.edu.pl.diet.payloads.validators;

import agh.edu.pl.diet.payloads.request.UserRequest;
import agh.edu.pl.diet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {
    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> aClass) {
        return UserRequest.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserRequest user = (UserRequest) o;

        if (user.getUsername() == null) {
            errors.rejectValue("username", "Null.userForm.username", "Username has to to be given");
            return;
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty", "Username has not to be empty or contains only spaces");
        if (user.getUsername().trim().length() < 6 || user.getUsername().length() > 32) {
            errors.rejectValue("username", "Size.userForm.username", "Username has to have min 6 and max 32 characters");
        }

        if (!user.getUsername().matches("^[a-zA-Z 0-9]+$")) {
            errors.rejectValue("username", "Format.userForm.username", "Username has to contain only letters, digits and spaces");
        }
        if (userService.findByUsername(user.getUsername()) != null) {
            errors.rejectValue("username", "Duplicate.userForm.username", "Username has to be unique");
        }

        if (user.getName() == null) {
            errors.rejectValue("name", "Null.userForm.name", "Name has to to be given");
            return;
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty", "Name has not to be empty or contains only spaces");
        if (user.getName().trim().length() < 2 || user.getName().length() > 40) {
            errors.rejectValue("name", "Size.userForm.name", "Name has to have min 2 and max 40 characters");
        }
        if (!user.getName().matches("^[a-zA-Z ]+$")) {
            errors.rejectValue("name", "Format.userForm.name", "Name has to contain only letters and spaces");
        }

        if (user.getSurname() == null) {
            errors.rejectValue("surname", "Null.userForm.surname", "Surname has to to be given");
            return;
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "surname", "NotEmpty", "Surname has not to be empty or contains only spaces");
        if (user.getSurname().trim().length() < 2 || user.getSurname().length() > 40) {
            errors.rejectValue("surname", "Size.userForm.surname", "Surname has to have min 2 and max 40 characters");
        }
        if (!user.getSurname().matches("^[a-zA-Z ]+$")) {
            errors.rejectValue("surname", "Format.userForm.surname", "Surname has to contain only letters and spaces");
        }

        if (user.getPassword() == null) {
            errors.rejectValue("password", "Null.userForm.password", "Password has to to be given");
            return;
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty", "Password has not to be empty or contains only spaces");
        if (user.getPassword().length() < 8 || user.getPassword().length() > 40) {
            errors.rejectValue("password", "Size.userForm.password", "Password has to have min 8 and max 40 characters");
        }

        if (!user.getPassword().matches("^[a-zA-Z0-9]+$")) {
            errors.rejectValue("password", "Format.userForm.password", "Password has to contain only letters and digits");
        }

        if (user.getPasswordConfirmation() == null) {
            errors.rejectValue("passwordConfirmation", "Null.userForm.passwordConfirmation", "PasswordConfirmation has to be given");
            return;
        }

        if (!user.getPasswordConfirmation().equals(user.getPassword())) {
            errors.rejectValue("passwordConfirmation", "Diff.userForm.passwordConfirmation", "Password has to equals with PasswordConfirmation");
        }

        if (user.getEmail() == null) {
            errors.rejectValue("email", "Null.userForm.email", "E-mail address has to to be given");
            return;
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty", "E-mail address has not to be empty or contains only spaces");

        if (!user.getEmail().matches("^[a-zA-Z0-9._]+@[a-zA-Z0-9.]+.[a-zA-Z]+$")) {
            errors.rejectValue("email", "Format.userForm.email", "E-mail address has to have format id@example.com");
        }
        if (user.getEmail().length() < 6 || user.getEmail().length() > 40) {
            errors.rejectValue("email", "Size.userForm.email", "E-mail address has to have min 6 and max 40 characters");
        }
    }
}
