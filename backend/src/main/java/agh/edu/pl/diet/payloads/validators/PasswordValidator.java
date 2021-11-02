package agh.edu.pl.diet.payloads.validators;

import agh.edu.pl.diet.payloads.request.ResetPasswordRequest;
import agh.edu.pl.diet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class PasswordValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return ResetPasswordRequest.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ResetPasswordRequest password = (ResetPasswordRequest) o;

        if (password.getPassword() == null) {
            errors.rejectValue("password", "Null.userForm.password", "Password has to to be given");
            return;
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty", "Password has not to be empty or contains only spaces");
        if (password.getPassword().length() < 8 || password.getPassword().length() > 40) {
            errors.rejectValue("password", "Size.userForm.password", "Password has to have min 8 and max 40 characters");
        }

        if (!password.getPassword().matches("^[a-zA-Z0-9]+$")) {
            errors.rejectValue("password", "Format.userForm.password", "Password has to contain only letters and digits");
        }

        if (password.getPasswordConfirmation() == null) {
            errors.rejectValue("passwordConfirmation", "Null.userForm.passwordConfirmation", "PasswordConfirmation has to be given");
            return;
        }

        if (!password.getPasswordConfirmation().equals(password.getPassword())) {
            errors.rejectValue("passwordConfirmation", "Diff.userForm.passwordConfirmation", "Password has to equals with PasswordConfirmation");
        }
    }
}
