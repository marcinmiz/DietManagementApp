package agh.edu.pl.diet.services.impl;

import agh.edu.pl.diet.entities.DietaryPreferences;
import agh.edu.pl.diet.entities.User;
import agh.edu.pl.diet.entities.Weight;
import agh.edu.pl.diet.exceptions.UserNotFoundException;
import agh.edu.pl.diet.payloads.request.ForgotPasswordRequest;
import agh.edu.pl.diet.payloads.request.ChangePasswordRequest;
import agh.edu.pl.diet.payloads.request.UserLoginRequest;
import agh.edu.pl.diet.payloads.request.UserRequest;
import agh.edu.pl.diet.payloads.response.ResponseMessage;
import agh.edu.pl.diet.payloads.validators.EmailValidator;
import agh.edu.pl.diet.payloads.validators.PasswordValidator;
import agh.edu.pl.diet.payloads.validators.UserValidator;
import agh.edu.pl.diet.repos.DietaryPreferencesRepo;
import agh.edu.pl.diet.repos.RoleRepo;
import agh.edu.pl.diet.repos.UserRepo;
import agh.edu.pl.diet.repos.WeightRepo;
import agh.edu.pl.diet.services.ImageService;
import agh.edu.pl.diet.services.SecurityService;
import agh.edu.pl.diet.services.UserService;
import agh.edu.pl.diet.utility.SecurityUtility;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private PasswordValidator passwordValidator;

    @Autowired
    private EmailValidator emailValidator;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private WeightRepo weightRepo;

    @Autowired
    private DietaryPreferencesRepo preferencesRepo;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private JavaMailSender mailSender;

    private final BCryptPasswordEncoder encoder = SecurityUtility.passwordEncoder();

    @Override
    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public ResponseMessage registerUser(UserRequest userRequest, BindingResult bindingResult) {
        userValidator.validate(userRequest, bindingResult);

        if (bindingResult.hasErrors()) {
            String message = bindingResult.getFieldError().getDefaultMessage();
            return new ResponseMessage(message);
        }

        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setName(userRequest.getName());
        user.setSurname(userRequest.getSurname());
        user.setPassword(encoder.encode(userRequest.getPassword()));
        String creationDate = new Date().toInstant().toString();
        user.setCreationDate(creationDate);
        user.setRole(roleRepo.findByName("USER"));
        user.setDietImprovement(1.0);

        userRepo.save(user);

        return new ResponseMessage("User " + user.getName() + " " + user.getSurname() + " has been registered");
    }

    @Override
    public ResponseMessage loginUser(UserLoginRequest userLoginRequest) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userLoginRequest.getUsername(), userLoginRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        if (authentication.isAuthenticated()) {

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User loggedUser = userRepo.findByUsername(userLoginRequest.getUsername());
            //compute daysDifference
            //if daysDifference is bigger than programmeDay, set programmeDay as daysDifference + 1, save
            //else do nothing
            if (loggedUser.getDietaryProgrammeStartDate() != null) {
                Calendar now = Calendar.getInstance();
                Instant startDateInstant = Instant.parse(loggedUser.getDietaryProgrammeStartDate());
                Calendar startDate = GregorianCalendar.from(ZonedDateTime.ofInstant(startDateInstant, ZoneId.systemDefault()));
                startDate.set(Calendar.HOUR_OF_DAY, 0);
                startDate.set(Calendar.MINUTE, 0);
                startDate.set(Calendar.SECOND, 0);

                if (now.after(startDate)) {
                    long end = now.getTimeInMillis();
                    long start = startDate.getTimeInMillis();
                    Integer daysBetween = Math.toIntExact(TimeUnit.MILLISECONDS.toDays(Math.abs(end - start)));
                    System.out.println(daysBetween);
                    if (daysBetween >= loggedUser.getCurrentDietaryProgrammeDay() && daysBetween + 1 <= loggedUser.getCurrentDietaryProgramme().getDietaryProgrammeDays()) {
                        loggedUser.setCurrentDietaryProgrammeDay(daysBetween + 1);
                        userRepo.save(loggedUser);
                    }
                }
            }
            return new ResponseMessage("User has been logged in");

        }

        return new ResponseMessage("User has not been logged in. Check username and password");
    }

    @Override
    public User getLoggedUser() {
        String username = securityService.findLoggedInUsername();
        if (username != null) {
            User user = userRepo.findByUsername(username);
            if (user != null) {
                String item_type = "avatar";
                String url = imageService.getImageURL(item_type, user.getUserId());
                user.setAvatarImage(url);
                return user;
            }
            return null;
        }
        return null;
    }

    @Override
    public Boolean existsUser(String username) {
        return userRepo.findByUsername(username) != null;
    }

    public void updateResetPasswordToken(String token, String email) throws UserNotFoundException {
        User user = userRepo.findByEmail(email);
        if (user != null) {
            user.setResetPasswordToken(token);
            userRepo.save(user);
        } else {
            throw new UserNotFoundException("Could not find any user with the email " + email);
        }
    }

    public User getByResetPasswordToken(String token) {
        return userRepo.findByResetPasswordToken(token);
    }

    public ResponseMessage changePassword(ChangePasswordRequest request, BindingResult bindingResult) {
        String resetType = request.getResetType();
        String token = request.getToken();
        String password = request.getPassword();
        User user = null;

        if (resetType.equalsIgnoreCase("forgot")) {
            user = getByResetPasswordToken(token);
        } else {
            user = findByUsername(getLoggedUser().getUsername());
        }

        if (user == null) {
            if (resetType.equalsIgnoreCase("forgot")) {
                return new ResponseMessage("Token is invalid");
            } else {
                return new ResponseMessage("Logged user has not been found");
            }
        } else {

            passwordValidator.validate(request, bindingResult);

            if (bindingResult.hasErrors()) {
                String message = bindingResult.getFieldError().getDefaultMessage();
                return new ResponseMessage(message);
            }

            String encodedPassword = encoder.encode(password);
            user.setPassword(encodedPassword);

            System.out.println("encodedPassword: " + user.getPassword());

            user.setResetPasswordToken(null);
            userRepo.save(user);
            return new ResponseMessage("You have successfully changed your password");
        }

    }

    @Override
    public ResponseMessage processForgotPassword(String email) {
        String token = RandomString.make(30);

        try {
            updateResetPasswordToken(token, email);
            String resetPasswordLink = SecurityUtility.getSiteURL() + "/resetPassword?token=" + token;
            sendEmail(email, resetPasswordLink);

        } catch (UserNotFoundException ex) {
            return new ResponseMessage(ex.getMessage());
        } catch (UnsupportedEncodingException | MessagingException e) {
            return new ResponseMessage("Error while sending email");
        }
        return new ResponseMessage("We have sent a reset password link to your email. Please check.");
    }

    @Override
    public void sendEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("contact@dietix.com", "Dietix Support");
        helper.setTo(recipientEmail);

        String subject = "Here's the link to reset your password";

        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }

    @Override
    public ResponseMessage checkTokenValidity(String token) {
        User user = getByResetPasswordToken(token);
        if (user == null) {
            return new ResponseMessage("Token is invalid");
        }
        return new ResponseMessage("Token is valid");
    }

    @Override
    public ResponseMessage changeEmail(ForgotPasswordRequest request, BindingResult bindingResult) {
        String email = request.getEmail();
        User user = findByUsername(getLoggedUser().getUsername());

        if (user == null) {
            return new ResponseMessage("Logged user has not been found");
        } else {

            emailValidator.validate(request, bindingResult);

            if (bindingResult.hasErrors()) {
                String message = bindingResult.getFieldError().getDefaultMessage();
                return new ResponseMessage(message);
            }

            user.setEmail(email);
            userRepo.save(user);
            return new ResponseMessage("You have successfully changed your e-mail");
        }
    }

    @Override
    public ResponseMessage addWeight(Double weight) {

        User user = findByUsername(getLoggedUser().getUsername());

        if (user == null) {
            return new ResponseMessage("Logged user has not been found");
        }

        Weight newWeight = new Weight(weight, user);

        weightRepo.save(newWeight);

        return new ResponseMessage("Weight " + weight + " has been added");
    }

    @Override
    public List<String> countMovingAverage(List<Weight> weightList) {
        Double movingAverageValue;
        List<String> movingAverage = new ArrayList<>();
        List<Weight> weights;
        Integer minimumWeightsSkipMode = 5;
        if (weightList.size() < 5) {
            return null;
        }
        if (weightList.size() > minimumWeightsSkipMode)
            weights = weightList.stream().skip(weightList.size() - minimumWeightsSkipMode).limit(5).collect(Collectors.toList());
        else
            weights = weightList.stream().limit(5).collect(Collectors.toList());

        movingAverageValue = weights.stream().mapToDouble(Weight::getWeightValue).sum() / weights.size();
        movingAverage.add(movingAverageValue.toString());
        movingAverage.add(weights.get(weights.size()-1).getMeasureDate());
        return movingAverage;
    }

    @Override
    public List<List<Weight>> getLoggedUserWeights() {

        User measurer = findByUsername(getLoggedUser().getUsername());

        if (measurer == null) {
            return null;
        }
        List<List<Weight>> weightList = new ArrayList<>();
        weightList.add(new ArrayList<>(weightRepo.findByMeasurer(measurer)));

        weightList.get(0).sort(Comparator.comparing(w -> Instant.parse(w.getMeasureDate())));

        Integer trendThreshold = 4, iterationLimit = weightList.get(0).size();

        if (measurer.getCurrentDietaryProgramme() != null) {
            trendThreshold = 5;
            Instant lastProgrammeDayInstant = Instant.parse(measurer.getDietaryProgrammeStartDate());
            Calendar lastProgrammeDay = GregorianCalendar.from(ZonedDateTime.ofInstant(lastProgrammeDayInstant, ZoneId.systemDefault()));
            lastProgrammeDay.add(Calendar.DAY_OF_MONTH, measurer.getCurrentDietaryProgramme().getDietaryProgrammeDays() + 1);

            DietaryPreferences preference = preferencesRepo.findByRelatedDietaryProgramme(measurer.getCurrentDietaryProgramme());

            if (preference == null) {
                return null;
            }

            Weight weight = new Weight(preference.getTargetWeight(), measurer);
            weight.setMeasureDate(lastProgrammeDay.toInstant().toString());
            weightList.get(0).add(weight);
        }

        weightList.add(new ArrayList<>());

        if (weightList.get(0).size()-1 >= trendThreshold) {
            for (int i = 4; i < iterationLimit; i++) {
                List<String> movingAverage = countMovingAverage(weightList.get(0).subList(0, i + 1));
                Weight weight = new Weight(Double.parseDouble(movingAverage.get(0)), measurer);
                weight.setMeasureDate(movingAverage.get(1));
                weightList.get(1).add(weight);
            }
        }

        return weightList;
    }

    @Override
    public Double getWeightTrend() {
        User measurer = findByUsername(getLoggedUser().getUsername());

        if (measurer == null) {
            return null;
        }
        List<Weight> weightList = weightRepo.findByMeasurer(measurer);

        weightList.sort(Comparator.comparing(w -> Instant.parse(w.getMeasureDate())));

//        int index = weightList.size() - 5;

        if (weightList.isEmpty()) {
            return 0.0;
        }

        List<String> trend = countMovingAverage(weightList);

        if (trend == null) {
            return 0.0;
        }

        Double movingAverageValue = Double.parseDouble(trend.get(0));

        Double lastMeasurementValue = weightList.get(weightList.size() - 1).getWeightValue();

        Double weightTrend = ((lastMeasurementValue - movingAverageValue) / movingAverageValue) * 100;

        Long tempWeightTrend = Math.round(weightTrend * 100);

        return Double.valueOf(tempWeightTrend) / 100;
    }
}