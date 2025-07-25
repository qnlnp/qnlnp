package kr.co.sist.link;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LinkDTO {
	private String urlName, url;

	// 쿼리스트링 활용해보려고~
	private String name;
	private int age;
}
