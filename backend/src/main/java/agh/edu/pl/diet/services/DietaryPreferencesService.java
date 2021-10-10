package agh.edu.pl.diet.services;

import agh.edu.pl.diet.entities.DietaryPreferences;
import agh.edu.pl.diet.payloads.request.DietaryPreferencesRequest;
import agh.edu.pl.diet.payloads.response.ResponseMessage;

import java.util.List;


public interface DietaryPreferencesService {

  //  List<DietaryPreferences> getAllPreferencesService();

    List<DietaryPreferences> getUserDietaryPreferences();

    DietaryPreferences getDietaryPreferences(Long dietaryPreferencesId);

    ResponseMessage addNewDietaryPreferences(DietaryPreferencesRequest dietaryPreferencesRequest);

    ResponseMessage updateDietaryPreferences(Long dietaryPreferencesId, DietaryPreferencesRequest dietaryPreferencesRequest);

    ResponseMessage removeDietaryPreferences(Long dietaryPreferencesId);

}
