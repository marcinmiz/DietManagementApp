package agh.edu.pl.diet.services;

import agh.edu.pl.diet.entities.DietaryProgramme;
import agh.edu.pl.diet.payloads.request.DietaryProgrammeRequest;
import agh.edu.pl.diet.payloads.response.ResponseMessage;

import java.util.List;

public interface DietaryProgrammeService {

    ResponseMessage addNewDietaryProgramme(DietaryProgrammeRequest dietaryProgrammeRequest);

    List<DietaryProgramme> getUserDietaryProgrammes();

    ResponseMessage useDietaryProgramme(Long programmeId, String type);

    ResponseMessage finishDietaryProgramme(Long programmeId);

    ResponseMessage removeDietaryProgramme(Long programmeId);

}
