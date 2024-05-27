package com.valdisdot.mycv.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.router.Route;
import com.valdisdot.mycv.storage.PageService;
import org.springframework.core.env.Environment;

@Route(value = "/print")
public class HrCVPage extends CVPage {
    public HrCVPage(PageService pageService, Environment environment) {
        super(pageService.getPage(environment.getProperty("mycv.page.print", Long.class)));
    }

    @Override
    protected Component findAnchor(String text) {
        return new Text(text);
    }
}
