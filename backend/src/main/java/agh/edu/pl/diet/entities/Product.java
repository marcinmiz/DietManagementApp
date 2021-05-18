package agh.edu.pl.diet.entities;

import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String productName;
    private String productAuthor;
    private String calories;
    //	private String publicationDate;
//	private String language;
    private String category;
    private String totalFat;
    private String totalCarbohydrate;
    private String protein;
    private String sodium;
    private boolean active = true;

//	@Column(columnDefinition="text")
//	private String description;
//	private int inStockNumber;

    @Transient
    private MultipartFile productImage;


//	@OneToMany(mappedBy = "product")
//	@JsonIgnore
//	private List<BookToCartItem> bookToCartItemList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductAuthor() {
        return productAuthor;
    }

    public void setProductAuthor(String productAuthor) {
        this.productAuthor = productAuthor;
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

    public MultipartFile getProductImage() {
        return productImage;
    }

    public void setProductImage(MultipartFile productImage) {
        this.productImage = productImage;
    }

//	public List<BookToCartItem> getBookToCartItemList() {
//		return bookToCartItemList;
//	}
//
//	public void setBookToCartItemList(List<BookToCartItem> bookToCartItemList) {
//		this.bookToCartItemList = bookToCartItemList;
//	}


}