package com.valdisdot.mycv.entity.visitor;

import com.valdisdot.mycv.entity.Identifiable;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

//for visit registration
public class VisitRecord extends Identifiable {
    private final Long timestamp;

    private final String identifier;

    private OfferRecord offerRecord;

    public VisitRecord(Long id, Long timestamp, String identifier, OfferRecord offerRecord) {
        super(id);
        this.timestamp = timestamp;
        this.identifier = identifier;
        this.offerRecord = offerRecord;
    }

    public VisitRecord(String identifier, OfferRecord offerRecord) {
        this(null, LocalDateTime.now().toEpochSecond(ZoneOffset.UTC), identifier, offerRecord);
    }

    public VisitRecord(String identifier) {
        this(identifier, null);
    }


    public Long getTimestamp() {
        return timestamp;
    }

    public String getIdentifier() {
        return identifier;
    }

    public OfferRecord getOffer() {
        return offerRecord;
    }

    public void setOffer(OfferRecord offerRecord) {
        this.offerRecord = offerRecord;
    }

    public boolean hasOffer() {
        return offerRecord != null;
    }
}
