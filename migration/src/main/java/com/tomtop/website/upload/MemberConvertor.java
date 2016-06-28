package com.tomtop.website.upload;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.website.dto.member.Member;
import com.website.dto.member.MemberAddress;

public class MemberConvertor {

	final List<String> notsaveEmails = Arrays.asList(new String[] {
			"894266014@qq.com", "admin@catchatt.com", "alanne6576@mail.com",
			"alarin-taylor@ec.rr.com", "alikasal41@gmail.com",
			"at3best@gmail.com", "at3bestz@gmail.com", "auction@tomtop.com",
			"benny9101@hotmail.co.uk", "cjhaseler@gmail.com",
			"claualejos@gmail.com", "com@tomtop.com", "conniejovial@gmail.com",
			"dangjr213@gmail.com", "daniel.rezig@gmail.com", "dmtet@mail.ru",
			"ed4253@gmail.com", "grandgk@gmail.com", "gundown64@aol.com",
			"hamiddz13@gmail.com", "home@tomtop.com", "invictawacht@gmail.com",
			"jnguyen23@student.rcc.edu", "joedude1@gmail.com",
			"karimquevedo@hotmail.com", "link@tomtop.com", "loginovse@mail.ru",
			"mike2@tomtop.com", "nachtritter@mail.ua", "order@keedex.com",
			"pzsp2010@gmail.com", "ratakaju@ymail.com", "redwwwest@gmail.com",
			"r.nicholson@zackoonline.info", "rwina2011@gmail.com",
			"sales02@tomtop.com", "sales166@tomtop.com", "sales190@tomtop.com",
			"silsheck@aol.com", "synth@yopmail.com", "tomtopplus@gmail.com",
			"tomtop.wholesaler@gmail.com", "travelmate5720g@gmail.com",
			"tva@nm.ru", "vinny.aggarwal@gmail.com", "wike@tomtopblog.com",
			"wojuanmao@hotmail.com", "xichan63@yahoo.co.uk",
			"xxxxshangtsungxxxx@gmail.com", "zaque@nimble3.com",
			"zhaosenlin@qq.com", "zithat@hotmail.com", "zqy234@126.com" });

	public JsonNode getMember(JsonNode jnode) {
		ObjectMapper jsonMapper = new ObjectMapper();

		Member member = new Member();
		member.setAcceptNewsletter(false);
		member.setActivated(jnode.get("is_active").asBoolean());
		member.setActivationCode("");
		member.setBirthday(null);
		// member.setCountryCode(countryCode);
		member.setEmail(jnode.get("email").asText().toLowerCase());
		member.setFirstname(jnode.get("firstname").asText());
		if ("123".equals(jnode.get("gender").asText())) {
			member.setGender(1);
		} else if ("124".equals(jnode.get("gender").asText())) {
			member.setGender(2);
		}
		member.setGroupId(1);
		member.setId(null);
		member.setLastname(jnode.get("lastname").asText());
		member.setMiddlename(jnode.get("middlename").asText());
		// member.setNickname();
		member.setPassword(jnode.get("password_hash").asText());
		member.setPrefix(getValue(jnode, "prefix"));
		member.setSuffix(getValue(jnode, "Suffix"));
		member.setTaxNumber(jnode.get("taxvat").asText());
		// member.setUsername(username);
		member.setWebsiteId(jnode.get("website_id").asInt());
		if (jnode.get("is_active").asBoolean())
			member.setWelcomeEmailSent(true);
		member.setAddresses(Lists.newArrayList(this.getAddress(jnode)));

		return jsonMapper.convertValue(member, JsonNode.class);
	}

	private String getValue(JsonNode jnode, String name) {
		if (null != jnode.get(name)) {
			return jnode.get(name).asText();
		}
		return null;
	}

	private Iterator<MemberAddress> getAddress(JsonNode node) {
		ObjectMapper jsonMapper = new ObjectMapper();
		JsonNode itemsnode = node.get("addresses");

		Iterator<MemberAddress> relist = Iterators.transform(
				itemsnode.iterator(),
				jnode -> {
					MemberAddress maddres = new MemberAddress();
					maddres.setId(jnode.get("entity_id").asInt());
					maddres.setAddressType(1);
					maddres.setCity(jnode.get("city").asText());
					maddres.setCompany("");
					maddres.setCountryCode(jnode.get("country_id").asText());
					maddres.setFax(jnode.get("fax").asText());
					maddres.setIsDefault(false);
					if (node.get("default_shipping") != null
							&& node.get("default_shipping").asText()
									.equals(jnode.get("entity_id").asText())) {
						maddres.setIsDefault(true);
						maddres.setAddressType(1);
					}
					maddres.setLastname(jnode.get("lastname").asText());
					maddres.setFirstname(jnode.get("firstname").asText());
					maddres.setMemberEmail(node.get("email").asText());
					maddres.setMiddlename(jnode.get("middlename").asText());
					maddres.setPostalcode(jnode.get("postcode").asText());
					maddres.setProvince(jnode.get("region").asText());
					maddres.setStreetAddress(jnode.get("street").asText());
					maddres.setTelephone(jnode.get("telephone").asText());
					maddres.setVatnumber(jnode.get("vat_id").asText());
					return maddres;
				});
		List<MemberAddress> maddlist = Lists.newArrayList(relist);
		if (node.get("default_billing") != null) {
			int entityid = node.get("default_billing").asInt();
			Collection<MemberAddress> mlist = Collections2.filter(maddlist,
					obj -> {
						return obj.getId() == entityid;
					});
			ObjectMapper om = new ObjectMapper();
			if (null != mlist && mlist.size() > 0) {
				MemberAddress ma;
				try {
					ma = om.readValue(
							om.writeValueAsString(mlist.toArray()[0]),
							MemberAddress.class);
					ma.setIsDefault(true);
					ma.setAddressType(2);
					maddlist.add(ma);
				} catch (JsonParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JsonMappingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				System.out.println(om.writeValueAsString(maddlist));
				System.out.println("---------------------");
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return maddlist.iterator();
	}

	public JsonNode getMemberPoint(JsonNode jn) {
		ObjectMapper om = new ObjectMapper();

		com.website.dto.member.MemberPoint mp;
		try {
			mp = om.readValue(jn + "", com.website.dto.member.MemberPoint.class);
			if (notsaveEmails.contains(mp.getEmail().toLowerCase().trim())) {
				return null;
			}
			mp.setEmail(mp.getEmail().toLowerCase());
			return om.readValue(om.writeValueAsString(mp), JsonNode.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jn;

	}
}
