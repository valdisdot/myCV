package com.valdisdot.mycv.storage.impl;

import com.valdisdot.mycv.entity.visitor.OfferRecord;
import com.valdisdot.mycv.entity.visitor.VisitRecord;
import com.valdisdot.mycv.storage.ServiceException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VisitorServiceImplTest {
    private final VisitorServiceImpl service = new VisitorServiceImpl(new VisitorDAOImpl(new DatabaseImpl().init("test.db")));

    @Test
    @Order(1)
    void createVisit() {
        VisitRecord visitRecord = new VisitRecord("app_test", null);
        assertDoesNotThrow(() -> service.createVisit(visitRecord));
    }

    @Test
    @Order(2)
    void createVisitWithOffer() {
        VisitRecord visitRecord = new VisitRecord("app_test", new OfferRecord("first name", "middle name", "last name", "company", "0962132654", "mail@mail.com", "an offer"));
        assertDoesNotThrow(() -> service.createVisit(visitRecord));
    }

    @Test
    @Order(3)
    void violateVisitOfferConstraints() {
        assertThrows(ServiceException.class, () -> service.createVisit(null));
        assertThrows(ServiceException.class, () -> service.createVisit(new VisitRecord(null, null)));
        assertThrows(ServiceException.class, () -> service.createVisit(new VisitRecord(null, null, "test", null)));
        assertThrows(ServiceException.class, () -> service.createVisit(new VisitRecord(null, LocalDateTime.now().plusDays(1).toEpochSecond(ZoneOffset.UTC), "test", null)));
    }

    @Test
    @Order(4)
    void getVisitsAfterYesterday() {
        service.createVisit(new VisitRecord("test", null));
        assertFalse(service.getVisitsAfter(LocalDateTime.now().minusDays(1)).isEmpty());
        assertThrows(ServiceException.class, () -> service.getVisitsAfter(LocalDateTime.now().plusDays(1)));
    }

    @Test
    @Order(5)
    void getVisitsWithOffers() {
        service.createVisit(new VisitRecord("test", new OfferRecord("first name", "middle name", "last name", "company", "0962132654", "mail@mail.com", "an offer")));
        assertFalse(service.getVisitsWithOffers().isEmpty());
    }

    @Test
    @Order(6)
    void getOffers() {
        service.createVisit(new VisitRecord("test", new OfferRecord("first name", "middle name", "last name", "company", "0962132654", "mail@mail.com", "an offer")));
        assertFalse(service.getOffers().isEmpty());
    }
}
