package services.product.inventory;

public interface IInventoryEnquiryService {

	public abstract int getInventoryByListingID(String listingID);

	public abstract boolean checkInventory(String listingID, Integer qty);

}