package no.sintef.ids4edge.context;

import no.sintef.ids4edge.context.spi.Ids4EdgeContext;
import org.eclipse.edc.runtime.metamodel.annotation.Inject;
import org.eclipse.edc.runtime.metamodel.annotation.Provider;
import org.eclipse.edc.spi.monitor.Monitor;
import org.eclipse.edc.spi.system.ServiceExtension;
import org.eclipse.edc.spi.system.ServiceExtensionContext;
//import org.eclipse.edc.spi.system.configuration.Config;

public class Ids4EdgeFileContextExtension implements ServiceExtension {

    private static final String FS_CONFIG = "ids4edge.fs.context";

    @Inject
    Monitor monitor;

    private Ids4EdgeContext ids4edgeContext;

    @Provider(isDefault = true)
    public Ids4EdgeContext provideIds4EdgeContext(ServiceExtensionContext context) {
        if (ids4edgeContext == null) {
            var config = context.getSetting(FS_CONFIG, "config/ids4edge.properties");
            ids4edgeContext = new Ids4EdgeFileContext(config, monitor);
        }
        /*var test = ids4edgeContext.getSupplierType("test");
        monitor.debug("Test supplier type: " + test);
        ids4edgeContext.setSupplierType("test", "black");
        test = ids4edgeContext.getSupplierType("test");
        monitor.debug("New test supplier type: " + test);*/

        return ids4edgeContext;
    }

    @Override
    public void initialize(ServiceExtensionContext context) {
        monitor.debug("IDS4EdgeFileContextExtension initialized");
    }
}
