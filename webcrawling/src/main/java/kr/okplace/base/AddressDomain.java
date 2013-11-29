package kr.okplace.base;

import java.io.Serializable;

public class AddressDomain implements Serializable {

	private static final long serialVersionUID = 1L;

	private AddressDomain head;
	private String part;
	private String core;
	private AddressDomain tail;

	public AddressDomain(String part) {
		this.part = part;
	}
	
	public AddressDomain(String part, String core, String remainder) {
		this.part = part;
		this.core = core;
		this.tail = new AddressDomain(remainder);
	}

	public AddressDomain(String part, String core, AddressDomain child) {
		this.part = part;
		this.core = core;
		this.tail = child;
	}
	
	public String getNormalized() {
		return tail==null? core: core.concat(tail.getNormalized());
	}
	
	public String getStandardized() {
		return tail==null? part: part.concat(tail.getStandardized());
	}

	public String getPart() {
		return part;
	}

	public void setPart(String part) {
		this.part = part;
	}

	public String getCore() {
		return core;
	}

	public void setCore(String core) {
		this.core = core;
	}

	public AddressDomain getTail() {
		return tail;
	}

	public String getRemainder() {
		return tail==null? null: tail.getPart();
	}

	public void setTail(AddressDomain child) {
		this.tail = child;
	}
}
