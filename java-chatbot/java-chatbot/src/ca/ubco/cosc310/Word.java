package ca.ubco.cosc310;

public class Word {
	
	private String content;
	private String identifier;
	
	public Word(String content, String identifier){
		this.content = content;
		this.identifier = identifier;
	}
	
	public Word() {
		this.content = null;
		this.identifier = null;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	

}
