package com.valdisdot.mycv.boot;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

//purpose: copy files from 'load' directory in the classpath to the root directory by java.path-pattern, files.media.my_pic.png -> ./files/media/my_pic.png
public class ResourcePreload implements Runnable {
    public static InputStream loadSCSS() {
        return new ByteArrayInputStream("{empty {}}".getBytes());
    }

    @Override
    public void run() {
        try {
            for (String resource : getResourceFiles("load")) {
                String[] path = resource.split("\\.");
                if (path.length == 1) new File(path[0]).mkdir();
                else if (path.length == 2) {
                    load(resource, new File(resource));
                } else {
                    StringBuilder builder = new StringBuilder(path[0]);
                    for (int i = 1; i < path.length - 2; ++i) builder.append("/").append(path[i]);
                    File dir = new File(builder.toString());
                    dir.mkdirs();
                    load(resource, new File(dir, path[path.length - 2] + "." + path[path.length - 1]));
                }
            }
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, e, e::getLocalizedMessage);
        }
    }

    private void load(String conventionalResourceFile, File to) throws IOException {
        if (!to.exists()) {
            try (InputStreamReader reader = new InputStreamReader(getResourceAsStream("load/" + conventionalResourceFile));
                 FileOutputStream writer = new FileOutputStream(to)) {
                while (reader.ready()) writer.write(reader.read());
                writer.flush();
            }

        }
    }

    private List<String> getResourceFiles(String path) throws IOException {
        List<String> filenames = new ArrayList<>();
        try (InputStream in = getResourceAsStream(path);
             BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String resource;
            while ((resource = br.readLine()) != null) filenames.add(resource);
        }
        return filenames;
    }

    private InputStream getResourceAsStream(String resource) {
        final InputStream in = this.getClass().getClassLoader().getResourceAsStream(resource);
        return in == null ? getClass().getResourceAsStream(resource) : in;
    }
}
