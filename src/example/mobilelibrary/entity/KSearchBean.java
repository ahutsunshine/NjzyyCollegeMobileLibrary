package example.mobilelibrary.entity;

/**
 * 知识检索--列表bean
 * 
 * 
 */
public class KSearchBean {

	private String title = "";
	private String href = "";
	private String author = "";
	private String source = "";
	private String pubdate = "";
	private String count = "";
	private String journal = "";
	private String downLink = "";
	private String fullLink = "";
	private String summary = "";
	private String isbn="";
	
	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		isbn = isbn == null ? "" : isbn;
		this.isbn = isbn;
	}
	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		summary = summary == null ? "" : summary;
		this.summary = summary;
	}

	public String getJournal() {
		return journal;
	}

	public void setJournal(String journal) {
		journal = journal == null ? "" : journal;
		this.journal = journal;
	}

	public String getDownLink() {
		return downLink;
	}

	public void setDownLink(String downLink) {
		downLink = downLink == null ? "" : downLink;
		this.downLink = downLink;
	}

	public String getFullLink() {
		return fullLink;
	}

	public void setFullLink(String fullLink) {
		fullLink = fullLink == null ? "" : fullLink;
		this.fullLink = fullLink;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		title = title == null ? "" : title;
		this.title = title;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		href = href == null ? "" : href;
		this.href = href;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		author = author == null ? "" : author;
		this.author = author;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		source = source == null ? "" : source;
		this.source = source;
	}

	public String getPubdate() {
		return pubdate;
	}

	public void setPubdate(String pubdate) {
		pubdate = pubdate == null ? "" : pubdate;
		this.pubdate = pubdate;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		count = count == null ? "" : count;
		this.count = count;
	}
}
