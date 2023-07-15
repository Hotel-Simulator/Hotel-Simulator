package pl.agh.edu.model.advertisement;

import java.time.LocalDate;

/*
web page
flyers
billboard
tv-ad
newspaper-ad
web-ad
 */


public interface Advertisement extends Comparable<Advertisement> {
    LocalDate getStartDate();
    LocalDate getEndDate();
    String getName();
    String getType();



    @Override
    default int compareTo(Advertisement o){
        return  getStartDate().compareTo(o.getStartDate());
    };
}
