package rso.project.delivery.lib;

public class DeliveryTimeMetadata {
    private Integer deliveryId;
    private double deliveryDistanceKm;

    public Integer getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Integer deliveryId) {
        this.deliveryId = deliveryId;
    }

    public double getDeliveryDistanceKm() {
        return deliveryDistanceKm;
    }

    public void setDeliveryDistanceKm(double deliveryDistanceKm) {
        this.deliveryDistanceKm = deliveryDistanceKm;
    }
}
