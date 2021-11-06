package agh.edu.pl.diet.payloads.validators;

import agh.edu.pl.diet.payloads.request.ForgotPasswordRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class EmailValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return ForgotPasswordRequest.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ForgotPasswordRequest email = (ForgotPasswordRequest) o;

        if (email.getEmail() == null) {
            errors.rejectValue("email", "Null.userForm.email", "E-mail address has to to be given");
            return;
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty", "E-mail address has not to be empty or contains only spaces");

        if (!email.getEmail().matches("^[a-zA-Z0-9._]+@[a-zA-Z0-9.]+.[a-zA-Z]+$")) {
            errors.rejectValue("email", "Format.userForm.email", "E-mail address has to have format id@example.com");
        }
        if (email.getEmail().length() < 6 || email.getEmail().length() > 40) {
            errors.rejectValue("email", "Size.userForm.email", "E-mail address has to have min 6 and max 40 characters");
        }

    }
}
