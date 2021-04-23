package agh.edu.pl.diet.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Product {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String title;
	private String note;
	private String calories;
	//	private String publicationDate;
//	private String language;
	private String category;
	private String totalFat;
	private String totalCarbohydrate;
	private String protein;
	private String sodium;
	private boolean active=true;

//	@Column(columnDefinition="text")
//	private String description;
//	private int inStockNumber;

	@Transient
	private MultipartFile bookImage;


//	@OneToMany(mappedBy = "product")
//	@JsonIgnore
//	private List<BookToCartItem> bookToCartItemList;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getCalories() {
		return calories;
	}

	public void setCalories(String calories) {
		this.calories = calories;
	}

//	public String getPublicationDate() {
//		return publicationDate;
//	}
//
//	public void setPublicationDate(String publicationDate) {
//		this.publicationDate = publicationDate;
//	}
//
//	public String getLanguage() {
//		return language;
//	}
//
//	public void setLanguage(String language) {
//		this.language = language;
//	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}


	public String getTotalFat() {
		return totalFat;
	}

	public void setTotalFat(String totalFat) {
		this.totalFat = totalFat;
	}

	public String getTotalCarbohydrate() {
		return totalCarbohydrate;
	}

	public void setTotalCarbohydrate(String totalCarbohydrate) {
		this.totalCarbohydrate = totalCarbohydrate;
	}

	public String getProtein() {
		return protein;
	}

	public void setProtein(String protein) {
		this.protein = protein;
	}

	public String getSodium() {
		return sodium;
	}

	public void setSodium(String sodium) {
		this.sodium = sodium;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

//	public int getInStockNumber() {
//		return inStockNumber;
//	}
//
//	public void setInStockNumber(int inStockNumber) {
//		this.inStockNumber = inStockNumber;
//	}

	public MultipartFile getBookImage() {
		return bookImage;
	}

	public void setBookImage(MultipartFile bookImage) {
		this.bookImage = bookImage;
	}

//	public List<BookToCartItem> getBookToCartItemList() {
//		return bookToCartItemList;
//	}
//
//	public void setBookToCartItemList(List<BookToCartItem> bookToCartItemList) {
//		this.bookToCartItemList = bookToCartItemList;
//	}


}
