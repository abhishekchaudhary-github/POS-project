package helper;

import com.increff.employee.pojo.*;

import java.time.LocalDateTime;

public class pojoHelper {
    public static BrandPojo makeBrandPojo(String brand, String category) {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand(brand);
        brandPojo.setCategory(category);
        return brandPojo;
    }

    public static ProductPojo makeProductPojo(String barcode, Integer brand_category, Double mrp, String name ) {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode(barcode);
        productPojo.setBrand_category(brand_category);
        productPojo.setMrp(mrp);
        productPojo.setName(name);
        return productPojo;
    }

    public static InventoryPojo makeInventoryPojo(Integer productId, Integer quantity ) {
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setProductId(productId);
        inventoryPojo.setQuantity(quantity);
        return inventoryPojo;
    }

    public static OrderItemPojo makeOrderItemPojo(Integer orderId, Integer productId, Integer quantity, Double sellingPrice ) {
        OrderItemPojo orderItemPojo = new OrderItemPojo();
        orderItemPojo.setOrderId(orderId);
        orderItemPojo.setProductId(productId);
        orderItemPojo.setQuantity(quantity);
        orderItemPojo.setSellingPrice(sellingPrice);
        return orderItemPojo;
    }

    public static OrderPojo makeOrderPojo(LocalDateTime time ) {
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setTime(time);
        return orderPojo;
    }

}
