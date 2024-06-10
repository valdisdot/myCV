package com.valdisdot.mycv.boot;


import com.valdisdot.mycv.entity.page.ContentItemRecord;
import com.valdisdot.mycv.entity.page.ImageItemRecord;
import com.valdisdot.mycv.entity.page.PageRecord;
import com.valdisdot.mycv.storage.PageService;
import com.valdisdot.mycv.storage.ServiceException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

@Component
public class CLIBuilder implements Runnable {
    private final PageService pageService;

    public CLIBuilder(PageService pageService) {
        this.pageService = pageService;
    }

    @Override
    public void run() {
        System.out.println(LocalDateTime.now() + " Preloading in the CV builder mode.");
        Scanner scanner = new Scanner(System.in);
        PageBuilder builder = new PageBuilder();
        System.out.print("Try to load the last page? (Y): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
            try {
                builder.pullValuesFrom(pageService.getPage());
                prebuild(scanner, builder);
                return;
            } catch (ServiceException e) {
                System.out.println("No page found.");
            }
        }
        System.out.print("Build a new one page with a build-master? (Y): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("y")) buildMaster(scanner, builder);
        else buildManually(scanner, builder, false);
    }

    private void prebuild(Scanner scanner, PageBuilder builder) {
        //print out builder
        System.out.println(builder);
        //offer buildManually
        System.out.print("Make a new page, based on the printed below? (Y): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("y")) buildManually(scanner, builder, false);
        else System.out.println("The printed below page will be used by the server.");
    }

    private void buildManually(Scanner scanner, PageBuilder builder, boolean printOut) {
        if (printOut) System.out.println(builder);
        //print setters menu
        String menu =
                "0\t- try to save the page to the database and continue\n" +
                        "1\t- set the name\n" +
                        "2\t- set the quote\n" +
                        "3\t- set the top description label\n" +
                        "4\t- set the top description content\n" +
                        "4a\t- append to the top description content\n" +
                        "5\t- set the top stack label\n" +
                        "6\t- fill the top stack\n" +
                        "7\t- refill the top stack\n" +
                        "8\t- set the education label\n" +
                        "9\t- fill the education list\n" +
                        "10\t- refill the education list\n" +
                        "11\t- set the avatar\n" +
                        "12\t- set the contact label\n" +
                        "13\t- fill the contact list\n" +
                        "14\t- refill the contact list\n" +
                        "15\t- set the soft skills label\n" +
                        "16\t- fill the soft skills list\n" +
                        "17\t- refill the soft skills list\n" +
                        "18\t- fill the gallery\n" +
                        "19\t- refill the gallery\n" +
                        "20\t- set the dog\n" +
                        "21\t- set the external CV\n" +
                        "p\t- print the pageRecord\n" +
                        "m\t- print the menu";
        System.out.println(menu);
        String temp = null;

        optionLabel:
        while (true) {
            System.out.print("Type an option: ");
            switch ((temp = scanner.nextLine().trim().toLowerCase())) {
                case "0": {
                    System.out.println("Trying to save the page.");
                    try {
                        PageRecord page = pageService.createPage(builder.build());
                        if (page != null) break optionLabel;
                        System.out.println("Page record is empty, something went wrong.");
                    } catch (ServiceException e) {
                        System.out.println("Can't save the page.");
                    }
                    break;
                }
                case "1": {
                    System.out.print("Type your name for CV ('-' to skip): ");
                    temp = scanner.nextLine();
                    if (!temp.trim().equals("-")) builder.name(temp);
                    break;
                }
                case "2": {
                    System.out.print("Type a quote (small italic text under the name, '-' to skip): ");
                    temp = scanner.nextLine();
                    if (!temp.trim().equals("-")) builder.quote(temp);
                    break;
                }
                case "3": {
                    System.out.print("Type a title for the top content (your most descriptive text, '-' to skip): ");
                    temp = scanner.nextLine();
                    if (!temp.trim().equals("-")) builder.topContentTitle(temp);
                    break;
                }
                case "4": {
                    System.out.print("Type the top content (your most descriptive text, '-' to skip): ");
                    temp = scanner.nextLine();
                    if (!temp.trim().equals("-")) builder.topContent(scanner.nextLine(), true);
                    break;
                }
                case "4a": {
                    do {
                        System.out.print("Append more text to the content? (Y): ");
                        temp = scanner.nextLine();
                        if (temp.equalsIgnoreCase("y")) {
                            System.out.println("Type a content to append: ");
                            builder.topContent(scanner.nextLine(), false);
                        } else temp = null;
                    } while (temp != null);
                    break;
                }

                case "5": {
                    System.out.print("Type a title for the main list (most significant list, for your experience etc., '-' to skip): ");
                    temp = scanner.nextLine();
                    if (!temp.trim().equals("-")) builder.mainListTitle(temp);
                    break;
                }
                case "6": {
                    builder.mainList(buildListItemMaster("main list", scanner), false);
                    break;
                }
                case "7": {
                    builder.mainList(buildListItemMaster("main list", scanner), true);
                    break;
                }
                case "8": {
                    System.out.print("Type a title for the sub list (the list under main list, for your education etc., '-' to skip): ");
                    temp = scanner.nextLine();
                    if (!temp.trim().equals("-")) builder.subListTitle(temp);
                    break;
                }
                case "9": {
                    builder.subList(buildListItemMaster("sub list", scanner), false);
                    break;
                }
                case "10": {
                    builder.subList(buildListItemMaster("sub list", scanner), true);
                    break;
                }
                case "11": {
                    System.out.print("Provide a path to the avatar: ");
                    builder.avatar(buildAvatar(scanner));
                    break;
                }
                case "12": {
                    System.out.print("Type a title for the your contacts list ('-' to skip): ");
                    temp = scanner.nextLine();
                    if (!temp.trim().equals("-")) builder.contactsListTitle(temp);
                    break;
                }
                case "13": {
                    builder.contactsList(buildListItemMaster("contacts", scanner), false);
                    break;
                }
                case "14": {
                    builder.contactsList(buildListItemMaster("contacts", scanner), true);
                    break;
                }
                case "15": {
                    System.out.print("Type a title for the mini list (the list under the contacts list, for your soft skills etc., '-' to skip): ");
                    temp = scanner.nextLine();
                    if (!temp.trim().equals("-")) builder.miniListTitle(temp);
                    break;
                }
                case "16": {
                    builder.miniList(buildListItemMaster("mini list", scanner), false);
                    break;
                }
                case "17": {
                    builder.miniList(buildListItemMaster("mini list", scanner), true);
                    break;
                }
                case "18": {
                    builder.gallery(buildImageItemMaster(scanner), false);
                    break;
                }
                case "19": {
                    builder.gallery(buildImageItemMaster(scanner), true);
                    break;
                }
                case "20": {
                    System.out.print("Type a dog (field for year or place etc., '-' to skip): ");
                    temp = scanner.nextLine();
                    if (!temp.trim().equals("-")) builder.dog(temp);
                    break;
                }
                case "21": {
                    loadExternalCV(builder, scanner);
                    break;
                }
                case "m": {
                    System.out.println(menu);
                    break;
                }
                case "p": {
                    System.out.println(builder);
                    break;
                }
                default: {
                    System.out.printf("'%s' is unknown option.", temp);
                }
            }
        }
    }

    private void buildMaster(Scanner scanner, PageBuilder builder) {
        String temp = null;
        System.out.print("Type your name for CV ('-' to skip): ");
        temp = scanner.nextLine();
        if (!temp.trim().equals("-")) builder.name(temp);

        System.out.print("Provide a path to the avatar: ");
        builder.avatar(buildAvatar(scanner));

        System.out.print("Type a quote (small italic text under the name, '-' to skip): ");
        temp = scanner.nextLine();
        if (!temp.trim().equals("-")) builder.quote(temp);

        System.out.print("Type a title for the top content (your most descriptive text, '-' to skip): ");
        temp = scanner.nextLine();
        if (!temp.trim().equals("-")) builder.topContentTitle(temp);

        System.out.print("Type the top content (your most descriptive text, '-' to skip): ");
        temp = scanner.nextLine();
        if (!temp.trim().equals("-")) builder.topContent(temp, true);

        do {
            System.out.print("Append more text to the content? (Y): ");
            temp = scanner.nextLine();
            if (temp.equalsIgnoreCase("y")) {
                System.out.println("Type a content to append: ");
                builder.topContent(scanner.nextLine(), false);
            } else temp = null;
        } while (temp != null);

        System.out.print("Type a title for the main list (most significant list, for your experience etc., '-' to skip): ");
        temp = scanner.nextLine();
        if (!temp.trim().equals("-")) builder.mainListTitle(temp);
        builder.mainList(buildListItemMaster("main list", scanner), false);

        System.out.print("Type a title for the sub list (the list under main list, for your education etc., '-' to skip): ");
        temp = scanner.nextLine();
        if (!temp.trim().equals("-")) builder.subListTitle(temp);
        builder.subList(buildListItemMaster("sub list", scanner), false);

        System.out.print("Type a title for the your contacts list ('-' to skip): ");
        temp = scanner.nextLine();
        if (!temp.trim().equals("-")) builder.contactsListTitle(temp);
        builder.contactsList(buildListItemMaster("contacts", scanner), false);

        System.out.print("Type a title for the mini list (the list under the contacts list, for your soft skills etc., '-' to skip): ");
        temp = scanner.nextLine();
        if (!temp.trim().equals("-")) builder.miniListTitle(temp);
        builder.miniList(buildListItemMaster("mini list", scanner), false);

        builder.gallery(buildImageItemMaster(scanner), false);

        System.out.print("Type a dog (field for year or place etc., '-' to skip): ");
        temp = scanner.nextLine();
        if (!temp.trim().equals("-")) builder.dog(temp);

        loadExternalCV(builder, scanner);

        buildManually(scanner, builder, true);
    }

    private List<ContentItemRecord> buildListItemMaster(String instance, Scanner scanner) {
        List<ContentItemRecord> contentItemRecords = new LinkedList<>();
        String ico = null;
        String title = null;
        String subtitle = null;
        String content = null;
        do {
            System.out.printf("Type an icon for the list item of %s (emoji before the title, '-' to skip): ", instance);
            ico = scanner.nextLine();
            if (ico.trim().equals("-")) ico = null;
            System.out.printf("Type a title for the list item of %s ('-' to skip): ", instance);
            title = scanner.nextLine();
            if (title.trim().equals("-")) title = null;
            System.out.printf("Type a subtitle for the list item of %s (a small text under the icon and the title, '-' to skip): ", instance);
            subtitle = scanner.nextLine();
            if (subtitle.trim().equals("-")) subtitle = null;
            System.out.printf("Type a content for the list item of %s (main text, '-' to skip): ", instance);
            content = scanner.nextLine();
            if (content.trim().equals("-")) content = null;
            else {
                String temp = null;
                do {
                    System.out.printf("Append more text to the content of %s? (Y): ", instance);
                    temp = scanner.nextLine();
                    if (temp.equalsIgnoreCase("y")) {
                        System.out.print("Type a content to append: ");
                        temp = scanner.nextLine();
                        content += "\n" + temp;
                    } else temp = null;
                } while (temp != null);
            }
            contentItemRecords.add(new ContentItemRecord(ico == null ? null : ico.getBytes(StandardCharsets.UTF_8), title, subtitle, content));
            System.out.printf("Add a new one item to the %s? (Y): ", instance);
        } while (scanner.nextLine().trim().equalsIgnoreCase("y"));
        return contentItemRecords;
    }

    private List<ImageItemRecord> buildImageItemMaster(Scanner scanner) {
        List<ImageItemRecord> listItems = new LinkedList<>();
        String path = null;
        do {
            System.out.print("Provide a path to a gallery image: ");
            path = scanner.nextLine();
            try (FileInputStream fileInputStream = new FileInputStream(path)) {
                listItems.add(new ImageItemRecord(fileInputStream.readAllBytes()));
            } catch (IOException e) {
                System.out.print("Can't read file: ");
                System.out.println(path);
            }
            System.out.print("Add a new one image to the gallery? (Y): ");
        } while (scanner.nextLine().trim().equalsIgnoreCase("y"));
        return listItems;
    }

    private ImageItemRecord buildAvatar(Scanner scanner) {
        String path = scanner.nextLine();
        try (FileInputStream fileInputStream = new FileInputStream(path)) {
            return new ImageItemRecord(fileInputStream.readAllBytes());
        } catch (IOException e) {
            System.out.print("Can't read the file: ");
            System.out.println(path);
            System.out.print("Try to add another one avatar? (Y): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("y")) return buildAvatar(scanner);
        }
        return null;
    }

    private void loadExternalCV(PageBuilder builder, Scanner scanner) {
        System.out.print("Provide a path to the CV file: ");
        File file = new File(scanner.nextLine());
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] fileByteArray = fileInputStream.readAllBytes();
            builder.externalFile(fileByteArray);
            builder.externalFileName(file.getName());
        } catch (IOException e) {
            System.out.print("Can't read the file: ");
            System.out.println(file.getAbsolutePath());
            System.out.print("Try to load another one CV file? (Y): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("y")) loadExternalCV(builder, scanner);
        }
    }

    public static class PageBuilder {
        private String name;
        private String quote;
        private ImageItemRecord avatar;
        private List<ContentItemRecord> mainList = new LinkedList<>();
        private String topContentTitle;
        private String topContent;
        private String subListTitle;
        private String mainListTitle;
        private List<ContentItemRecord> subList = new LinkedList<>();
        private String contactsListTitle;
        private List<ContentItemRecord> contactsList = new LinkedList<>();
        private String miniListTitle;
        private List<ContentItemRecord> miniList = new LinkedList<>();
        private List<ImageItemRecord> gallery = new LinkedList<>();
        private String dog;
        private String externalCVFileName;
        private byte[] externalCVFile;

        public PageBuilder pullValuesFrom(PageRecord pageRecord) {
            this.name = pageRecord.getName();
            this.quote = pageRecord.getQuote();
            this.avatar = pageRecord.getAvatar();
            this.mainList.addAll(pageRecord.getMainList() == null ? List.of() : pageRecord.getMainList());
            this.topContentTitle = pageRecord.getTopContentTitle();
            this.topContent = pageRecord.getTopContent();
            this.subListTitle = pageRecord.getSubListTitle();
            this.mainListTitle = pageRecord.getMainListTitle();
            this.subList.addAll(pageRecord.getSubList() == null ? List.of() : pageRecord.getSubList());
            this.contactsListTitle = pageRecord.getContactsListTitle();
            this.contactsList.addAll(pageRecord.getContactsList() == null ? List.of() : pageRecord.getContactsList());
            this.miniListTitle = pageRecord.getMiniListTitle();
            this.miniList.addAll(pageRecord.getMiniList() == null ? List.of() : pageRecord.getMiniList());
            this.gallery.addAll(pageRecord.getGallery() == null ? List.of() : pageRecord.getGallery());
            this.dog = pageRecord.getDog();
            this.externalCVFileName = pageRecord.getExternalCVFileName();
            this.externalCVFile = pageRecord.getExternalCV();
            return this;
        }

        public PageBuilder name(String name) {
            this.name = name;
            return this;
        }

        public PageBuilder quote(String quote) {
            this.quote = quote;
            return this;
        }

        public PageBuilder avatar(ImageItemRecord avatar) {
            this.avatar = avatar;
            return this;
        }

        public PageBuilder topContentTitle(String topContentTitle) {
            this.topContentTitle = topContentTitle;
            return this;
        }

        public PageBuilder topContent(String topContent, boolean doOverride) {
            if (this.topContent == null || doOverride) this.topContent = topContent;
            return this;
        }

        public PageBuilder mainListTitle(String mainListTitle) {
            this.mainListTitle = mainListTitle;
            return this;
        }

        public PageBuilder mainList(List<ContentItemRecord> items, boolean doOverride) {
            if (doOverride) this.mainList.clear();
            this.mainList.addAll(items);
            return this;
        }

        public PageBuilder subListTitle(String subListTitle) {
            this.subListTitle = subListTitle;
            return this;
        }

        public PageBuilder subList(List<ContentItemRecord> items, boolean doOverride) {
            if (doOverride) this.subList.clear();
            this.subList.addAll(items);
            return this;
        }

        public PageBuilder contactsListTitle(String contactsListTitle) {
            this.contactsListTitle = contactsListTitle;
            return this;
        }

        public PageBuilder contactsList(List<ContentItemRecord> items, boolean doOverride) {
            if (doOverride) this.contactsList.clear();
            this.contactsList.addAll(items);
            return this;
        }

        public PageBuilder miniListTitle(String miniListTitle) {
            this.miniListTitle = miniListTitle;
            return this;
        }

        public PageBuilder miniList(List<ContentItemRecord> items, boolean doOverride) {
            if (doOverride) this.miniList.clear();
            this.miniList.addAll(items);
            return this;
        }

        public PageBuilder gallery(List<ImageItemRecord> items, boolean doOverride) {
            if (doOverride) this.gallery.clear();
            this.gallery.addAll(items);
            return this;
        }

        public PageBuilder dog(String dog) {
            this.dog = dog;
            return this;
        }

        public PageBuilder externalFileName(String externalCVFileName){
            this.externalCVFileName = externalCVFileName;
            return this;
        }

        public PageBuilder externalFile(byte[] externalCVFile){
            this.externalCVFile = externalCVFile;
            return this;
        }

        public PageRecord build() {
            try {
                return new PageRecord(
                        avatar, name, quote, topContentTitle, topContent, mainListTitle, mainList, subListTitle, subList, contactsListTitle, contactsList, miniListTitle, miniList, gallery, dog, externalCVFileName, externalCVFile);
            } finally {
                name = null;
                quote = null;
                avatar = null;
                mainList = null;
                topContentTitle = null;
                topContent = null;
                subListTitle = null;
                mainListTitle = null;
                subList = null;
                contactsListTitle = null;
                contactsList = null;
                miniListTitle = null;
                miniList = null;
                gallery = null;
                dog = null;
                externalCVFileName = null;
                externalCVFile = null;
            }
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder
                    .append("the name: ")
                    .append(name)
                    .append("\nquote: ")
                    .append(quote)
                    .append("\navatar (bytes): ")
                    .append(avatar == null || avatar.getPhoto() == null ? 0 : avatar.getPhoto().length)
                    .append("\ntop description label: ")
                    .append(topContentTitle)
                    .append("\ntop description content: ")
                    .append(topContent)
                    .append("\ntop stack label: ")
                    .append(mainListTitle)
                    .append(mainList.isEmpty() ? "\ntop stack: empty" : "\ntop stack:");
            for (ContentItemRecord item : mainList)
                if (item != null)
                    builder
                            .append("\ncontent: ").append(item.getContent())
                            .append(", icon (bytes): ").append(item.getIcon().length);
            builder
                    .append("\neducation label: ")
                    .append(subListTitle)
                    .append(mainList.isEmpty() ? "\neducation list: empty" : "\neducation list:");
            for (ContentItemRecord item : subList)
                if (item != null)
                    builder
                            .append("\ncontent: ").append(item.getContent())
                            .append(", icon (bytes): ").append(item.getIcon().length);
            builder
                    .append("\ncontact label: ")
                    .append(contactsListTitle)
                    .append(mainList.isEmpty() ? "\ncontact list: empty" : "\ncontact list:");
            for (ContentItemRecord item : contactsList)
                if (item != null)
                    builder
                            .append("\ncontent: ").append(item.getContent())
                            .append(", icon (bytes): ").append(item.getIcon() == null ? 0 : item.getIcon().length);
            builder
                    .append("\nsoft skills label: ")
                    .append(miniListTitle)
                    .append(mainList.isEmpty() ? "\nsoft skill list: empty" : "\nsoft skill list:");
            for (ContentItemRecord item : miniList)
                if (item != null)
                    builder
                            .append("\ncontent: ").append(item.getContent())
                            .append(", icon (bytes): ").append(item.getIcon().length);
            builder.append(mainList.isEmpty() ? "\ngallery: empty" : "\ngallery:");
            for (ImageItemRecord item : gallery)
                if (item != null) builder.append("\nphoto (bytes): ").append(item.getPhoto().length);
            builder
                    .append("\ndog: ")
                    .append(dog)
                    .append("\nexternal CV file: ")
                    .append(externalCVFileName)
                    .append(", length: ")
                    .append(externalCVFile == null ? 0 : externalCVFile.length);
            return builder.toString();
        }
    }
}
