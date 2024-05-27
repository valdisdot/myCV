package com.valdisdot.mycv.view.component;

//package org.vaadin.stefan;
//source code from https://github.com/stefanuebe/vaadin-lazy-download-button/blob/master/addon/src/main/java/org/vaadin/stefan/LazyDownloadButton.java

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.dom.DomEvent;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.shared.Registration;

import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class LazyDownloadButton extends Button {
    private static final String DEFAULT_FILE_NAME = "download";
    private static final Supplier<String> DEFAULT_FILE_NAME_SUPPLIER = () -> DEFAULT_FILE_NAME;
    private Supplier<String> fileNameCallback;
    private InputStreamFactory inputStreamCallback;

    private Anchor anchor;

    public LazyDownloadButton() {
    }

    public LazyDownloadButton(String text) {
        super(text);
    }

    public LazyDownloadButton(Component icon) {
        super(icon);
    }

    public LazyDownloadButton(String text, InputStreamFactory inputStreamFactory) {
        this(text, DEFAULT_FILE_NAME_SUPPLIER, inputStreamFactory);
    }

    public LazyDownloadButton(Component icon, InputStreamFactory inputStreamFactory) {
        this(icon, DEFAULT_FILE_NAME_SUPPLIER, inputStreamFactory);
    }

    public LazyDownloadButton(String text, Component icon, InputStreamFactory inputStreamFactory) {
        this(text, icon, DEFAULT_FILE_NAME_SUPPLIER, inputStreamFactory);
    }

    public LazyDownloadButton(String text, Supplier<String> fileNameCallback, InputStreamFactory inputStreamFactory) {
        this(text, null, fileNameCallback, inputStreamFactory);
    }

    public LazyDownloadButton(Component icon, Supplier<String> fileNameCallback, InputStreamFactory inputStreamFactory) {
        this("", icon, fileNameCallback, inputStreamFactory);
    }

    public LazyDownloadButton(String text, Component icon, Supplier<String> fileNameCallback, InputStreamFactory inputStreamCallback) {
        super(text);

        this.fileNameCallback = fileNameCallback;
        this.inputStreamCallback = inputStreamCallback;

        if (icon != null) {
            setIcon(icon);
        }

        super.addClickListener(event -> {
            getParent().ifPresent(component -> {
                Objects.requireNonNull(fileNameCallback, "File name callback must not be null");
                Objects.requireNonNull(inputStreamCallback, "Input stream callback must not be null");

                if (anchor == null) {
                    anchor = new Anchor();
                    Element anchorElement = anchor.getElement();
                    anchorElement.setAttribute("download", true);
                    anchorElement.getStyle().set("display", "none");
                    component.getElement().appendChild(anchor.getElement());
                    anchorElement.addEventListener("click", event1 -> fireEvent(new DownloadStartsEvent(this, true, event1)));
                }

                Optional<UI> optionalUI = getUI();
                Executors.newSingleThreadExecutor().execute(() -> {
                    try {
                        InputStream inputStream = inputStreamCallback.createInputStream();

                        optionalUI.ifPresent(ui -> ui.access(() -> {
                            StreamResource href = new StreamResource(fileNameCallback.get(), () -> inputStream);
                            href.setCacheTime(0);
                            anchor.setHref(href);
                            anchor.getElement().callJsFunction("click");
                        }));

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            });
        });
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        if (anchor != null) {
            getParent().map(Component::getElement).ifPresent(parentElement -> {
                Element anchorElement = anchor.getElement();
                if (anchorElement != null && parentElement.getChildren().anyMatch(anchorElement::equals)) {
                    parentElement.removeChild(anchorElement);
                }
            });
        }
    }

    public Registration addDownloadStartsListener(ComponentEventListener<DownloadStartsEvent> listener) {
        return addListener(DownloadStartsEvent.class, listener);
    }

    public Supplier<String> getFileNameCallback() {
        return fileNameCallback;
    }

    public void setFileNameCallback(Supplier<String> fileNameCallback) {
        this.fileNameCallback = fileNameCallback;
    }

    public InputStreamFactory getInputStreamCallback() {
        return inputStreamCallback;
    }

    public void setInputStreamCallback(InputStreamFactory inputStreamCallback) {
        this.inputStreamCallback = inputStreamCallback;
    }

    public static class DownloadStartsEvent extends ComponentEvent<LazyDownloadButton> {

        private final DomEvent clientSideEvent;
        public DownloadStartsEvent(LazyDownloadButton source, boolean fromClient, DomEvent clientSideEvent) {
            super(source, fromClient);
            this.clientSideEvent = clientSideEvent;
        }

        public DomEvent getClientSideEvent() {
            return clientSideEvent;
        }
    }
}

