package com.valdisdot.mycv.entity;

import com.valdisdot.mycv.entity.page.ContentItemRecord;
import com.valdisdot.mycv.entity.page.ImageItemRecord;
import com.valdisdot.mycv.entity.visitor.OfferRecord;

import java.util.List;

public final class EntityUtil {
    private EntityUtil() {
    }

    public static OfferRecord getOfferLazy(Long id) {
        return id == null ? null : new OfferRecord(id, null, null, null, null, null, null, null, null);
    }

    public static ImageItemRecord getAvatarLazy(Long id) {
        return id == null ? null : new ImageItemRecord(id, null, null);
    }

    public static List<ContentItemRecord> getSingleItemLazyList(Long listId) {
        return listId == null ? List.of() : List.of(new ContentItemRecord(null, null, null, null, null, listId));
    }

    public static List<ImageItemRecord> getSingleItemImageLazyList(Long listId) {
        return listId == null ? List.of() : List.of(new ImageItemRecord(null, null, listId));
    }
}
