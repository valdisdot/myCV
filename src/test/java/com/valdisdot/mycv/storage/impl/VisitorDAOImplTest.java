package com.valdisdot.mycv.storage.impl;

import com.valdisdot.mycv.entity.EntityUtil;
import com.valdisdot.mycv.entity.visitor.OfferRecord;
import com.valdisdot.mycv.entity.visitor.VisitRecord;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VisitorDAOImplTest {
    private final VisitorDAOImpl dao = new VisitorDAOImpl(new DatabaseImpl().init("test.db"));

    @Test
    @Order(1)
    void createVisitWithoutOffer() {
        VisitRecord visitRecord = new VisitRecord("app_test", null);
        assertNotNull(dao.createVisit(visitRecord));
    }

    @Test
    @Order(2)
    void createOffer() {
        OfferRecord offerRecord = new OfferRecord("first", "middle", "last", "company", "0876598741", "mail@mail.com", "we have a job for you!");
        assertNotNull(dao.createOffer(offerRecord));
    }

    @Test
    @Order(3)
    void createVisitWithOffer() {
        OfferRecord offerRecord = new OfferRecord("first", "middle", "last", "company", "0999999999", "mail@mail.uk", "we have a job for you!");
        /*Lazy-loaded object*/
        offerRecord = EntityUtil.getOfferLazy(dao.createOffer(offerRecord));
        VisitRecord visitRecord = new VisitRecord("app_test", offerRecord);
        assertNotNull(dao.createVisit(visitRecord));
    }

    @Test
    @Order(4)
    void getVisitsByTime() {
        long after = LocalDateTime.now().minusDays(1).toEpochSecond(ZoneOffset.UTC);
        List<VisitRecord> res = dao.getVisitsAfter(after);
        assertFalse(res.isEmpty());
        after = LocalDateTime.now().plusDays(1).toEpochSecond(ZoneOffset.UTC);
        res = dao.getVisitsAfter(after);
        assertTrue(res.isEmpty());
    }

    @Test
    @Order(5)
    void getVisitsWithOffer() {
        assertFalse(dao.getVisitsWithOffers().isEmpty());
    }

    @Test
    @Order(6)
    void getOfferById() {
        OfferRecord offerRecord = dao.getOfferById(1L);
        assertNotNull(offerRecord);
        assertEquals(offerRecord.getId(), 1L);
    }

    @Test
    @Order(7)
    void getAllOffers() {
        List<OfferRecord> res = dao.getOffers();
        assertFalse(res.isEmpty());
        assertTrue(res.size() > 1);
    }
}
