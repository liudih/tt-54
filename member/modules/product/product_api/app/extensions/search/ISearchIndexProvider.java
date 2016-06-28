package extensions.search;

import valueobjects.search.ProductIndexingContext;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 定义扩展索引文件的字段：产品索引文件定义了一个可扩展字段，让其他模块扩展。 产品扩展字段为JSON格式:
 * 
 * <pre>
 * { extra: [ {partName1: object1}, {partName2: object2} ] }
 * </pre>
 * 
 * @author kmtong
 *
 */
public interface ISearchIndexProvider {

	String partName();

	ObjectNode indexPart(ProductIndexingContext context);

	void decorateMapping(ObjectNode mappings);
}
