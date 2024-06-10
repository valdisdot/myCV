package com.valdisdot.mycv.storage.impl;

import com.valdisdot.mycv.entity.EntityUtil;
import com.valdisdot.mycv.entity.page.ContentItemRecord;
import com.valdisdot.mycv.entity.page.ImageItemRecord;
import com.valdisdot.mycv.entity.page.PageRecord;
import com.valdisdot.mycv.storage.Database;
import com.valdisdot.mycv.storage.PageDAO;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class PageDAOImpl implements PageDAO {
    private final Database database;

    public PageDAOImpl(Database database) {
        this.database = database;
    }

    @Override
    public Long createPage(PageRecord pageRecord) {
        try (PreparedStatement statement = database.getConnection().prepareStatement("INSERT INTO page(avatar_id,name,quote,top_content_title,top_content,main_list_title,main_list_id,sub_list_title,sub_list_id,contacts_list_title,contacts_list_id,mini_list_title,mini_list_id,gallery_list_id,dog,external_cv_name, external_cv) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)")) {
            if (pageRecord.getAvatar() == null) statement.setNull(1, Types.INTEGER);
            else statement.setLong(1, pageRecord.getAvatar().getId());
            statement.setString(2, pageRecord.getName());
            statement.setString(3, pageRecord.getQuote());
            statement.setString(4, pageRecord.getTopContentTitle());
            statement.setString(5, pageRecord.getTopContent());
            statement.setString(6, pageRecord.getMainListTitle());
            if (pageRecord.getMainList().isEmpty()) statement.setNull(7, Types.INTEGER);
            else statement.setLong(7, pageRecord.getMainList().get(0).getListId());
            statement.setString(8, pageRecord.getSubListTitle());
            if (pageRecord.getSubList().isEmpty()) statement.setNull(9, Types.INTEGER);
            else statement.setLong(9, pageRecord.getSubList().get(0).getListId());
            statement.setString(10, pageRecord.getContactsListTitle());
            if (pageRecord.getContactsList().isEmpty()) statement.setNull(11, Types.INTEGER);
            else statement.setLong(11, pageRecord.getContactsList().get(0).getListId());
            statement.setString(12, pageRecord.getMiniListTitle());
            if (pageRecord.getMiniList().isEmpty()) statement.setNull(13, Types.INTEGER);
            else statement.setLong(13, pageRecord.getMiniList().get(0).getListId());
            if (pageRecord.getGallery().isEmpty()) statement.setNull(14, Types.INTEGER);
            else statement.setLong(14, pageRecord.getGallery().get(0).getListId());
            statement.setString(15, pageRecord.getDog());
            statement.setString(16, pageRecord.getExternalCVFileName());
            statement.setBytes(17, pageRecord.getExternalCV());
            statement.executeUpdate();
            return database.getLastPrimaryKeyFor("page");
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, e, e::getLocalizedMessage);
            return null;
        }
    }

    @Override
    public PageRecord getPageById(Long id) {
        try (PreparedStatement statement = database.getConnection().prepareStatement("SELECT id,avatar_id,name,quote,top_content_title,top_content,main_list_title,main_list_id,sub_list_title,sub_list_id,contacts_list_title,contacts_list_id,mini_list_title,mini_list_id,gallery_list_id,dog,external_cv_name,external_cv FROM page WHERE id = ?;")) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new PageRecord(
                        rs.getLong(1),
                        EntityUtil.getAvatarLazy(rs.getLong(2)),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7),
                        EntityUtil.getSingleItemLazyList(rs.getLong(8)),
                        rs.getString(9),
                        EntityUtil.getSingleItemLazyList(rs.getLong(10)),
                        rs.getString(11),
                        EntityUtil.getSingleItemLazyList(rs.getLong(12)),
                        rs.getString(13),
                        EntityUtil.getSingleItemLazyList(rs.getLong(14)),
                        EntityUtil.getSingleItemImageLazyList(rs.getLong(15)),
                        rs.getString(16),
                        rs.getString(17),
                        rs.getBytes(18)
                );
            }
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, e, e::getLocalizedMessage);
        }
        return null;
    }

    @Override
    public PageRecord getLastPage() {
        try {
            return getPageById(database.getLastPrimaryKeyFor("page"));
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, e, e::getLocalizedMessage);
            return null;
        }
    }

    public Long createListItems(List<ContentItemRecord> items) {
        StringBuilder builder = new StringBuilder("INSERT INTO content_item(content,icon,list_id,title,subtitle) VALUES ");
        for (int i = 0; i < items.size() - 1; ++i) builder.append("(?,?,?,?,?),");
        builder.append("(?,?,?,?,?);");

        Long nextAvailableListId = getNextAvailableListId();
        try (PreparedStatement statement = database.getConnection().prepareStatement(builder.toString())) {
            for (int paramI = 0; paramI < items.size() * 5; paramI += 5) {
                statement.setString(1 + paramI, items.get(paramI / 5).getContent());
                statement.setBytes(2 + paramI, items.get(paramI / 5).getIcon());
                statement.setLong(3 + paramI, nextAvailableListId);
                statement.setString(4 + paramI, items.get(paramI / 5).getTitle());
                statement.setString(5 + paramI, items.get(paramI / 5).getSubtitle());
            }
            statement.executeUpdate();
            return nextAvailableListId;
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, e, e::getLocalizedMessage);
            return null;
        }
    }

    @Override
    public Long getNextAvailableListId() {
        try (Statement statement = database.getConnection().createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT IIF(MAX(list_id) IS NULL, 0, MAX(list_id))+1 FROM content_item");
            if (rs.next()) return rs.getLong(1);
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, e, e::getLocalizedMessage);
        }
        return null;
    }

    @Override
    public List<ContentItemRecord> getListItemsByListId(Long listId) {
        try (PreparedStatement statement = database.getConnection().prepareStatement("SELECT id,icon,title,subtitle,content FROM content_item WHERE list_id = ?")) {
            statement.setLong(1, listId);
            ResultSet rs = statement.executeQuery();
            List<ContentItemRecord> res = new ArrayList<>(rs.getFetchSize());
            while (rs.next()) {
                res.add(
                        new ContentItemRecord(
                                rs.getLong(1),
                                rs.getBytes(2),
                                rs.getString(3),
                                rs.getString(4),
                                rs.getString(5),
                                listId
                        )
                );
            }
            return res;
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, e, e::getLocalizedMessage);
            return List.of();
        }
    }

    @Override
    public Long createAvatar(ImageItemRecord imageItemRecord) {
        try (PreparedStatement statement = database.getConnection().prepareStatement("INSERT INTO image_item(image) VALUES (?);")) {
            statement.setBytes(1, imageItemRecord.getPhoto());
            statement.executeUpdate();
            return database.getLastPrimaryKeyFor("image_item");
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, e, e::getLocalizedMessage);
            return null;
        }
    }

    @Override
    public ImageItemRecord getAvatar(Long imageId) {
        try (PreparedStatement statement = database.getConnection().prepareStatement("SELECT image FROM image_item WHERE id = ?")) {
            statement.setLong(1, imageId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new ImageItemRecord(
                        imageId,
                        rs.getBytes(1)
                );
            }
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, e, e::getLocalizedMessage);
        }
        return null;
    }

    @Override
    public Long createImageItems(List<ImageItemRecord> items) {
        StringBuilder builder = new StringBuilder("INSERT INTO image_item(image,list_id) VALUES ");
        for (int i = 0; i < items.size() - 1; ++i) builder.append("(?,?),");
        builder.append("(?,?);");

        Long nextAvailableGalleryId = getNextAvailableGalleryId();
        try (PreparedStatement statement = database.getConnection().prepareStatement(builder.toString())) {
            for (int paramI = 0; paramI < items.size() * 2; paramI += 2) {
                statement.setBytes(1 + paramI, items.get(paramI / 2).getPhoto());
                statement.setLong(2 + paramI, nextAvailableGalleryId);
            }
            statement.executeUpdate();
            return nextAvailableGalleryId;
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, e, e::getLocalizedMessage);
            return null;
        }
    }

    @Override
    public Long getNextAvailableGalleryId() {
        try (Statement statement = database.getConnection().createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT IIF(MAX(list_id) IS NULL, 0, MAX(list_id))+1 FROM image_item");
            if (rs.next()) return rs.getLong(1);
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, e, e::getLocalizedMessage);
        }
        return null;
    }

    @Override
    public List<ImageItemRecord> getImageItemsByGalleryId(Long galleryId) {
        try (PreparedStatement statement = database.getConnection().prepareStatement("SELECT id,image FROM image_item WHERE list_id = ?")) {
            statement.setLong(1, galleryId);
            ResultSet rs = statement.executeQuery();
            List<ImageItemRecord> res = new ArrayList<>(rs.getFetchSize());
            while (rs.next()) {
                res.add(
                        new ImageItemRecord(
                                rs.getLong(1),
                                rs.getBytes(2),
                                galleryId
                        )
                );
            }
            return res;
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, e, e::getLocalizedMessage);
            return List.of();
        }
    }
}
