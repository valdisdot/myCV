package com.valdisdot.mycv.storage;

import com.valdisdot.mycv.entity.visitor.OfferRecord;
import com.valdisdot.mycv.entity.visitor.VisitRecord;

import java.util.List;

public interface VisitorDAO {
    //foe visitors
    Long createVisit(VisitRecord visitRecord);

    Long createOffer(OfferRecord offerRecord);

    //for user
    List<VisitRecord> getVisitsAfter(long timestamp);

    List<VisitRecord> getVisitsWithOffers();

    OfferRecord getOfferById(Long id);

    List<OfferRecord> getOffers();
}
