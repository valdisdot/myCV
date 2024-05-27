package com.valdisdot.mycv.storage;

import com.valdisdot.mycv.entity.page.ContentItemRecord;
import com.valdisdot.mycv.entity.page.ImageItemRecord;
import com.valdisdot.mycv.entity.page.PageRecord;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PageDAO {
    Long createPage(PageRecord pageRecord);

    PageRecord getPageById(Long id);

    PageRecord getLastPage();


    Long createListItems(List<ContentItemRecord> contentItemRecords);

    Long getNextAvailableListId();

    List<ContentItemRecord> getListItemsByListId(Long listId);


    Long createAvatar(ImageItemRecord imageItemRecord);

    ImageItemRecord getAvatar(Long imageId);

    Long createImageItems(List<ImageItemRecord> imageItemRecords);

    Long getNextAvailableGalleryId();

    List<ImageItemRecord> getImageItemsByGalleryId(Long galleryId);
}
