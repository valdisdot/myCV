package com.valdisdot.mycv.storage.impl;

import com.valdisdot.mycv.entity.EntityUtil;
import com.valdisdot.mycv.entity.page.PageRecord;
import com.valdisdot.mycv.storage.PageDAO;
import com.valdisdot.mycv.storage.PageService;
import com.valdisdot.mycv.storage.ServiceException;
import org.springframework.stereotype.Service;

@Service
public class PageServiceImpl implements PageService {
    private final PageDAO dao;
    private PageRecord lastPageRecord;

    public PageServiceImpl(PageDAO dao) {
        this.dao = dao;
    }

    @Override
    public PageRecord createPage(PageRecord pageRecord) {
        if (pageRecord == null) throw new ServiceException("PageRecord is null");
        pageRecord = new PageRecord(
                pageRecord.getAvatar() == null ? null : EntityUtil.getAvatarLazy(dao.createAvatar(pageRecord.getAvatar())),
                pageRecord.getName(),
                pageRecord.getQuote(),
                pageRecord.getTopContentTitle(),
                pageRecord.getTopContent(),
                pageRecord.getMainListTitle(),
                pageRecord.getMainList().isEmpty() ? null : EntityUtil.getSingleItemLazyList(dao.createListItems(pageRecord.getMainList())),
                pageRecord.getSubListTitle(),
                pageRecord.getSubList().isEmpty() ? null : EntityUtil.getSingleItemLazyList(dao.createListItems(pageRecord.getSubList())),
                pageRecord.getContactsListTitle(),
                pageRecord.getContactsList().isEmpty() ? null : EntityUtil.getSingleItemLazyList(dao.createListItems(pageRecord.getContactsList())),
                pageRecord.getMiniListTitle(),
                pageRecord.getMiniList().isEmpty() ? null : EntityUtil.getSingleItemLazyList(dao.createListItems(pageRecord.getMiniList())),
                pageRecord.getGallery().isEmpty() ? null : EntityUtil.getSingleItemImageLazyList(dao.createImageItems(pageRecord.getGallery())),
                pageRecord.getDog(),
                pageRecord.getExternalCVFileName(),
                pageRecord.getExternalCV()
        );
        dao.createPage(pageRecord);
        //the created pageRecord is the last pageRecord
        lastPageRecord = makePage(null);
        return lastPageRecord;
    }

    @Override
    public PageRecord getPage() {
        if (lastPageRecord == null) lastPageRecord = makePage(null);
        return lastPageRecord;
    }

    @Override
    public PageRecord getPage(Long pageId) {
        if (pageId == null) throw new ServiceException("PageRecord id is null");
        return makePage(pageId);
    }

    private PageRecord makePage(Long id) {
        PageRecord pageRecord = id == null ? dao.getLastPage() : dao.getPageById(id);
        if (pageRecord == null) throw new ServiceException("No pages in the storage");
        if (pageRecord.getAvatar() != null) pageRecord.setAvatar(dao.getAvatar(pageRecord.getAvatar().getId()));
        if (!pageRecord.getMainList().isEmpty())
            pageRecord.setMainList(dao.getListItemsByListId(pageRecord.getMainList().get(0).getListId()));
        if (!pageRecord.getSubList().isEmpty())
            pageRecord.setSubList(dao.getListItemsByListId(pageRecord.getSubList().get(0).getListId()));
        if (!pageRecord.getContactsList().isEmpty())
            pageRecord.setContactsList(dao.getListItemsByListId(pageRecord.getContactsList().get(0).getListId()));
        if (!pageRecord.getMiniList().isEmpty())
            pageRecord.setMiniList(dao.getListItemsByListId(pageRecord.getMiniList().get(0).getListId()));
        if (!pageRecord.getGallery().isEmpty())
            pageRecord.setGallery(dao.getImageItemsByGalleryId(pageRecord.getGallery().get(0).getListId()));
        return pageRecord;
    }
}
