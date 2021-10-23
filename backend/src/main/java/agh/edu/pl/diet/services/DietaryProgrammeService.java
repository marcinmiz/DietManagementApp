package agh.edu.pl.diet.services;

import agh.edu.pl.diet.payloads.request.DietaryProgrammeRequest;
import agh.edu.pl.diet.payloads.response.ResponseMessage;

public interface DietaryProgrammeService {

    ResponseMessage addNewDietaryProgramme(DietaryProgrammeRequest dietaryProgrammeRequest);
}
