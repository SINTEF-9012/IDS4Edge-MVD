package no.sintef.ids4edge.context;

import no.sintef.ids4edge.context.spi.Ids4EdgeContext;
import org.eclipse.edc.spi.EdcException;
import org.eclipse.edc.spi.monitor.Monitor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import static java.lang.String.format;

public class Ids4EdgeFileContext implements Ids4EdgeContext {

    private Monitor monitor;
    private String configPath;
    private Properties properties;

    public Ids4EdgeFileContext(String configPath, Monitor monitor) {
        this.configPath = configPath;
        this.monitor = monitor;
    }

    private void saveConfig() {
        var configLocation = this.configPath;
        var configPath = Paths.get(configLocation);

        if (properties == null) {
            monitor.warning("No configuration to save. Ignoring.");
            return;
        }

        try (var os = Files.newOutputStream(configPath)) {
            properties.store(os, "IDS4Edge configuration");
        } catch (IOException e) {
            throw new EdcException(e);
        }
    }


    private void loadConfig() {
        var configLocation = this.configPath;
        var configPath = Paths.get(configLocation);

        if (!Files.exists(configPath)) {
            monitor.warning(format("Configuration file does not exist: %s. Ignoring.", configLocation));
            return;
        }

        try (InputStream is = Files.newInputStream(configPath)) {

            if (properties == null) {
                properties = new Properties();
            }
            properties.load(is);

        } catch (IOException e) {
            throw new EdcException(e);
        }
    }

    @Override
    public String getSupplierType(String supplierId) {
        loadConfig();
        return properties.getProperty("supplier." + supplierId + ".type");
    }

    @Override
    public void setSupplierType(String supplierId, String type) {
        loadConfig();
        properties.setProperty("supplier." + supplierId + ".type", type);
        saveConfig();
    }
}
