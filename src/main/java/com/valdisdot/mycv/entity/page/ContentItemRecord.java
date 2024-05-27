package com.valdisdot.mycv.entity.page;

import com.valdisdot.mycv.entity.Identifiable;

//for cards and lists
public class ContentItemRecord extends Identifiable {
    private final byte[] icon;
    private final String title;
    private final String subtitle;
    private final String content;
    private Long listId;

    public ContentItemRecord(Long id, byte[] icon, String title, String subtitle, String content, Long listId) {
        super(id);
        this.icon = icon;
        this.title = title;
        this.subtitle = subtitle;
        this.content = content;
        this.listId = listId;
    }

    public ContentItemRecord(byte[] icon, String title, String subtitle, String content) {
        super(null);
        this.icon = icon;
        this.title = title;
        this.subtitle = subtitle;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public byte[] getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public Long getListId() {
        return listId;
    }
}
