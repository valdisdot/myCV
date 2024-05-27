package com.valdisdot.mycv.storage.impl;

import com.valdisdot.mycv.entity.EntityUtil;
import com.valdisdot.mycv.entity.visitor.OfferRecord;
import com.valdisdot.mycv.entity.visitor.VisitRecord;
import com.valdisdot.mycv.storage.Database;
import com.valdisdot.mycv.storage.VisitorDAO;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class VisitorDAOImpl implements VisitorDAO {
    private final Database database;

    public VisitorDAOImpl(Database database) {
        this.database = database;
    }

    @Override
    public Long createVisit(VisitRecord visitRecord) {
        String createVisit = visitRecord.getOffer() == null ? "INSERT INTO visit(visited_at, identifier) VALUES (?,?)" : "INSERT INTO visit(visited_at, identifier,offer_id) VALUES (?,?,?)";
        try (PreparedStatement statement = database.getConnection().prepareStatement(createVisit)) {
            statement.setLong(1, visitRecord.getTimestamp());
            statement.setString(2, visitRecord.getIdentifier());
            if (visitRecord.hasOffer()) statement.setLong(3, visitRecord.getOffer().getId());
            statement.executeUpdate();
            return database.getLastPrimaryKeyFor("visit");
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, e, e::getLocalizedMessage);
            return null;
        }
    }

    @Override
    public Long createOffer(OfferRecord offerRecord) {
        String createOffer = "INSERT INTO offer(first_name,middle_name,last_name,company,phone,email,offer,created_at) VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement statement = database.getConnection().prepareStatement(createOffer)) {
            statement.setString(1, offerRecord.getFirstName());
            statement.setString(2, offerRecord.getMiddleName());
            statement.setString(3, offerRecord.getLastName());
            statement.setString(4, offerRecord.getCompany());
            statement.setString(5, offerRecord.getPhone());
            statement.setString(6, offerRecord.getEmail());
            statement.setString(7, offerRecord.getOffer());
            statement.setLong(8, offerRecord.getCreatedAt());
            statement.executeUpdate();
            return database.getLastPrimaryKeyFor("offer");
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, e, e::getLocalizedMessage);
            return null;
        }
    }

    @Override
    public List<VisitRecord> getVisitsAfter(long timestamp) {
        try (PreparedStatement statement = database.getConnection().prepareStatement("SELECT id, visited_at, identifier, offer_id FROM visit WHERE visited_at > ?;")) {
            statement.setLong(1, timestamp);
            ResultSet rs = statement.executeQuery();
            List<VisitRecord> res = new ArrayList<>(rs.getFetchSize());
            while (rs.next()) {
                res.add(
                        new VisitRecord(
                                rs.getLong(1),
                                rs.getLong(2),
                                rs.getString(3),
                                EntityUtil.getOfferLazy(rs.getLong(4))
                        )
                );
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, e, e::getLocalizedMessage);
            return List.of();
        }
    }

    @Override
    public List<VisitRecord> getVisitsWithOffers() {
        try (Statement statement = database.getConnection().createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT id, visited_at, identifier, offer_id FROM visit WHERE offer_id IS NOT NULL;");
            List<VisitRecord> res = new ArrayList<>(rs.getFetchSize());
            while (rs.next()) {
                res.add(
                        new VisitRecord(
                                rs.getLong(1),
                                rs.getLong(2),
                                rs.getString(3),
                                EntityUtil.getOfferLazy(rs.getLong(4))
                        )
                );
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, e, e::getLocalizedMessage);
            return List.of();
        }
    }

    @Override
    public OfferRecord getOfferById(Long id) {
        try (PreparedStatement statement = database.getConnection().prepareStatement("SELECT id, first_name, middle_name, last_name, company, phone, email, offer.offer, created_at from offer WHERE id = ?")) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new OfferRecord(
                        rs.getLong(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getLong(9)
                );
            }
            return null;
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, e, e::getLocalizedMessage);
            return null;
        }
    }

    @Override
    public List<OfferRecord> getOffers() {
        try (Statement statement = database.getConnection().createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT id, first_name, middle_name, last_name, company, phone, email, offer.offer, created_at from offer;");
            List<OfferRecord> res = new ArrayList<>(rs.getFetchSize());
            while (rs.next()) {
                res.add(new OfferRecord(
                        rs.getLong(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getLong(9)
                ));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, e, e::getLocalizedMessage);
            return List.of();
        }
    }
}
