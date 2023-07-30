package pl.agh.edu.model.employee;

import pl.agh.edu.enums.TypeOfContract;

import java.math.BigDecimal;

public record PossibleEmployee(String firstName,
                               String lastName,
                               int age,
                               double skills,
                               BigDecimal acceptedWage,
                               BigDecimal desiredWage,
                               Shift desiredShift,
                               TypeOfContract desiredTypeOfContract,
                               Profession profession) {

    public JobOfferResponse offerJob(JobOffer jobOffer){
        //todo zapytac czy wywalic desiredTypeOfContract

        if(desiredShift == jobOffer.shift() && jobOffer.typeOfContract() == desiredTypeOfContract){
            if(jobOffer.offeredWage().doubleValue() >= acceptedWage.doubleValue()){
                return JobOfferResponse.POSITIVE;
            }
        }else if(desiredShift == jobOffer.shift() || jobOffer.typeOfContract() == desiredTypeOfContract){
            if(jobOffer.offeredWage().doubleValue() * 2 >= acceptedWage.doubleValue() + desiredWage.doubleValue()){
                return JobOfferResponse.POSITIVE;
            }
        }
        else {
            if(jobOffer.offeredWage().doubleValue() >= desiredWage.doubleValue()){
                return JobOfferResponse.POSITIVE;
            }
        }
        return JobOfferResponse.NEGATIVE;
    }
}
