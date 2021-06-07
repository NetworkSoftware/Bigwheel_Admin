package pro.network.bringwheeladmin.order;

public interface StatusListener {

    void onDeliveredClick(Order order, String id);
    void onWhatsAppClick(String phone);
    void onCallClick(String phone);
    void onCancelClick(Order order, String id);
    void onItemClick(Order order);

    void assignDboy(Order order);
}
