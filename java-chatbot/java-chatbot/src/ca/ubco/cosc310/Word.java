package ca.ubco.cosc310;

public class Word {
	
	private String content;
	private String identifier;
	
	Word(String content, String identifier){
		this.content = content;
		this.identifier = identifier;
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
