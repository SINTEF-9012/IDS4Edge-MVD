package no.sintef.ids4edge.runtime;
import org.eclipse.edc.runtime.metamodel.annotation.Provider;
import org.eclipse.edc.spi.system.ServiceExtension;


public class CustomMonitorExtension implements ServiceExtension {

    @Provider
    public CustomMonitor provideMonitor() {
        return new CustomMonitor();
    }
}
