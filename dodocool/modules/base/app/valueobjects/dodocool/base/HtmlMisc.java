package valueobjects.dodocool.base;

import java.util.LinkedList;
import java.util.List;

import play.Logger;
import play.api.mvc.Call;
import play.libs.F;
import play.twirl.api.Html;

import com.google.common.collect.Lists;

public class HtmlMisc {

	final List<Html> head = new LinkedList<Html>();
	final List<String> headString = new LinkedList<String>();
	boolean headRendered = false;

	final List<Html> tail = new LinkedList<Html>();
	final List<String> tailString = new LinkedList<String>();
	final List<F.Function0<Html>> tailFunc = new LinkedList<F.Function0<Html>>();

	public void addHead(Html html) {
		if (headRendered) {
			Logger.error("Cannot add to <head> which is already rendered: {}",
					html);
			throw new RuntimeException(
					"Cannot add to HEAD as it is already rendered");
		}
		head.add(html);
		headString.add(html.toString());
	}

	public void addTail(Html html) {
		tail.add(html);
		tailString.add(html.toString());
	}

	public void addHeadOnce(Html html) {
		if (!headString.contains(html.toString())) {
			addHead(html);
		}
	}

	public void addTailOnce(Html html) {
		if (!tailString.contains(html.toString())) {
			addTail(html);
		}
	}

	public void addTail(F.Function0<Html> html) {
		tailFunc.add(html);
	}

	public List<Html> getHead() {
		return getHead(true);
	}

	public List<Html> getHead(boolean render) {
		if (render) {
			this.headRendered = true;
		}
		return head;
	}

	public List<Html> getTail() {
		List<Html> l = Lists.newArrayList(tail);
		l.addAll(Lists.transform(tailFunc, (F.Function0<Html> f) -> {
			try {
				Html h = f.apply();
				Logger.trace("Tail: {}", h);
				return h;
			} catch (Throwable e) {
				Logger.error("Error Lazy Evaluation", e);
				return null;
			}
		}));
		return l;
	}

	/**
	 * 添加script 该方法的存在是为了解决添加脚本的真正唯一性
	 * addHeadOnce这个方法不能保证添加的唯一性，比如多个空格和少个空格，那么这两个String肯定不一样
	 * 
	 * @author lijun
	 * @param src
	 *            脚本的src
	 */
	public void addScriptOnce(String src) {
		if (!headString.contains(src)) {
			headString.add(src);
			Html html = Html.apply("<script type='text/javascript' src='" + src
					+ "'></script>");
			this.addHeadOnce(html);
		}
	}

	/**
	 * 添加script
	 * 
	 * @author lijun
	 * @param src
	 */
	public void addScriptOnce(Call src) {
		String srcStr = src.toString();
		this.addScriptOnce(srcStr);
	}

	public void addScriptOnce(Html src) {
		String srcStr = src.toString();
		this.addScriptOnce(srcStr);
	}

	/**
	 * 添加css
	 * 
	 * @author lijun
	 * @param src
	 *            css的src
	 */
	public void addCssOnce(String src) {
		if (!headString.contains(src)) {
			headString.add(src);
			Html html = Html.apply("<link href='" + src
					+ "' rel='stylesheet' type='text/css'/>");
			this.addHeadOnce(html);
		}
	}

	/**
	 * 添加css
	 * 
	 * @author lijun
	 * @param src
	 *            css的src
	 */
	public void addCssOnce(Call src) {
		String srcStr = src.toString();
		this.addCssOnce(srcStr);
	}

	public void addCssOnce(Html src) {
		String srcStr = src.toString();
		this.addCssOnce(srcStr);
	}
}
