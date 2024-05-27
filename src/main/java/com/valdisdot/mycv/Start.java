package com.valdisdot.mycv;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.valdisdot.mycv.boot.CLIBuilder;
import com.valdisdot.mycv.boot.ResourcePreload;
import com.valdisdot.mycv.storage.impl.DatabaseImpl;
import com.valdisdot.mycv.storage.impl.PageDAOImpl;
import com.valdisdot.mycv.storage.impl.PageServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@Theme(value = "strict")
//figure out how to change it in the runtime
@Theme(value = "colorful")
@PWA(
        name = "CV Page",
        shortName = "cv",
        offlinePath = "pwa/page.html",
        iconPath = "pwa/icon.png",
        offlineResources = {"pwa/style.css", "pwa/script.js"}
)
public class Start implements AppShellConfigurator {
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("-b")) {
            new CLIBuilder(new PageServiceImpl(new PageDAOImpl(new DatabaseImpl().init("database.db")))).run();
            if (args.length > 1) {
                String[] _args = new String[args.length - 1];
                System.arraycopy(args, 1, _args, 0, args.length - 1);
                args = _args;
            } else args = new String[0];
        }
        new ResourcePreload().run();
        SpringApplication.run(Start.class, args);
    }
}
