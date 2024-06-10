package com.valdisdot.mycv.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WebBrowser;
import com.vaadin.flow.theme.lumo.LumoIcon;
import com.valdisdot.mycv.entity.page.PageRecord;
import com.valdisdot.mycv.view.component.LazyDownloadButton;
import com.valdisdot.mycv.entity.visitor.VisitRecord;
import com.valdisdot.mycv.storage.PageService;
import com.valdisdot.mycv.storage.VisitorService;
import org.springframework.core.env.Environment;

import java.io.ByteArrayInputStream;
import java.util.Optional;

public class BasicAppLayout extends AppLayout implements HasDynamicTitle {
    private final VisitorService service;
    //for fetch the dog-text for the footer
    private final PageService viewService;
    private final String title;

    public BasicAppLayout(VisitorService service, PageService viewService, Environment environment) {
        this.service = service;
        this.viewService = viewService;
        this.title = environment.getProperty("mycv.name", "myCV");

        UI.getCurrent().getPage().executeJs("window.addEventListener('load', () => {} );")
                .then((event) -> {
                    WebBrowser wb = VaadinSession.getCurrent().getBrowser();
                    this.service.createVisit(new VisitRecord(wb.getBrowserApplication() + ", " + wb.getAddress()));
                });
        DrawerToggle toggle = new DrawerToggle();
        toggle.setIcon(VaadinIcon.GRID_SMALL.create());

        Component statusElement = !environment.containsProperty("mycv.status") ? new Div(new Text("")) : new Paragraph(
                Optional.ofNullable(environment.getProperty("mycv.status")).filter(status -> !status.isBlank()).map(status -> "@" + status).orElse("")
        ) {
            //anon class
            {
                this.getElement().getStyle().set("background", "#6d77e3");
                this.getElement().getStyle().set("border-radius", "5px");
                this.getElement().getStyle().set("padding-left", "5px");
                this.getElement().getStyle().set("padding-right", "5px");
                this.getElement().getStyle().set("margin-left", "15px");
                this.getElement().getStyle().set("color", "#edeef5");
            }
        };

        H1 titleHeader = new H1(title);
        titleHeader.setClassName("navbar-item");

        addToDrawer(makeSideBar());
        PageRecord lastPage = viewService.getPage();
        Button downloadCVButton = new LazyDownloadButton(
                "Download CV",
                LumoIcon.DOWNLOAD.create(),
                lastPage.getExternalCVFileName() == null ?
                        () -> "cv.txt" : () -> lastPage.getExternalCVFileName(),
                lastPage.getExternalCV() == null ?
                        () -> new ByteArrayInputStream("to be continued ;)".getBytes()) : () -> new ByteArrayInputStream(lastPage.getExternalCV())
        );
        downloadCVButton.setClassName("cv-download-button");
        addToDrawer(new Div(downloadCVButton));
        if (environment.containsProperty("mycv.link.git")) {
            Button goGitHub = new Button(new Anchor(environment.getProperty("mycv.link.git"), "Go to GitRepository", AnchorTarget.BLANK));
            goGitHub.setClassName("cv-link-goto");
            addToDrawer(new Div(goGitHub));
        }
        setDrawerOpened(false);
        //@beta"
        addToNavbar(
                toggle,
                statusElement,
                titleHeader);
    }

    private SideNav makeSideBar() {
        SideNav sideNav = new SideNav();
        SideNavItem personalCVLink = new SideNavItem("Personal CV", PersonalCVPage.class, VaadinIcon.CHILD.create());
//        personalCVLink.addClassName("drawer-item");
        SideNavItem cvForHRLink = new SideNavItem("CV for HR", HrCVPage.class, VaadinIcon.BRIEFCASE.create());
//        cvForHRLink.addClassName("drawer-item");
        SideNavItem contactMe = new SideNavItem("Contact me", ContactPage.class, VaadinIcon.COMMENTS_O.create());
//        contactMe.addClassName("drawer-item");
        sideNav.addItem(
                personalCVLink,
                cvForHRLink,
                contactMe
        );
        return sideNav;
    }

    @Override
    public String getPageTitle() {
        return title;
    }
}
