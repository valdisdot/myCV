package com.valdisdot.mycv.storage.impl;

import com.valdisdot.mycv.entity.page.ContentItemRecord;
import com.valdisdot.mycv.entity.page.ImageItemRecord;
import com.valdisdot.mycv.entity.page.PageRecord;
import com.valdisdot.mycv.storage.PageService;
import com.valdisdot.mycv.storage.ServiceException;
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
public class PageRecordServiceImplTest {
    private final PageService service = new PageServiceImpl(new PageDAOImpl(new DatabaseImpl().init("test.db")));

    @Test
    @Order(1)
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
                new ImageItemRecord(img),
                "the pageRecord name",
                "the quote",
                "the main",
                "lorem ipsum30",
                "experience",
                List.of(new ContentItemRecord("❤".getBytes(StandardCharsets.UTF_8), "a title", "a subtitle", "a content"), new ContentItemRecord("❤".getBytes(StandardCharsets.UTF_8), "a title", "a subtitle", "a content")),
                "education",
                List.of(new ContentItemRecord("❤".getBytes(StandardCharsets.UTF_8), "a title", "a subtitle", "a content"), new ContentItemRecord("❤".getBytes(StandardCharsets.UTF_8), "a title", "a subtitle", "a content")),
                "contact",
                List.of(new ContentItemRecord("❤".getBytes(StandardCharsets.UTF_8), "a title", "a subtitle", "a content"), new ContentItemRecord("❤".getBytes(StandardCharsets.UTF_8), "a title", "a subtitle", "a content")),
                "soft skills",
                List.of(new ContentItemRecord("❤".getBytes(StandardCharsets.UTF_8), "a title", "a subtitle", "a content"), new ContentItemRecord("❤".getBytes(StandardCharsets.UTF_8), "a title", "a subtitle", "a content")),
                List.of(new ImageItemRecord(img), new ImageItemRecord(img)),
                "2024"
        );
        assertDoesNotThrow(() -> service.createPage(pageRecord));
        PageRecord saved = service.createPage(pageRecord);
        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals(pageRecord.getName(), saved.getName());
    }

    @Test
    @Order(2)
    void violatePageConstraints() {
        assertThrows(ServiceException.class, () -> service.createPage(null));
        assertThrows(ServiceException.class, () -> service.getPage(null));
    }
}
