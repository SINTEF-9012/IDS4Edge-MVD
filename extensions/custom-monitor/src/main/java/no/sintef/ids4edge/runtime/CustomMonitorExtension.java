package no.sintef.ids4edge.runtime;
import org.eclipse.edc.runtime.metamodel.annotation.Provider;
import org.eclipse.edc.spi.monitor.CustomMonitor;
import org.eclipse.edc.spi.monitor.Monitor;
import org.eclipse.edc.spi.system.ServiceExtension;
import org.eclipse.edc.spi.system.ServiceExtensionContext;


public class CustomMonitorExtension implements ServiceExtension {

    private Monitor monitor;

    @Provider
    public Monitor provideMonitor() {
        monitor = new CustomMonitor();
        return monitor;
    }

    @Override
    public void initialize(ServiceExtensionContext context) {
        var monitor = context.getMonitor();
        monitor.debug("Custom monitor extension initialized");
    }
}
