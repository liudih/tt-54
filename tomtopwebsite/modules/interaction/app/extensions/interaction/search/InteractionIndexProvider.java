package extensions.interaction.search;

import javax.inject.Inject;

import mapper.interaction.InteractionCommentMapper;
import play.libs.Json;
import valueobjects.search.ProductIndexingContext;

import com.fasterxml.jackson.databind.node.ObjectNode;

import extensions.search.ISearchIndexProvider;

public class InteractionIndexProvider implements ISearchIndexProvider {

	@Inject
	InteractionCommentMapper commentMapper;

	@Override
	public String partName() {
		return "interactions";
	}

	@Override
	public ObjectNode indexPart(ProductIndexingContext context) {
		ObjectNode obj = Json.newObject();
		int reviewCount = commentMapper.getCountByListingId(context
				.getListingId());
		obj.put("reviewCount", reviewCount);
		return obj;
	}

	@Override
	public void decorateMapping(ObjectNode mappings) {
		ObjectNode n = mappings.putObject("reviewCount");
		n.put("type", "long");
	}

}
