package agh.edu.pl.diet.services.impl;

import agh.edu.pl.diet.entities.DietaryPreferences;
import agh.edu.pl.diet.entities.DietaryPreferencesNutrient;
import agh.edu.pl.diet.entities.DietaryProgramme;
import agh.edu.pl.diet.entities.User;
import agh.edu.pl.diet.payloads.request.DietaryProgrammeRequest;
import agh.edu.pl.diet.payloads.response.ResponseMessage;
import agh.edu.pl.diet.repos.DietaryProgrammeRepo;
import agh.edu.pl.diet.services.DailyMenuService;
import agh.edu.pl.diet.services.DietaryPreferencesService;
import agh.edu.pl.diet.services.DietaryProgrammeService;
import agh.edu.pl.diet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Service
public class DietaryProgrammeServiceImpl implements DietaryProgrammeService {

    @Autowired
    private DietaryProgrammeRepo dietaryProgrammeRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private DietaryPreferencesService dietaryPreferencesService;

    @Autowired
    private DailyMenuService dailyMenuService;

    @Override
    public ResponseMessage addNewDietaryProgramme(DietaryProgrammeRequest dietaryProgrammeRequest) {
        DietaryProgramme dietaryProgramme = new DietaryProgramme();

        dietaryProgramme.setDietaryProgrammeName(dietaryProgrammeRequest.getDietaryProgrammeName());

        User currentLoggedOwner = userService.findByUsername(userService.getLoggedUser().getUsername());
        if (currentLoggedOwner == null) {
            return new ResponseMessage("Dietary programme owner has not been found");
        }

        dietaryProgramme.setOwner(currentLoggedOwner);

        Integer dietaryProgrammeDays = dietaryProgrammeRequest.getDietaryProgrammeDays();

        if (dietaryProgrammeDays < 1) {
            return new ResponseMessage("Dietary programme days has to be at least equal to 1");
        }

        dietaryProgramme.setDietaryProgrammeDays(dietaryProgrammeDays);

        DietaryPreferences dietaryPreference = dietaryPreferencesService.getDietaryPreferences(dietaryProgrammeRequest.getPreferenceId());

        if (dietaryPreference == null) {
            return new ResponseMessage("Dietary preference with id " + dietaryProgrammeRequest.getPreferenceId() + " has not been found");
        }

        Set<DietaryPreferencesNutrient> nutrients = dietaryPreference.getNutrients();
        Map<String, Double> totalDailyNutrients = new LinkedHashMap<>();

        for (DietaryPreferencesNutrient dietaryPreferencesNutrient: nutrients) {
            totalDailyNutrients.put(dietaryPreferencesNutrient.getNutrient().getNutrientName(), dietaryPreferencesNutrient.getNutrientAmount());
        }

//        Calendar startDate = Calendar.getInstance();

        dietaryProgrammeRepo.save(dietaryProgramme);

        for (int i = 1; i <= dietaryProgrammeDays; i++) {
            if (!dailyMenuService.addNewDailyMenu(dietaryProgramme, dietaryPreference.getTotalDailyCalories(), dietaryPreference.getMealsQuantity(), totalDailyNutrients, i, dietaryProgrammeDays).getMessage().equals("Daily Menu has been added")) {
                return new ResponseMessage("Daily Menu has not been created");
            }
        }

        return new ResponseMessage("Dietary Programme has been added");
    }
}
