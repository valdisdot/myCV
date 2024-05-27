package com.valdisdot.mycv.storage.impl;

import com.valdisdot.mycv.entity.EntityUtil;
import com.valdisdot.mycv.entity.page.ContentItemRecord;
import com.valdisdot.mycv.entity.page.ImageItemRecord;
import com.valdisdot.mycv.entity.page.PageRecord;
import com.valdisdot.mycv.storage.PageDAO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PageRecordDAOImplTest {
    private final PageDAO dao = new PageDAOImpl(new DatabaseImpl().init("test.db"));

    @Test
    @Order(1)
    void createListItems() {
        Long listId = dao.createListItems(List.of(new ContentItemRecord("❤".getBytes(StandardCharsets.UTF_8), "a title", "a subtitle", "a content"), new ContentItemRecord("❤".getBytes(StandardCharsets.UTF_8), "a title", "a subtitle", "a content"), new ContentItemRecord("❤".getBytes(StandardCharsets.UTF_8), "a title", "a subtitle", "a content")));
        assertNotNull(listId);
    }

    @Test
    @Order(2)
    void getNextListId() {
        assertTrue(1 < dao.getNextAvailableListId());
    }

    @Test
    @Order(3)
    void createImageItems() {
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("test_image.png")) {
            byte[] img = inputStream.readAllBytes();
            Long galleryId = dao.createImageItems(List.of(new ImageItemRecord(img), new ImageItemRecord(img), new ImageItemRecord(img)));
            assertNotNull(galleryId);
        } catch (IOException e) {
            assertDoesNotThrow(() -> {
                throw e;
            });
        }
    }

    @Test
    @Order(4)
    void getNextGalleryId() {
        assertTrue(1 < dao.getNextAvailableGalleryId());
    }

    @Test
    @Order(5)
    void getListItemsByListId() {
        assertFalse(dao.getListItemsByListId(1L).isEmpty());
    }

    @Test
    @Order(6)
    void getImageItemsByGalleryId() {
        assertFalse(dao.getImageItemsByGalleryId(1L).isEmpty());
    }

    @Test
    @Order(7)
    void createPage() {
        byte[] img = {0};
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("test_image.png")) {
            img = inputStream.readAllBytes();
        } catch (IOException e) {
            assertDoesNotThrow(() -> {
                throw e;
            });
        }

        PageRecord pageRecord = new PageRecord(
                EntityUtil.getAvatarLazy(dao.createAvatar(new ImageItemRecord(img))),
                "the pageRecord name",
                "the quote",
                "the main",
                "lorem ipsum30",
                "experience",
                EntityUtil.getSingleItemLazyList(dao.createListItems(List.of(new ContentItemRecord("❤".getBytes(StandardCharsets.UTF_8), "a title", "a subtitle", "a content"), new ContentItemRecord("❤".getBytes(StandardCharsets.UTF_8), "a title", "a subtitle", "a content")))),
                "education",
                EntityUtil.getSingleItemLazyList(dao.createListItems(List.of(new ContentItemRecord("❤".getBytes(StandardCharsets.UTF_8), "a title", "a subtitle", "a content"), new ContentItemRecord("❤".getBytes(StandardCharsets.UTF_8), "a title", "a subtitle", "a content")))),
                "contact",
                EntityUtil.getSingleItemLazyList(dao.createListItems(List.of(new ContentItemRecord("❤".getBytes(StandardCharsets.UTF_8), "a title", "a subtitle", "a content"), new ContentItemRecord("❤".getBytes(StandardCharsets.UTF_8), "a title", "a subtitle", "a content")))),
                "soft skills",
                EntityUtil.getSingleItemLazyList(dao.createListItems(List.of(new ContentItemRecord("❤".getBytes(StandardCharsets.UTF_8), "a title", "a subtitle", "a content"), new ContentItemRecord("❤".getBytes(StandardCharsets.UTF_8), "a title", "a subtitle", "a content")))),
                EntityUtil.getSingleItemImageLazyList(dao.createImageItems(List.of(new ImageItemRecord(img), new ImageItemRecord(img)))),
                "2024"
        );
        assertNotNull(dao.createPage(pageRecord));
    }

    @Test
    @Order(8)
    void getPageById() {
        assertNotNull(dao.getPageById(1L));
    }

    @Test
    @Order(9)
    void getLastPage() {
        assertNotNull(dao.getLastPage());
    }
}
