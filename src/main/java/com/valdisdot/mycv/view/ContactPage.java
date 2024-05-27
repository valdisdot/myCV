package com.valdisdot.mycv.view;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;
import com.valdisdot.mycv.entity.visitor.OfferRecord;
import com.valdisdot.mycv.entity.visitor.VisitRecord;
import com.valdisdot.mycv.storage.VisitorService;

import java.util.logging.Level;
import java.util.logging.Logger;

@Route(value = "/contact", layout = BasicAppLayout.class)
public class ContactPage extends Composite<VerticalLayout> {
    private final TextField firstName = new TextField("First name", "Type your first name");
    private final TextField middleName = new TextField("Middle name", "Type your middle name");
    private final TextField lastName = new TextField("Last name", "Type your last name");
    private final TextField company = new TextField("Company", "Type the company you present");
    private final TextField phone = new TextField("Phone", "Type your phone");
    private final TextField email = new TextField("Email", "Type your email");
    private final TextArea offer = new TextArea("Offer", "Provide the offer");

    private final VisitorService visitorService;

    public ContactPage(VisitorService visitorService) {
        this.visitorService = visitorService;
        BeanValidationBinder<OfferRecord> offerBinder = new BeanValidationBinder<>(OfferRecord.class);
        offerBinder.bindInstanceFields(this);
        firstName.setClassName("form_field");
        middleName.setClassName("form_field");
        lastName.setClassName("form_field");
        company.setClassName("form_field");
        phone.setClassName("form_field");
        email.setClassName("form_field");
        offer.setClassName("form_field");


        Button sendButton = new Button("Send");
        sendButton.addClickListener(click -> {
            OfferRecord offerRecordEntity = new OfferRecord();
            try {
                if (offerBinder.isValid()) {
                    offerBinder.writeBean(offerRecordEntity);
                    visitorService.createVisit(new VisitRecord("offer", offerRecordEntity));
                } else Notification.show("Type at least first name, phone and the offer!");
            } catch (ValidationException e) {
                Logger.getLogger(this.getClassName()).log(Level.WARNING, e, e::getLocalizedMessage);
            }
        });
        sendButton.setClassName("send-offer-button");
        getContent().setSizeFull();
        getContent().setAlignItems(FlexComponent.Alignment.CENTER);
        VerticalLayout container = new VerticalLayout();
        container.setAlignItems(FlexComponent.Alignment.CENTER);
        container.add(
                new Paragraph("Thank you for your interest!"),
                firstName,
                middleName,
                lastName,
                company,
                phone,
                email,
                offer,
                sendButton
        );
        container.setClassName("form-section");
        getContent().add(container);
    }
}
