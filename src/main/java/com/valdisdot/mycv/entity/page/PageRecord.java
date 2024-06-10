package com.valdisdot.mycv.entity.page;


import com.valdisdot.mycv.entity.Identifiable;

import java.util.LinkedList;
import java.util.List;

public class PageRecord extends Identifiable {
    private String name;

    private String quote;

    private String topContentTitle;

    private String topContent;

    private String mainListTitle;

    private List<ContentItemRecord> mainList;

    private String subListTitle;

    private List<ContentItemRecord> subList;

    private ImageItemRecord avatar;

    private String contactsListTitle;

    private List<ContentItemRecord> contactsList;

    private String miniListTitle;

    private List<ContentItemRecord> miniList;

    private List<ImageItemRecord> gallery;

    private String dog;

    private String externalCVFileName;
    private byte[] externalCV;

    public PageRecord(Long id, ImageItemRecord avatar, String name, String quote, String topContentTitle, String topContent, String mainListTitle, List<ContentItemRecord> mainList, String subListTitle, List<ContentItemRecord> subList, String contactsListTitle, List<ContentItemRecord> contactsList, String miniListTitle, List<ContentItemRecord> miniList, List<ImageItemRecord> gallery, String dog, String externalCVFileName, byte[] externalCV) {
        super(id);
        this.name = name;
        this.quote = quote;
        this.topContentTitle = topContentTitle;
        this.topContent = topContent;
        this.mainListTitle = mainListTitle;
        this.mainList = mainList == null ? List.of() : mainList;
        this.subListTitle = subListTitle;
        this.subList = subList == null ? List.of() : subList;
        this.avatar = avatar;
        this.contactsListTitle = contactsListTitle;
        this.contactsList = contactsList == null ? List.of() : contactsList;
        this.miniListTitle = miniListTitle;
        this.miniList = miniList == null ? List.of() : miniList;
        this.gallery = gallery == null ? List.of() : gallery;
        this.dog = dog;
        this.externalCVFileName = externalCVFileName;
        this.externalCV = externalCV;
    }

    public PageRecord(ImageItemRecord avatar, String name, String quote, String topContentTitle, String topContent, String mainListTitle, List<ContentItemRecord> mainList, String subListTitle, List<ContentItemRecord> subList, String contactsListTitle, List<ContentItemRecord> contactsList, String miniListTitle, List<ContentItemRecord> miniList, List<ImageItemRecord> gallery, String dog, String externalCVFileName, byte[] externalCV) {
        this(null, avatar, name, quote, topContentTitle, topContent, mainListTitle, mainList, subListTitle, subList, contactsListTitle, contactsList, miniListTitle, miniList, gallery, dog, externalCVFileName, externalCV);
    }

    protected PageRecord() {
        super(null);
        this.mainList = new LinkedList<>();
        this.subList = new LinkedList<>();
        this.contactsList = new LinkedList<>();
        this.miniList = new LinkedList<>();
        this.gallery = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public String getQuote() {
        return quote;
    }

    public String getTopContentTitle() {
        return topContentTitle;
    }

    public String getTopContent() {
        return topContent;
    }

    public String getMainListTitle() {
        return mainListTitle;
    }

    public List<ContentItemRecord> getMainList() {
        return mainList;
    }

    public void setMainList(List<ContentItemRecord> mainList) {
        this.mainList = mainList;
    }

    public String getSubListTitle() {
        return subListTitle;
    }

    public List<ContentItemRecord> getSubList() {
        return subList;
    }

    public void setSubList(List<ContentItemRecord> subList) {
        this.subList = subList;
    }

    public ImageItemRecord getAvatar() {
        return avatar;
    }

    public void setAvatar(ImageItemRecord avatar) {
        this.avatar = avatar;
    }

    public String getContactsListTitle() {
        return contactsListTitle;
    }

    public List<ContentItemRecord> getContactsList() {
        return contactsList;
    }

    public void setContactsList(List<ContentItemRecord> contactsList) {
        this.contactsList = contactsList;
    }

    public String getMiniListTitle() {
        return miniListTitle;
    }

    public List<ContentItemRecord> getMiniList() {
        return miniList;
    }

    public void setMiniList(List<ContentItemRecord> miniList) {
        this.miniList = miniList;
    }

    public List<ImageItemRecord> getGallery() {
        return gallery;
    }

    public void setGallery(List<ImageItemRecord> gallery) {
        this.gallery = gallery;
    }

    public String getDog() {
        return dog;
    }

    public byte[] getExternalCV() {
        return externalCV;
    }

    public void setExternalCV(byte[] externalCV) {
        this.externalCV = externalCV;
    }

    public String getExternalCVFileName() {
        return externalCVFileName;
    }

    public void setExternalCVFileName(String externalCVFileName) {
        this.externalCVFileName = externalCVFileName;
    }
}
