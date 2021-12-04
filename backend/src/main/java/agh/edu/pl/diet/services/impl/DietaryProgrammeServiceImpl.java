package agh.edu.pl.diet.services.impl;

import agh.edu.pl.diet.entities.*;
import agh.edu.pl.diet.payloads.request.DietaryProgrammeRequest;
import agh.edu.pl.diet.payloads.response.ResponseMessage;
import agh.edu.pl.diet.repos.*;
import agh.edu.pl.diet.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DietaryProgrammeServiceImpl implements DietaryProgrammeService, MenusAndMealsService {

    @Autowired
    private MealRepo mealRepo;
    @Autowired
    private DailyMenuRepo dailyMenuRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private DietaryProgrammeRepo dietaryProgrammeRepo;
    @Autowired
    private DietaryPreferencesRepo dietaryPreferencesRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private DietaryPreferencesService dietaryPreferencesService;

    @Autowired
    private DailyMenuService dailyMenuService;

    @Override
    public ResponseMessage createDailyMenusWithMeals(DietaryPreferences dietaryPreference, DietaryProgramme programme, String creatingType) {
        if (dietaryPreference == null) {
            return new ResponseMessage("Dietary preference is required");
        }

        if (programme == null) {
            return new ResponseMessage("Dietary programme is required");
        }

        if (!creatingType.equals("create") && !creatingType.equals("update")) {
            return new ResponseMessage("Bad creating type");
        }

        Set<DietaryPreferencesNutrient> nutrients = dietaryPreference.getNutrients();
        Map<String, Double> totalDailyNutrients = new LinkedHashMap<>();

        for (DietaryPreferencesNutrient dietaryPreferencesNutrient : nutrients) {
            totalDailyNutrients.put(dietaryPreferencesNutrient.getNutrient().getNutrientName(), dietaryPreferencesNutrient.getNutrientAmount());
        }

        Integer relatedProgrammeDays = programme.getDietaryProgrammeDays();

        if (creatingType.equals("create")) {
            dietaryProgrammeRepo.save(programme);

            System.out.println("saving prog");

            dietaryPreference.setRelatedDietaryProgramme(programme);
            dietaryPreferencesRepo.save(dietaryPreference);
            System.out.println("saving pref");
        }

        for (int i = 1; i <= relatedProgrammeDays; i++) {
            if (!dailyMenuService.addNewDailyMenu(programme, dietaryPreference.getTotalDailyCalories(), dietaryPreference.getMealsQuantity(), totalDailyNutrients, i, relatedProgrammeDays).getMessage().equals("Daily Menu has been added")) {

                dietaryPreference.setRelatedDietaryProgramme(null);
                dietaryPreferencesRepo.save(dietaryPreference);
                System.out.println("deleting pref");

                System.out.println("deleting prog");
                dietaryProgrammeRepo.delete(programme);
                return new ResponseMessage("Daily Menu has not been created");
            }
        }

        return new ResponseMessage("Daily Menus with meals has been created");

    }

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

        if (dietaryPreference.getRelatedDietaryProgramme() != null) {
            return new ResponseMessage("Dietary preference with id " + dietaryPreference.getDietaryPreferenceId() + " has related dietary programme yet");
        }

        if (createDailyMenusWithMeals(dietaryPreference, dietaryProgramme, "create").getMessage().equals("Daily Menus with meals has been created")) {
            return new ResponseMessage("Dietary Programme has been added");
        } else {
            return new ResponseMessage("Dietary Programme has not been added");
        }
    }

    @Override
    public List<DietaryProgramme> getUserDietaryProgrammes() {

        User currentLoggedUser = userService.findByUsername(userService.getLoggedUser().getUsername());
        if (currentLoggedUser == null) {
            return null;
        }

        return dietaryProgrammeRepo.findByOwnerUserId(currentLoggedUser.getUserId());
    }

    @Override
    public ResponseMessage useDietaryProgramme(Long programmeId, String type) {

        if (!type.equalsIgnoreCase("start") && !type.equalsIgnoreCase("abandon")) {
            return new ResponseMessage("Type should be equal to \"start\" or \"abandon\"");
        }

        DietaryProgramme programme = dietaryProgrammeRepo.findById(programmeId).orElse(null);
        if (programme == null) {
            return new ResponseMessage("Dietary programme with id " + programmeId + " has not been found");
        }

        Calendar startDate = Calendar.getInstance();
        Calendar beforeBreakfast = Calendar.getInstance();
        beforeBreakfast.set(Calendar.HOUR_OF_DAY, 8);
        beforeBreakfast.set(Calendar.MINUTE, 0);
        beforeBreakfast.set(Calendar.SECOND, 0);

        if (startDate.after(beforeBreakfast)) {
            startDate.add(Calendar.DAY_OF_MONTH, 1);
        }

        User programmeOwner = programme.getOwner();

        if (programmeOwner == null) {
            return new ResponseMessage("Dietary programme owner has not been found");
        }

        if (type.equalsIgnoreCase("start")) {
            programmeOwner.setCurrentDietaryProgramme(programme);
            programmeOwner.setCurrentDietaryProgrammeDay(1);
            programmeOwner.setDietaryProgrammeStartDate(startDate.toInstant().toString());
        } else {
            programmeOwner.setCurrentDietaryProgramme(null);
            programmeOwner.setCurrentDietaryProgrammeDay(null);
            programmeOwner.setDietaryProgrammeStartDate(null);
        }

        userRepo.save(programmeOwner);

        List<DailyMenu> menus = dailyMenuRepo.findByDietaryProgramme(programme);

        menus.sort((m1, m2) -> {
            Integer dayNumber1 = Integer.valueOf(m1.getDailyMenuName().split(" ")[1]);
            Integer dayNumber2 = Integer.valueOf(m2.getDailyMenuName().split(" ")[1]);
            return dayNumber1.compareTo(dayNumber2);
        });

        for (int i = 0; i < programme.getDietaryProgrammeDays(); i++) {

            DailyMenu menu = menus.get(i);

            if (type.equalsIgnoreCase("start")) {
                startDate.add(Calendar.DAY_OF_MONTH, i == 0 ? 0 : 1);
                menu.setDailyMenuDate(startDate.toInstant().toString());
            } else {
                menu.setDailyMenuDate(null);
            }

            dailyMenuRepo.save(menu);

            List<Meals> meals = mealRepo.findByDailyMenu(menu);

            for (Meals meal : meals) {

                if (type.equalsIgnoreCase("start")) {
                    switch (meal.getMealsName()) {
                        case "breakfast":
                            startDate.set(Calendar.HOUR_OF_DAY, 8);
                            break;
                        case "lunch":
                            startDate.set(Calendar.HOUR_OF_DAY, 11);
                            break;
                        case "dinner":
                            startDate.set(Calendar.HOUR_OF_DAY, 14);
                            break;
                        case "tea":
                            startDate.set(Calendar.HOUR_OF_DAY, 17);
                            break;
                        case "supper":
                            startDate.set(Calendar.HOUR_OF_DAY, 20);
                            break;
                        default:
                            return new ResponseMessage("Wrong meal name");
                    }

                    startDate.set(Calendar.MINUTE, 0);
                    startDate.set(Calendar.SECOND, 0);

                    meal.setMealHourTime(startDate.toInstant().toString());
                } else {
                    meal.setMealHourTime(null);
                }
                mealRepo.save(meal);
            }

        }

        if (type.equalsIgnoreCase("start")) {
            return new ResponseMessage("Dietary programme " + programme.getDietaryProgrammeName() + " has been started");
        } else {
            return new ResponseMessage("Dietary programme " + programme.getDietaryProgrammeName() + " has been abandoned");
        }

    }

    @Override
    public ResponseMessage finishDietaryProgramme(Long programmeId) {

        String type = "abandon";

        DietaryProgramme programme = dietaryProgrammeRepo.findById(programmeId).orElse(null);
        if (programme == null) {
            return new ResponseMessage("Dietary programme with id " + programmeId + " has not been found");
        }

        if (useDietaryProgramme(programmeId, type).getMessage().endsWith(" has been abandoned")) {

            User programmeOwner = programme.getOwner();
            Double improvementStep = 0.05;

            if (programmeOwner.getDietImprovement() > improvementStep) {
                programmeOwner.setDietImprovement(programmeOwner.getDietImprovement() - improvementStep);
                userRepo.save(programmeOwner);
            }
            return new ResponseMessage("Dietary programme " + programme.getDietaryProgrammeName() + " has been finished");

        } else {
            return new ResponseMessage("Dietary programme " + programme.getDietaryProgrammeName() + " has not been finished");
        }

    }

    @Override
    public ResponseMessage updateDietaryProgramme(DietaryProgramme programme) {

        Boolean condition = false;

        if (programme == null) {
            return new ResponseMessage("Dietary Programme does not exist");
        }

        DietaryPreferences preference = dietaryPreferencesRepo.findByRelatedDietaryProgramme(programme);

        if (preference == null) {
            return new ResponseMessage("Cannot update dietary programme, which is related to no dietary programme");
        }

        while (!condition){
            condition = removeDailyMenusWithMeals(programme);
            System.out.println("waiting");
        }

        System.out.println("removed");

        if (createDailyMenusWithMeals(preference, programme, "update").getMessage().equals("Daily Menus with meals has been created")) {
            return new ResponseMessage("Dietary Programme has been updated");
        } else {
            return new ResponseMessage("Dietary Programme has not been updated");
        }
    }

    @Override
    public ResponseMessage editDietaryProgramme(Long programmeId, DietaryProgrammeRequest dietaryProgrammeRequest) {

        User loggedUser = userService.findByUsername(userService.getLoggedUser().getUsername());

        if (loggedUser == null) {
            return new ResponseMessage("Logged user has not been found");
        }

        DietaryProgramme programme = dietaryProgrammeRepo.findById(programmeId).orElse(null);

        if (programme == null) {
            return new ResponseMessage("Dietary Programme does not exist");
        }

        if (loggedUser.getCurrentDietaryProgramme() == null || !loggedUser.getCurrentDietaryProgramme().equals(programme)) {

            String programmeName = dietaryProgrammeRequest.getDietaryProgrammeName();

            if (!programme.getDietaryProgrammeName().equals(programmeName)) {
                programme.setDietaryProgrammeName(programmeName);
            }

            Integer programmeDays = dietaryProgrammeRequest.getDietaryProgrammeDays();

            Long preferenceId = dietaryProgrammeRequest.getPreferenceId();

            DietaryPreferences currentPreference = dietaryPreferencesRepo.findByRelatedDietaryProgramme(programme);

            if (currentPreference == null) {
                return new ResponseMessage("Dietary Programme is related to no dietary preference");
            }

            if (!currentPreference.getDietaryPreferenceId().equals(preferenceId)) {

                DietaryPreferences newPreference = dietaryPreferencesRepo.findById(preferenceId).orElse(null);

                if (newPreference == null) {
                    return new ResponseMessage("Dietary Programme is related to no dietary preference");
                }

                if (newPreference.getRelatedDietaryProgramme() != null) {
                    return new ResponseMessage("New Dietary Preference is occupied");
                }

                newPreference.setRelatedDietaryProgramme(programme);
                currentPreference.setRelatedDietaryProgramme(null);

                dietaryPreferencesRepo.save(currentPreference);
                dietaryPreferencesRepo.save(newPreference);

                if (programme.getDietaryProgrammeDays().equals(programmeDays)) {
                    ResponseMessage updatedMessage = updateDietaryProgramme(programme);
                    if (!updatedMessage.getMessage().equals("Dietary Programme has been updated")) {

                        currentPreference.setRelatedDietaryProgramme(programme);
                        newPreference.setRelatedDietaryProgramme(null);

                        dietaryPreferencesRepo.save(currentPreference);
                        dietaryPreferencesRepo.save(newPreference);

                        return new ResponseMessage("Cannot change dietary preference related to " + programme.getDietaryProgrammeName() + " dietary programme");
                    }
                }
            }

            if (!programme.getDietaryProgrammeDays().equals(programmeDays)) {
                if (programmeDays < 1) {
                    return new ResponseMessage("Dietary Programme has to last at least 1 day");
                }
                programme.setDietaryProgrammeDays(programmeDays);

                ResponseMessage updatedMessage = updateDietaryProgramme(programme);

                if (!updatedMessage.getMessage().equals("Dietary Programme has been updated")) {

                    return new ResponseMessage("Cannot change dietary programme related to " + programme.getDietaryProgrammeName() + " dietary programme");
                }
            }

            dietaryProgrammeRepo.save(programme);

            return new ResponseMessage("Dietary Programme " + programme.getDietaryProgrammeName() + " has been edited");
        }

        return new ResponseMessage("Current dietary programme cannot be edited");
    }

    @Override
    public ResponseMessage removeDietaryProgramme(Long programmeId) {

        User currentLoggedOwner = userService.findByUsername(userService.getLoggedUser().getUsername());
        if (currentLoggedOwner == null) {
            return new ResponseMessage("Dietary programme owner has not been found");
        }

        DietaryProgramme programme = dietaryProgrammeRepo.findById(programmeId).orElse(null);
        if (programme == null) {
            return new ResponseMessage("Dietary programme with id " + programmeId + " has not been found");
        }

        removeDailyMenusWithMeals(programme);

        DietaryPreferences dietaryPreference = dietaryPreferencesRepo.findByRelatedDietaryProgramme(programme);

        if (dietaryPreference == null) {
            return new ResponseMessage("Dietary preference related to dietary programme with id " + programmeId + " has not been found");
        }

        dietaryPreference.setRelatedDietaryProgramme(null);
        dietaryPreferencesRepo.save(dietaryPreference);

        dietaryProgrammeRepo.delete(programme);

        return new ResponseMessage("Dietary programme " + programme.getDietaryProgrammeName() + " has been removed");
    }

    @Override
    public Boolean removeDailyMenusWithMeals(DietaryProgramme programme) {
        List<DailyMenu> menus = dailyMenuRepo.findByDietaryProgramme(programme);
        for (DailyMenu menu : menus) {
            List<Meals> meals = mealRepo.findByDailyMenu(menu);
            for (Meals meal : meals) {
                mealRepo.delete(meal);
            }
            dailyMenuRepo.delete(menu);
        }
        return true;
    }
}
