package services.search;

public interface IOperatingIndex {

	public void createIndex(int siteId);

	public void dropIndex();

	public void indexAll(boolean dropIndex, boolean createIndex, Integer siteId);

	public void deleteAll(int siteId);

	public void deleteByListing(String listingId);

	public void index(String listingID);

	public void update(String listingID, String script);

}
