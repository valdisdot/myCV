package com.valdisdot.mycv.storage.impl;

import com.valdisdot.mycv.entity.EntityUtil;
import com.valdisdot.mycv.entity.visitor.OfferRecord;
import com.valdisdot.mycv.entity.visitor.VisitRecord;
import com.valdisdot.mycv.storage.ServiceException;
import com.valdisdot.mycv.storage.VisitorDAO;
import com.valdisdot.mycv.storage.VisitorService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class VisitorServiceImpl implements VisitorService {
    private final VisitorDAO dao;

    public VisitorServiceImpl(VisitorDAO dao) {
        this.dao = dao;
    }

    @Override
    public void createVisit(VisitRecord visitRecord) {
        if (visitRecord == null) throw new ServiceException("VisitRecord is null");
        if (visitRecord.getIdentifier() == null || visitRecord.getIdentifier().isBlank())
            throw new ServiceException("Visitor identifier is empty", visitRecord);
        if (visitRecord.getTimestamp() == null) throw new ServiceException("Timestamp is null", visitRecord);
        if (LocalDateTime.now().isBefore(LocalDateTime.ofEpochSecond(visitRecord.getTimestamp(), 0, ZoneOffset.UTC)))
            throw new ServiceException("Timestamp is after current time", visitRecord);
        if (visitRecord.hasOffer()) {
            OfferRecord offerRecord = EntityUtil.getOfferLazy(dao.createOffer(visitRecord.getOffer()));
            if (offerRecord == null) throw new ServiceException("Can't save an offerRecord", visitRecord);
            visitRecord.setOffer(offerRecord);
        }
        if (dao.createVisit(visitRecord) == null) throw new ServiceException("Can't save a visitRecord", visitRecord);
    }

    @Override
    public List<VisitRecord> getVisitsAfter(LocalDateTime localDateTime) {
        if (localDateTime == null) throw new ServiceException("DateTime is null");
        if (localDateTime.isAfter(LocalDateTime.now())) throw new ServiceException("DateTime is after current time");
        return dao.getVisitsAfter(localDateTime.toEpochSecond(ZoneOffset.UTC));
    }

    @Override
    public List<VisitRecord> getVisitsWithOffers() {
        return dao.getVisitsWithOffers();
    }

    @Override
    public List<OfferRecord> getOffers() {
        return dao.getOffers();
    }
}
