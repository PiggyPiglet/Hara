package ml.jammehcow;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;

import java.io.*;

/**
 * Author: jammehcow.
 * Date: 21/12/16.
 */

public class ConfigWrapper {
    //private final int CURRENT_REV = 1;

    public static Config getConfig() {
        @SuppressWarnings("unchecked")
        Config results = null;

        try {
            File cfgFile = new File((new File(System.getProperty("java.class.path"))).getAbsoluteFile().getParentFile() + File.separator +"config.yml");

            if (!cfgFile.exists()) {
                InputStream resource = Main.class.getClassLoader().getResourceAsStream("ml/jammehcow/config.yml");

                if (resource == null) {
                    try {
                        exportResource("/config.yml");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            YamlReader reader = new YamlReader(new FileReader("config.yml"));
            Config object = reader.read(Config.class);
            results = object;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (YamlException e) {
            e.printStackTrace();
        }

        return results;
    }

    static String exportResource(String resourceName) throws Exception {
        InputStream stream = null;
        OutputStream resStreamOut = null;
        String jarFolder;
        try {
            stream = Config.class.getResourceAsStream(resourceName);//note that each / is a directory down in the "jar tree" been the jar the root of the tree
            if(stream == null) {
                throw new Exception("Cannot get resource \"" + resourceName + "\" from Jar file.");
            }

            int readBytes;
            byte[] buffer = new byte[4096];
            jarFolder = new File(Config.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath().replace('\\', '/');
            resStreamOut = new FileOutputStream(jarFolder + resourceName);
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            stream.close();
            resStreamOut.close();
        }

        return jarFolder + resourceName;
    }
}
