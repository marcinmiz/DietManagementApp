package agh.edu.pl.diet.services;

import agh.edu.pl.diet.entities.DietaryPreferences;
import agh.edu.pl.diet.entities.DietaryPreferencesNutrient;
import agh.edu.pl.diet.entities.DietaryProgramme;
import agh.edu.pl.diet.payloads.request.DietaryProgrammeRequest;
import agh.edu.pl.diet.payloads.response.ResponseMessage;
import agh.edu.pl.diet.repos.DietaryPreferencesRepo;
import agh.edu.pl.diet.repos.DietaryProgrammeRepo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DietaryProgrammeService {

    ResponseMessage createDailyMenusWithMeals(DietaryPreferences dietaryPreference, DietaryProgramme programme, String creatingType);

    ResponseMessage addNewDietaryProgramme(DietaryProgrammeRequest dietaryProgrammeRequest);

    List<DietaryProgramme> getUserDietaryProgrammes();

    ResponseMessage useDietaryProgramme(Long programmeId, String type);

    ResponseMessage finishDietaryProgramme(Long programmeId);

    ResponseMessage updateDietaryProgramme(DietaryProgramme programme);

    ResponseMessage editDietaryProgramme(Long programmeId, DietaryProgrammeRequest dietaryProgrammeRequest);

    ResponseMessage removeDietaryProgramme(Long programmeId);

}
