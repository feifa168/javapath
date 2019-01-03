package com.path;


import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;

public class App {
    public static void main(String[] args) {
        App ap = new App();
        ap.testUrl();
    }

    @Test
    public void testPath() {
        System.out.println(getClass().toString());

        String name = "config.txt";

        System.out.println(getClass().getResource(name));
        System.out.println(getClass().getResource("./"+name));
        System.out.println(getClass().getResource("../../"+name));
        System.out.println(getClass().getResource("/"+name));

        System.out.println(getClass().getClassLoader().getResource(name));
        System.out.println(getClass().getClassLoader().getResource("./"+name));
        System.out.println(getClass().getClassLoader().getResource("../"+name));
        System.out.println(getClass().getClassLoader().getResource("/"+name));

        System.out.println(getClass().getClassLoader().getResource("../../"+name));
        System.out.println(getClass().getClassLoader().getResource("../../../"+name));
        System.out.println(getClass().getClassLoader().getResource("../../../../"+name));

        System.out.println(getClass().getResource("../../"+name));
        System.out.println(getClass().getResource("../../../"+name));
        System.out.println(getClass().getResource("../../../../"+name));
    }

    @Test
    public void testMain() {
        testUrl();
    }

    public void testUrl() {
//        testCustomUrl("config.txt", true);
//        testCustomUrl("config.txt", false);

        testCustomUrl("config/config.txt", true, true);
        testCustomUrl("config/config.txt", true, false);
        testCustomUrl("config/config.txt", false, false);
        //testCustomUrl("config/resourceconfig.txt");
    }

    private void testCustomUrl(String path, boolean isurl, boolean isClassLoader) {
        System.out.println("==========================================");
        System.out.println("class.getResource()                 = "+getClass().getResource(""));
        System.out.println("class.getResource(/)                = "+getClass().getResource("/"));
        System.out.println("class.getResource(./)                = "+getClass().getResource("./"));
        System.out.println("class.getResource(../)                = "+getClass().getResource("../"));
        System.out.println("class.getClassloader.getResource()  = "+getClass().getClassLoader().getResource(""));
        System.out.println("class.getClassloader.getResource(/) = "+getClass().getClassLoader().getResource("/"));
        System.out.println("==========================================\n");
        displayUrlResource2(path, isurl, isClassLoader);
        displayUrlResource2("./"+path, isurl, isClassLoader);
        displayUrlResource2("../"+path, isurl, isClassLoader);
        displayUrlResource2("../../"+path, isurl, isClassLoader);
        displayUrlResource2("../../../"+path, isurl, isClassLoader);
        displayUrlResource2("/"+path, isurl, isClassLoader);
    }

    public void displayUrlResource2(String path, boolean url, boolean isClassLoader) {
        System.out.println("======="+path+", isurl="+(url?"true":"false") + ", isclassloader="+(isClassLoader?"true":"false"));

        try {
            InputStream ism = null;
            if (url) {
                URL ul = null;
                if (isClassLoader) {
                    ul = getClass().getClassLoader().getResource(path);
                } else {
                    ul = getClass().getResource(path);
                }
                if (ul == null) {
                    System.out.println("url is null\n");
                    return;
                }
                System.out.println("URL is "+ul.getFile());

                ism = ul.openStream();
            } else {
                ism = new FileInputStream(path);
            }

            byte[] bts = new byte[128];
            int readlen = ism.read(bts);
            System.out.println(new String(bts, 0, readlen)+"\n");

        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            System.out.println(e.getMessage());
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void displayUrlResource(String path, boolean url) {
        System.out.println("======="+path+", "+(url?"true":"false"));
        try {
            URL ul = getClass().getClassLoader().getResource(path);
            if (ul == null) {
                System.out.println("url is null");
                return;
            }
            System.out.println("URL is "+ul.getFile());
            try {
                URI ui = ul.toURI();
                if (ui != null) {
                    System.out.println("URI is "+ui.toString());
                }
                Path pt = null;
                if (url) {
                    pt = Paths.get(ui);
                } else {
                    pt = Paths.get(path);
                }
                FileChannel fc = FileChannel.open(pt);
                ByteBuffer bf = ByteBuffer.allocate(256);
                boolean hasData = true;
                while (hasData) {
                    bf.clear();
                    while (bf.hasRemaining()) {
                        if (fc.read(bf) < 1) {
                            hasData = false;
                            break;
                        }
                    }
                    bf.flip();
                    System.out.println(new String(bf.array()));
                }
            } catch(FileSystemNotFoundException e) {
                e.printStackTrace();
            }
        } catch(NullPointerException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayByChannel(String path, boolean url, boolean isClassLoader) {
        System.out.println("======="+path+", isurl="+(url?"true":"false") + ", isclassloader="+(isClassLoader?"true":"false"));
        FileChannel fc = null;
        if (url) {
            URL ul = null;
            if (isClassLoader) {
                ul = getClass().getClassLoader().getResource(path);
            } else {
                ul = getClass().getResource(path);
            }
            if (ul != null) {
                try {
                    URI ui = ul.toURI();
                    Path pt = Paths.get(ui);
                    fc = FileChannel.open(pt, StandardOpenOption.READ);
                } catch (URISyntaxException e) {
                    //e.printStackTrace();
                    System.out.println(e.getMessage());
                } catch (IOException e) {
                    //e.printStackTrace();
                    System.out.println(e.getMessage());
                }
            }
        } else {
            try {
                Path pt = Paths.get(path);
                fc = FileChannel.open(pt, StandardOpenOption.READ);
            } catch (NoSuchFileException e) {
                //e.printStackTrace();
                System.out.println("no such file " + e.getMessage());
            } catch (IOException e) {
                //e.printStackTrace();
                System.out.println("error is " + e.getMessage());
            }
        }
        if (fc != null) {
            ByteBuffer buf = ByteBuffer.allocate(256);
            try {
                fc.read(buf);
                buf.flip();
                System.out.println(new String(buf.array()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testUrlByChannel() {
//        testCustomUrl("config.txt", true);
//        testCustomUrl("config.txt", false);

        testCustomUrlByChannel("config/config.txt", true, true);
        testCustomUrlByChannel("config/config.txt", true, false);
        testCustomUrlByChannel("config/config.txt", false, false);
        //testCustomUrl("config/resourceconfig.txt");
    }

    private void testCustomUrlByChannel(String path, boolean isurl, boolean isClassLoader) {
        System.out.println("==========================================");
        System.out.println("class.getResource()                 = "+getClass().getResource(""));
        System.out.println("class.getResource(/)                = "+getClass().getResource("/"));
        System.out.println("class.getResource(./)                = "+getClass().getResource("./"));
        System.out.println("class.getResource(../)                = "+getClass().getResource("../"));
        System.out.println("class.getClassloader.getResource()  = "+getClass().getClassLoader().getResource(""));
        System.out.println("class.getClassloader.getResource(/) = "+getClass().getClassLoader().getResource("/"));
        System.out.println("==========================================\n");
        displayByChannel(path, isurl, isClassLoader);
        displayByChannel("./"+path, isurl, isClassLoader);
        displayByChannel("../"+path, isurl, isClassLoader);
        displayByChannel("../../"+path, isurl, isClassLoader);
        displayByChannel("../../../"+path, isurl, isClassLoader);
        displayByChannel("/"+path, isurl, isClassLoader);
    }
}

