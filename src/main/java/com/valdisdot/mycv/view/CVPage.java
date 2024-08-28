package com.valdisdot.mycv.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;
import com.valdisdot.mycv.entity.page.ContentItemRecord;
import com.valdisdot.mycv.entity.page.ImageItemRecord;
import com.valdisdot.mycv.entity.page.PageRecord;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public abstract class CVPage extends VerticalLayout{
    private final VerticalLayout leftSide;
    private final VerticalLayout mainSide;

    protected CVPage() {
        leftSide = new VerticalLayout();
        leftSide.addClassNames("left-side");
        leftSide.setSpacing(false);
        leftSide.setAlignItems(Alignment.CENTER);
        mainSide = new VerticalLayout();
        mainSide.addClassNames("main-side");
        mainSide.setSpacing(false);
        Div container = new Div();
        container.setClassName("content-container");
        setWidthFull();
        container.add(leftSide);
        container.add(mainSide);
        add(container);
        setAlignItems(Alignment.CENTER);
    }

    public CVPage(PageRecord pageRecord) {
        this();
        makeTopSection(pageRecord.getName(), pageRecord.getQuote(), pageRecord.getTopContentTitle(), pageRecord.getTopContent(), false).ifPresent(this::addToMainSide);
        makeMainListSection(pageRecord.getMainListTitle(), pageRecord.getMainList()).ifPresent(this::addToMainSide);
        makeSubListSection(pageRecord.getSubListTitle(), pageRecord.getSubList()).ifPresent(this::addToMainSide);
        makeAvatar(pageRecord.getAvatar()).ifPresent(this::addToLeftSide);
        makeTopSection(pageRecord.getName(), pageRecord.getQuote(), pageRecord.getTopContentTitle(), pageRecord.getTopContent(), true).ifPresent(this::addToLeftSide);
        makeContactsListSection(pageRecord.getContactsListTitle(), pageRecord.getContactsList()).ifPresent(this::addToLeftSide);
        makeMiniListSection(pageRecord.getMiniListTitle(), pageRecord.getMiniList()).ifPresent(this::addToLeftSide);
    }

    protected void addToLeftSide(Component... components) {
        leftSide.add(components);
    }

    protected void addToMainSide(Component... components) {
        mainSide.add(components);
    }

    protected Optional<Component> makeTopSection(String name, String quote, String topContentTitle, String topContent, boolean forMobile) {
        List<Component> components = new LinkedList<>();
        String cssClassPrefix = "top-section" + (forMobile ? "-mobile" : "");
        if (name != null && !name.isBlank()) {
            H2 nameElement = new H2(name);
            nameElement.setClassName(cssClassPrefix + "-name");
            components.add(nameElement);
        }
        if (topContentTitle != null && !topContentTitle.isBlank()) {
            H3 topContentTitleElement = new H3(topContentTitle);
            topContentTitleElement.setClassName(cssClassPrefix + "-content-title");
            components.add(topContentTitleElement);
        }
        if (quote != null && !quote.isBlank()) {
            Paragraph quoteElement = new Paragraph(quote);
            quoteElement.setClassName(cssClassPrefix + "-quote");
            components.add(quoteElement);
        }
        if (!forMobile && topContent != null && !topContent.isBlank()) {
            Paragraph topContentElement = new Paragraph(topContent);
            topContentElement.setClassName(cssClassPrefix + "-content");
            components.add(topContentElement);
        }
        if (components.isEmpty()) return Optional.empty();
        VerticalLayout topSection = new VerticalLayout();
        topSection.setClassName(cssClassPrefix);
        topSection.setSpacing(false);
        topSection.setAlignItems(FlexComponent.Alignment.START);
        components.forEach(topSection::add);
        return Optional.of(topSection);
    }

    protected Optional<Component> makeMainListSection(String title, Collection<ContentItemRecord> itemRecords) {
        return makeListSection(title, "main-list", itemRecords);
    }

    protected Optional<Component> makeSubListSection(String title, Collection<ContentItemRecord> itemRecords) {
        return makeListSection(title, "sub-list", itemRecords);
    }

    protected Optional<Component> makeAvatar(ImageItemRecord avatarImageItem) {
        if (avatarImageItem == null || avatarImageItem.getPhoto() == null || avatarImageItem.getPhoto().length == 0)
            return Optional.empty();
        String uuid = UUID.randomUUID().toString();
        Image avatar = new Image(new StreamResource(uuid, () -> new ByteArrayInputStream(avatarImageItem.getPhoto())), uuid);
        Div container = new Div(avatar);
        container.addClassName("avatar");
        return Optional.of(container);
    }

    protected Optional<Component> makeContactsListSection(String title, Collection<ContentItemRecord> itemRecords) {
        return makeListSection(title, "contacts-list", itemRecords);
    }

    protected Optional<Component> makeMiniListSection(String title, Collection<ContentItemRecord> itemRecords) {
        return makeListSection(title, "mini-list", itemRecords);
    }

    private Optional<Component> makeListSection(String title, String cssClassPrefix, Collection<ContentItemRecord> itemRecords) {
        List<Component> components = new LinkedList<>();
        if (title != null && !title.isBlank()) {
            H3 mainListTitle = new H3(title);
            mainListTitle.setClassName(cssClassPrefix + "-title");
            components.add(mainListTitle);
        }
        if (itemRecords != null && !itemRecords.isEmpty()) {
            Div itemsElement = new Div();
            itemsElement.setClassName(cssClassPrefix + "-items");
            for (ContentItemRecord item : itemRecords) {
                Div itemElement = new Div();

                itemElement.addClassName(cssClassPrefix + "-item");
                if (item.getIcon() != null && item.getIcon().length > 0) {
                    Span itemIconElement = new Span(new String(item.getIcon(), StandardCharsets.UTF_8));
                    itemIconElement.setClassName(cssClassPrefix + "-item-icon");
                    itemElement.add(itemIconElement, new Text(" "));
                }
                if (item.getTitle() != null && !item.getTitle().isBlank()) {
                    Span itemTitleElement = new Span(findAnchor(item.getTitle()));
                    itemTitleElement.setClassName(cssClassPrefix + "-item-title");
                    itemElement.add(itemTitleElement);
                }
                if (itemElement.getChildren().findAny().isPresent()) {
                    itemElement.add(new HtmlComponent("br"));
                }
                if (item.getSubtitle() != null && !item.getSubtitle().isBlank()) {
                    Paragraph subtitleElement = new Paragraph(item.getSubtitle());
                    subtitleElement.setClassName(cssClassPrefix + "-item-subtitle");
                    itemElement.add(subtitleElement);
                }
                if (item.getContent() != null && !item.getContent().isBlank()) {
                    Div contentsElement = new Div(findList(item.getContent()));
                    contentsElement.setClassName(cssClassPrefix + "-item-content");
                    itemElement.add(contentsElement);
                }
                itemsElement.add(itemElement);
            }
            components.add(itemsElement);
        }
        if (components.isEmpty()) return Optional.empty();
        VerticalLayout listContainer = new VerticalLayout();
        listContainer.setClassName(cssClassPrefix);
        components.forEach(listContainer::add);
        return Optional.of(listContainer);
    }

    private Component findList(String content) {
        String[] tokens = content.split("\n");
        Div list = new Div();
        for (String token : tokens) {
            list.add(new Paragraph(token));
        }
        list.setClassName("text-list-item");
        return list;

    }

    protected Component findAnchor(String text) {
        if (text.startsWith("+")) {
            try {
                Long.parseLong(text.substring(1));
                return new Anchor("tel:" + text, text);
            } catch (NumberFormatException e) {
                return new Text(text);
            }
        } else if (text.contains("@")) {
            if(text.startsWith("@")) {
                return new Anchor("https://t.me/" + text.substring(1), text, AnchorTarget.BLANK);
            }
            String[] tokens = text.split("@");
            if (tokens.length == 2) {
                if (tokens[1].contains(".")) return new Anchor("mailto:" + text, text);
            }
        } else if (text.replace("s", "").contains("http://")) {
            return new Anchor(text, text.substring(text.indexOf("//") + 2), AnchorTarget.BLANK);
        }
        return new Text(text);
    }
}