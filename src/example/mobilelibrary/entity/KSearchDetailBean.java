package example.mobilelibrary.entity;


/**
 * ÖªÊ¶¼ìË÷---ÏêÏ¸bean
 * 
 
 *
 */
public class KSearchDetailBean {

	private String chTitle="";
	private String enTitle="";
	private String downLink="";
	private String author="";
	private String institute="";
	private String summary="";
	private String content="";
	private String pubdate="";
	public String getPubdate() {
		return pubdate;
	}

	public void setPubdate(String Pubdate) {
		pubdate=Pubdate==null?"":Pubdate;
		this.pubdate = Pubdate;
	}
	public String getChTitle() {
		return chTitle;
	}

	public void setChTitle(String chTitle) {
		chTitle=chTitle==null?"":chTitle;
		this.chTitle = chTitle;
	}
	
	public String getEnTitle() {
		return enTitle;
	}

	public void setEnTitle(String enTitle) {
		enTitle=enTitle==null?"":enTitle;
		this.enTitle = enTitle;
	}
	
	public void setContent(String content) {
		content=content==null?"":content;
		this.content = content;
	}
	public String getContent() {
		return content;
	}
	public String getDownLink() {
		return downLink;
	}

	public void setDownLink(String downLink) {
		downLink=downLink==null?"":downLink;
		this.downLink = downLink;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		author=author==null?"":author;
		this.author = author;
	}

	public String getInstitute() {
		return institute;
	}

	public void setInstitute(String institute) {
		institute=institute==null?"":institute;
		this.institute = institute;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		summary=summary==null?"":summary;
		this.summary = summary;
	}

	public String getTitle() {
		if(getEnTitle().contentEquals(""))
			return getChTitle();
		else if(getChTitle().contentEquals(""))
			return getEnTitle();
		else return getChTitle() + "\n" + getEnTitle();
	}

}
