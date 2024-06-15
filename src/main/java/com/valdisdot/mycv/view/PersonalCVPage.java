package com.valdisdot.mycv.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.Route;
import com.valdisdot.mycv.view.component.ImageGallery;
import com.valdisdot.mycv.entity.page.ImageItemRecord;
import com.valdisdot.mycv.storage.PageService;

import java.util.Collection;
import java.util.Optional;

@Route(value = "/cv", layout = BasicAppLayout.class)
public class PersonalCVPage extends CVPage {
    public PersonalCVPage(PageService pageService) {
        super(pageService.getPage());
        makeGallery(pageService.getPage().getGallery()).ifPresent(this::addToLeftSide);
    }

    private Optional<Component> makeGallery(Collection<ImageItemRecord> imageItemRecords) {
        return imageItemRecords == null || imageItemRecords.isEmpty() ? Optional.empty() : Optional.of(new ImageGallery(imageItemRecords.stream().map(ImageItemRecord::getPhoto).toList()));
    }
}
