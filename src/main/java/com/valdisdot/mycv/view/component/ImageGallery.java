package com.valdisdot.mycv.view.component;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.LumoIcon;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.UUID;

public class ImageGallery extends Composite<VerticalLayout> {
    private final Deque<Image> images;
    private Image current;

    public ImageGallery(Collection<byte[]> images) {
        this.images = new LinkedList<>(
                images.stream().map(array -> {
                    String alt = UUID.randomUUID().toString();
                    Image image = new Image(new StreamResource(alt, () -> new ByteArrayInputStream(array)), alt);
                    image.setMaxWidth(100, Unit.PERCENTAGE);
                    image.setWidth(100, Unit.PERCENTAGE);
                    return image;
                }).toList());
        if (!images.isEmpty()) {
            getContent().setAlignItems(FlexComponent.Alignment.CENTER);
            current = popRightImage();
            getContent().add(new Span(
                        makeNavButton(LumoIcon.CHEVRON_LEFT.create(), click -> {
                            Image image = popLeftImage();
                            if (image.equals(current)) image = popLeftImage();
                            getContent().replace(current, image);
                            current = image;
                        }),
                        makeNavButton(LumoIcon.CHEVRON_RIGHT.create(), click -> {
                            Image image = popRightImage();
                            if (image.equals(current)) image = popRightImage();
                            getContent().replace(current, image);
                            current = image;
                        })
            ), current);
        }
        getContent().setSpacing(false);
        getContent().setClassName("gallery");
    }

    private Button makeNavButton(Icon icon, ComponentEventListener<ClickEvent<Button>> onClick) {
        Button button = new Button(icon);
        button.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        button.addClickListener(onClick);
        return button;
    }

    private Image popRightImage() {
        Image image = images.removeFirst();
        images.addLast(image);
        return image;
    }

    private Image popLeftImage() {
        Image image = images.removeLast();
        images.addFirst(image);
        return image;
    }
}
