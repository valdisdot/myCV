package com.valdisdot.mycv.storage;

import com.valdisdot.mycv.entity.page.PageRecord;

import java.io.InputStream;

public interface PageService {
    PageRecord createPage(PageRecord pageRecord);

    PageRecord getPage();

    PageRecord getPage(Long pageId);

    InputStream getPageInputStream(Long pageId);
}
