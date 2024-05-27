package com.valdisdot.mycv.entity.page;


import com.valdisdot.mycv.entity.Identifiable;

//for avatars and galleries
public class ImageItemRecord extends Identifiable {
    private final byte[] photo;
    private Long listId;


    public ImageItemRecord(Long id, byte[] photo, Long listId) {
        super(id);
        this.photo = photo;
        this.listId = listId;
    }

    public ImageItemRecord(Long id, byte[] photo) {
        super(id);
        this.photo = photo;
    }

    public ImageItemRecord(byte[] photo) {
        this(null, photo, null);
    }

    public byte[] getPhoto() {
        return photo;
    }

    public Long getListId() {
        return listId;
    }
}
