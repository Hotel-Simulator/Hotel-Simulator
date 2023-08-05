package pl.agh.edu.model.employee;

import pl.agh.edu.enums.TypeOfContract;

import java.math.BigDecimal;

public record PossibleEmployee(String firstName,
                               String lastName,
                               int age,
                               double skills,
                               EmploymentPreferences preferences,
                               Profession profession) {

    public JobOfferResponse offerJob(JobOffer jobOffer){
        //todo zapytac czy wywalic desiredTypeOfContract

        if(preferences.desiredShift() == jobOffer.shift()
                && jobOffer.typeOfContract() == preferences.desiredTypeOfContract()){

            if(jobOffer.offeredWage().doubleValue() >= preferences.acceptableWage().doubleValue()){
                return JobOfferResponse.POSITIVE;
            }
        }else if(preferences.desiredShift() == jobOffer.shift()
                || jobOffer.typeOfContract() == preferences.desiredTypeOfContract()){

            if(jobOffer.offeredWage().doubleValue() * 2 >=
                    preferences.acceptableWage().doubleValue() + preferences.desiredWage().doubleValue()){
                return JobOfferResponse.POSITIVE;
            }
        }
        else {
            if(jobOffer.offeredWage().doubleValue() >= preferences.desiredWage().doubleValue()){
                return JobOfferResponse.POSITIVE;
            }
        }
        return JobOfferResponse.NEGATIVE;
    }
}
