package agh.edu.pl.diet.services;

import agh.edu.pl.diet.entities.DailyMenu;
import agh.edu.pl.diet.payloads.request.DailyMenuRequest;
import agh.edu.pl.diet.payloads.response.ResponseMessage;

public interface DailyMenuService {

    DailyMenu getDailyMenu(Long dailyMenuId);

    ResponseMessage addNewDailyMenu(DailyMenuRequest dailyMenuRequest);

    ResponseMessage updateDailyMenu(Long dailyMenuId, DailyMenuRequest dailyMenuRequest);

    ResponseMessage removeDailyMenu(Long dailyMenuId);

}
