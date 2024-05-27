package com.valdisdot.mycv.storage;

import com.valdisdot.mycv.entity.visitor.OfferRecord;
import com.valdisdot.mycv.entity.visitor.VisitRecord;

import java.time.LocalDateTime;
import java.util.List;

public interface VisitorService {
    void createVisit(VisitRecord visitRecord);

    List<VisitRecord> getVisitsAfter(LocalDateTime localDateTime);

    List<VisitRecord> getVisitsWithOffers();

    List<OfferRecord> getOffers();
}
