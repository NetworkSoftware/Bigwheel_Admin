package pro.network.bringwheeladmin.deliveryboy;

public interface OnDeliveryBoy {
    void onStatusClick(int position,String status);

    void onEditClick(DeliveryBean deliveryBean);

    void onDeleteClick(int position);
}
