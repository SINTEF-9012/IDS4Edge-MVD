package no.sintef.ids4edge.context.spi;

public interface Ids4EdgeContext {

    String getSupplierType(String supplierId);

    void setSupplierType(String supplierId, String type);
}
